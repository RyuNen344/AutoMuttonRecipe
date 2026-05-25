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

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.message
import assertk.assertions.prop
import io.github.ryunen344.mutton.compose.testing.SerializableAction
import io.github.ryunen344.mutton.compose.testing.SerializableState
import io.github.ryunen344.mutton.compose.testing.SerializableStateMachine
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.SerializationException
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SerializableStateMachineSaverTest {
    @get:Rule
    val rule = createComposeRule()

    private val restorationTester = StateRestorationTester(rule)

    @Test
    fun testRestore_givenPlatformSerializableState_thenSuccessRestore() {
        var stateMachine: SerializableStateMachine? = null
        restorationTester.setContent {
            val coroutineScope = rememberCoroutineScope()
            stateMachine = rememberSerializableStateMachine<SerializableStateMachine, SerializableState>(initialState = SerializableState.Idle(0)) {
                SerializableStateMachine(
                    initialState = it,
                    context = coroutineScope.coroutineContext,
                )
            }
        }

        restorationTester.emulateSavedInstanceStateRestore()
        assertThat(stateMachine)
            .isNotNull()
            .prop(SerializableStateMachine::state)
            .prop(StateFlow<SerializableState>::value)
            .isDataClassEqualTo(SerializableState.Idle(0))
    }

    @Test
    fun testRestore_givenParcelableState_thenSuccessRestore() {
        var stateMachine: SerializableStateMachine? = null
        restorationTester.setContent {
            val coroutineScope = rememberCoroutineScope()
            stateMachine = rememberSerializableStateMachine<SerializableStateMachine, SerializableState>(initialState = SerializableState.Idle(0)) { state ->
                SerializableStateMachine(
                    initialState = state,
                    context = coroutineScope.coroutineContext,
                )
            }
        }

        rule.runOnUiThread {
            with(requireNotNull(stateMachine)) {
                dispatch(SerializableAction.Start)
                dispatch(SerializableAction.Stop)
                dispatch(SerializableAction.Clean)
                dispatch(SerializableAction.Start)
            }

            // we null it to ensure recomposition happened
            stateMachine = null
        }

        restorationTester.emulateSavedInstanceStateRestore()

        assertThat(stateMachine)
            .isNotNull()
            .prop(SerializableStateMachine::state)
            .prop(StateFlow<SerializableState>::value)
            .isDataClassEqualTo(SerializableState.Running(2))
    }

    @Test
    fun testRestore_givenUnsupportedState_thenThrowsException() {
        var stateMachine: SerializableStateMachine? = null
        restorationTester.setContent {
            val coroutineScope = rememberCoroutineScope()
            stateMachine = rememberSerializableStateMachine<SerializableStateMachine, SerializableState>(initialState = SerializableState.Idle(0)) {
                SerializableStateMachine(
                    initialState = it,
                    context = coroutineScope.coroutineContext,
                )
            }
        }

        rule.runOnUiThread {
            with(requireNotNull(stateMachine)) {
                dispatch(SerializableAction.Start)
                dispatch(SerializableAction.Stop)
            }

            // we null it to ensure recomposition happened
            stateMachine = null
        }

        assertFailure { restorationTester.emulateSavedInstanceStateRestore() }
            .isInstanceOf(SerializationException::class)
            .message()
            .isEqualTo(
                "Serializer for subclass 'Stopped' is not found in the polymorphic scope of 'SerializableState'.\n" +
                    "Check if class with serial name 'Stopped' exists and serializer is registered in a corresponding SerializersModule.\n" +
                    "To be registered automatically, class 'Stopped' has to be '@Serializable', " +
                    "and the base class 'SerializableState' has to be sealed and '@Serializable'.",
            )
    }
}
