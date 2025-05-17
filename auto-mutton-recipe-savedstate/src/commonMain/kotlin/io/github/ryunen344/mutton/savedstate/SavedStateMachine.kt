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

package io.github.ryunen344.mutton.savedstate

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import io.github.ryunen344.mutton.Action
import io.github.ryunen344.mutton.Effect
import io.github.ryunen344.mutton.EffectHandle
import io.github.ryunen344.mutton.FallbackHandle
import io.github.ryunen344.mutton.Graph
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.StateMachine
import io.github.ryunen344.mutton.log.Logger
import io.github.ryunen344.mutton.log.NoopLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A [StateMachine] that saves its state to [SavedStateHandle]
 */
@Suppress("UNCHECKED_CAST", "DEPRECATION")
public abstract class SavedStateMachine<S, A, E> @MainThread constructor(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialState: S,
    graph: Graph<S, A, E>,
    effectHandle: EffectHandle<S, A, E>,
    fallbackHandle: FallbackHandle<S, A, E>? = null,
    logger: Logger = NoopLogger,
    context: CoroutineContext = EmptyCoroutineContext,
    initializer: (S) -> MutableStateFlow<S> = { s -> savedStateHandle.getMutableStateFlow(key, s) },
) : StateMachine<S, A, E>(
    initialState = savedStateHandle[key] as? S ?: initialState,
    graph = graph,
    effectHandle = effectHandle,
    fallbackHandle = fallbackHandle,
    logger = logger,
    context = context,
    initializer = initializer,
) where S : State, A : Action, E : Effect
