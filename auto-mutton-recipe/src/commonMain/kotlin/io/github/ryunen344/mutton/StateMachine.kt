/*
 * Copyright (C) 2025 RyuNen344
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE.md
 */

package io.github.ryunen344.mutton

import io.github.ryunen344.mutton.coroutine.ExceptionCapturedAction
import io.github.ryunen344.mutton.coroutine.ExceptionCapturedEffect
import io.github.ryunen344.mutton.coroutine.ExceptionCapturedState
import io.github.ryunen344.mutton.coroutine.withReentrantLock
import io.github.ryunen344.mutton.log.Logger
import io.github.ryunen344.mutton.log.NoopLogger
import io.github.ryunen344.mutton.log.error
import io.github.ryunen344.mutton.log.info
import io.github.ryunen344.mutton.log.warn
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

public abstract class StateMachine<S, A, E>(
    initialState: S,
    public val graph: Graph<S, A, E>,
    public val effectHandle: EffectHandle<S, A, E>,
    public val fallbackHandle: FallbackHandle<S, A, E>? = null,
    public val logger: Logger = NoopLogger,
    public val context: CoroutineContext = EmptyCoroutineContext,
) where S : State, A : Action, E : Effect {

    private val name: String by lazy { this::class.simpleName.orEmpty() }

    private val mutex: Mutex by lazy { Mutex() }

    public val scope: CoroutineScope by lazy { CoroutineScope(CoroutineName(name) + SupervisorJob() + context + exceptionHandler) }

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { context, exception ->
            @Suppress("UNCHECKED_CAST")
            val capturedState = context[ExceptionCapturedState]?.state as? S

            @Suppress("UNCHECKED_CAST")
            val capturedAction = context[ExceptionCapturedAction]?.action as? A

            @Suppress("UNCHECKED_CAST")
            val capturedEffect = context[ExceptionCapturedEffect]?.effect as? E

            logger.error(name, exception) {
                "StateMachine caught unhandled exception at state:[$capturedState], action:[$capturedAction], effect:[$capturedEffect]"
            }

            if (fallbackHandle != null) {
                val action = fallbackHandle(capturedState, capturedAction, capturedEffect, exception)
                dispatch(action)
            } else {
                throw exception
            }
        }
    }

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    public val state: StateFlow<S> = _state.asStateFlow()

    public fun dispatch(action: A) {
        scope.launch(CoroutineName("$name:dispatch") + ExceptionCapturedAction(action)) {
            mutex.withReentrantLock {
                val current = _state.value
                withContext(coroutineContext + ExceptionCapturedState(current)) {
                    try {
                        val transition = graph.edges[current::class]?.get(action::class)?.invoke(current, action)
                        if (transition != null) {
                            val updated = _state.compareAndSet(current, transition.next)
                            if (updated) {
                                logger.info(name) { "dispatch:[$action], current state:[$current], transition:[$transition]" }
                                transition.effect?.let { effect(it, current, transition.next) }
                            } else {
                                logger.warn(name) { "un-dispatched action:[$action], current state:[$current], transition:[$transition]" }
                            }
                        } else {
                            logger.warn(name) { "unhandled action:[$action], current state:[$current], transition:[null]" }
                        }
                        yield()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
                        @OptIn(InternalCoroutinesApi::class)
                        handleCoroutineException(currentCoroutineContext(), e)
                    }
                }
            }
        }
    }

    private fun effect(effect: E, prev: S, current: S) {
        scope.launch(CoroutineName("$name:effect") + ExceptionCapturedState(current) + ExceptionCapturedEffect(effect)) {
            mutex.withReentrantLock {
                logger.info(name) { "effect:[$effect], prev:[$prev], current:[$current]" }
                effectHandle(effect, prev, current, ::dispatch)
            }
        }
    }
}
