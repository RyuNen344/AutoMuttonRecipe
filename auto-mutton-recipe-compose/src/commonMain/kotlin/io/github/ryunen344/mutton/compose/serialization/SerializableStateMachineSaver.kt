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

package io.github.ryunen344.mutton.compose.serialization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.savedstate.SavedState
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.StateMachine

/**
 * A [Saver] for [StateMachine] that saves the current state with [SavedState].
 */
public inline fun <T : StateMachine<S, *, *>, reified S : State> serializableStateMachineSaver(
    configuration: SavedStateConfiguration = SavedStateConfiguration.DEFAULT,
    crossinline restore: (value: S) -> T,
): Saver<T, SavedState> = Saver(
    save = {
        encodeToSavedState(it.state.value, configuration)
    },
    restore = {
        restore(decodeFromSavedState<S>(it, configuration))
    },
)

/**
 * Remember a [StateMachine] that is remembered across compositions and configuration changes.
 *
 * @sample io.github.ryunen344.mutton.compose.samples.SerializableStateMachineSaverSample
 */
@Composable
public inline fun <T : StateMachine<S, *, *>, reified S : State> rememberSerializableStateMachine(
    vararg inputs: Any?,
    initialState: S,
    key: String? = null,
    configuration: SavedStateConfiguration = SavedStateConfiguration.DEFAULT,
    crossinline init: (state: S) -> T,
): T {
    return rememberSaveable(
        inputs = inputs,
        saver = serializableStateMachineSaver<T, S>(configuration, init),
        key = key,
    ) {
        init(initialState)
    }
}
