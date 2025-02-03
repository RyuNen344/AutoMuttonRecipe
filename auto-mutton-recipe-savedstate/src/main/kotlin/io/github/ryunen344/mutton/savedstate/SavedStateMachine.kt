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

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.core.os.bundleOf
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
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A [StateMachine] that saves its state to [SavedStateHandle]
 *
 * - **Can't update state via [SavedStateHandle] directly, use [StateMachine.dispatch]**
 * - Planned to be updated after androidx.lifecycle:lifecycle-viewmodel-savedstate:2.9.* was released
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
) : StateMachine<S, A, E>(
    initialState = savedStateHandle.get<Bundle>(key)?.get(key) as? S ?: initialState,
    graph = graph,
    effectHandle = effectHandle,
    fallbackHandle = fallbackHandle,
    logger = logger,
    context = context,
) where S : State, A : Action, E : Effect {
    init {
        savedStateHandle.setSavedStateProvider(key) {
            bundleOf(key to state.value)
        }
    }
}
