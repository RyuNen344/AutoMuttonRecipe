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

package io.github.ryunen344.mutton.compose

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StateMachineSaverTest {

    @get:Rule
    val rule = createComposeRule()

    private val restorationTester = StateRestorationTester(rule)

    @Test
    fun testStateMachineSaver() {
        var stateMachine: SampleStateMachine? = null
        restorationTester.setContent {
            val coroutineScope = rememberCoroutineScope()
            stateMachine = rememberStateMachine<SampleStateMachine, SampleState>(initialState = SampleState.Idle(0)) { state ->
                SampleStateMachine(
                    initialState = state,
                    context = coroutineScope.coroutineContext,
                )
            }
        }

        rule.runOnUiThread {
            with(requireNotNull(stateMachine)) {
                dispatch(SampleAction.Start)
                dispatch(SampleAction.Stop)
                dispatch(SampleAction.Clean)
                dispatch(SampleAction.Start)
            }

            // we null it to ensure recomposition happened
            stateMachine = null
        }

        restorationTester.emulateSavedInstanceStateRestore()

        assertThat(stateMachine)
            .isNotNull()
            .prop(SampleStateMachine::state)
            .prop(StateFlow<SampleState>::value)
            .isDataClassEqualTo(SampleState.Running(2))
    }
}
