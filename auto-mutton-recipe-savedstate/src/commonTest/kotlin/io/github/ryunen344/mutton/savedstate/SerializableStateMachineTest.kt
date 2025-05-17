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
import io.github.ryunen344.mutton.savedstate.testing.SerializableState
import io.github.ryunen344.mutton.savedstate.testing.SerializableStateMachine
import kotlin.test.Test

abstract class SerializableStateMachineTest {
    @Test
    fun testSaveState_givenSupportedState_thenSaveState() {
        val expect = SerializableState.OK("ok value")
        val handle = SavedStateHandle()
        val stateMachine = SerializableStateMachine(handle, "key", expect)

        val savedState = handle.savedStateProvider().saveState()
        val newHandle = SavedStateHandle.createHandle(savedState, null)
        val newStateMachine = SerializableStateMachine(newHandle, "key", SerializableState.Error("error massage"))

        assertThat(newStateMachine.state.value).isEqualTo(expect)
        assertThat(newStateMachine.state.value).isEqualTo(stateMachine.state.value)
    }

    @Test
    open fun testSaveState_givenUnsupportedState_thenSaveState() {
        val expect = SerializableState.Error("error message")
        val handle = SavedStateHandle()
        val stateMachine = SerializableStateMachine(handle, "key", expect)

        val savedState = handle.savedStateProvider().saveState()
        val newHandle = SavedStateHandle.createHandle(savedState, null)
        val newStateMachine = SerializableStateMachine(newHandle, "key", SerializableState.OK("ok value"))

        assertThat(newStateMachine.state.value).isEqualTo(expect)
        assertThat(newStateMachine.state.value).isEqualTo(stateMachine.state.value)
    }
}
