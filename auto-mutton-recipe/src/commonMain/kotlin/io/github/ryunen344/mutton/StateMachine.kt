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

import io.github.ryunen344.mutton.coroutine.withReentrantLock
import io.github.ryunen344.mutton.log.Logger
import io.github.ryunen344.mutton.log.NoopLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

public abstract class StateMachine<S, A, E>(
    initialState: S,
    public val graph: Graph<S, A, E>,
    public val effectHandle: EffectHandle<S, A, E>,
    public val fallback: (suspend (Throwable) -> A)? = null,
    public val logger: Logger = NoopLogger,
    public val context: CoroutineContext = EmptyCoroutineContext,
) where S : State, A : Action, E : Effect {

    private val name: String by lazy { this::class.simpleName.orEmpty() }

    private val mutex: Mutex by lazy { Mutex() }

    public val scope: CoroutineScope by lazy { CoroutineScope(SupervisorJob() + context) }

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            logger.log(name, exception) { "StateMachine caught unhandled exception" }
            if (fallback != null) {
                scope.launch {
                    dispatch(fallback(exception))
                }
            } else {
                throw exception
            }
        }
    }

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    public val state: StateFlow<S> = _state.asStateFlow()

    public fun dispatch(action: A) {
        scope.launch(exceptionHandler) {
            mutex.withReentrantLock {
                val current = _state.value
                val transition = graph.edges[current::class]?.get(action::class)?.invoke(current, action)
                if (transition != null) {
                    logger.log(name) { "dispatch:[$action], current state:[$current], transition:[$transition]" }
                    _state.update { transition.next }
                    transition.effect?.let { effect(it, current, transition.next) }
                } else {
                    logger.log(name) { "unhandled action:[$action], current state:[$current], transition:[null]" }
                }
            }
        }
    }

    private fun effect(effect: E, prev: S, current: S) {
        scope.launch(exceptionHandler) {
            mutex.withReentrantLock {
                logger.log(name) { "effect:[$effect], prev:[$prev], current:[$current]" }
                effectHandle(effect, prev, current, ::dispatch)
            }
        }
    }
}
