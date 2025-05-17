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

package io.github.ryunen344.mutton.savedstate.testing

import androidx.lifecycle.SavedStateHandle
import io.github.ryunen344.mutton.EffectHandle
import io.github.ryunen344.mutton.Graph
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.savedstate.SavedStateMachine

sealed class PlatformSerializableState : State() {
    data class OK(val value: String) : PlatformSerializableState(), java.io.Serializable
    data class Error(val message: String) : PlatformSerializableState()
}

class PlatformSerializableStateMachine(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialState: PlatformSerializableState,
) : SavedStateMachine<PlatformSerializableState, SavedAction, SavedEffect>(
    savedStateHandle = savedStateHandle,
    key = key,
    initialState = initialState,
    graph = Graph<PlatformSerializableState, SavedAction, SavedEffect> { },
    effectHandle = EffectHandle<PlatformSerializableState, SavedAction, SavedEffect> { _, _, _, _ -> },
)
