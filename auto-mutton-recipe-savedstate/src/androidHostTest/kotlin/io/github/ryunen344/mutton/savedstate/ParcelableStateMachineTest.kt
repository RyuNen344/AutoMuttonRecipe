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
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.github.ryunen344.mutton.savedstate.testing.MainDispatcherRule
import io.github.ryunen344.mutton.savedstate.testing.ParcelableState
import io.github.ryunen344.mutton.savedstate.testing.ParcelableStateMachine
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParcelableStateMachineTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testSaveState_givenSupportedState_thenSaveState() {
        val expect = ParcelableState.OK("ok value")
        val handle = SavedStateHandle()
        val stateMachine = ParcelableStateMachine(handle, "key", expect)

        val savedState = handle.savedStateProvider().saveState()
        val newHandle = SavedStateHandle.createHandle(savedState, null)
        val newStateMachine = ParcelableStateMachine(newHandle, "key", ParcelableState.Error("error massage"))

        assertThat(newStateMachine.state.value).isEqualTo(expect)
        assertThat(newStateMachine.state.value).isEqualTo(stateMachine.state.value)
    }

    @Test
    fun testSaveState_givenUnsupportedState_thenThrowsException() {
        val expect = ParcelableState.Error("error massage")
        val handle = SavedStateHandle()
        ParcelableStateMachine(handle, "key", expect)

        assertFailure { handle.savedStateProvider().saveState() }
            .isInstanceOf(IllegalArgumentException::class)
            .hasMessage("Illegal value type io.github.ryunen344.mutton.savedstate.testing.ParcelableState.Error for key \"key\"")
    }
}
