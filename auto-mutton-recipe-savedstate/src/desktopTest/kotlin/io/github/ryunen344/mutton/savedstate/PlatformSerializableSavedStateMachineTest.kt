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

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.ryunen344.mutton.savedstate.testing.PlatformSerializableState
import io.github.ryunen344.mutton.savedstate.testing.PlatformSerializableStateMachine
import kotlin.test.Test

class PlatformSerializableSavedStateMachineTest {

    @Test
    fun testSaveState_givenSupportedState_thenSaveState() {
        val expect = PlatformSerializableState.OK("ok value")
        val handle = SavedStateHandle()
        val stateMachine = PlatformSerializableStateMachine(handle, "key", expect)

        val savedState = handle.savedStateProvider().saveState()
        val newHandle = SavedStateHandle.createHandle(savedState, null)
        val newStateMachine = PlatformSerializableStateMachine(newHandle, "key", PlatformSerializableState.Error("error massage"))

        assertThat(newStateMachine.state.value).isEqualTo(expect)
        assertThat(newStateMachine.state.value).isEqualTo(stateMachine.state.value)
    }

    @Test
    fun testSaveState_givenUnsupportedState_thenSaveState() {
        val expect = PlatformSerializableState.Error("error massage")
        val handle = SavedStateHandle()
        val stateMachine = PlatformSerializableStateMachine(handle, "key", expect)

        val savedState = handle.savedStateProvider().saveState()
        val newHandle = SavedStateHandle.createHandle(savedState, null)
        val newStateMachine = PlatformSerializableStateMachine(newHandle, "key", PlatformSerializableState.OK("ok value"))

        assertThat(newStateMachine.state.value).isEqualTo(expect)
        assertThat(newStateMachine.state.value).isEqualTo(stateMachine.state.value)
    }
}
