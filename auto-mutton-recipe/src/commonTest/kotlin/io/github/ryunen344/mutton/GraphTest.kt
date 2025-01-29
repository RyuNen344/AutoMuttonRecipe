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

import assertk.assertFailure
import assertk.assertions.isEqualTo
import io.github.ryunen344.mutton.testing.SampleAction
import io.github.ryunen344.mutton.testing.SampleEffect
import io.github.ryunen344.mutton.testing.SampleState
import kotlin.test.Test

class GraphTest {

    @Test
    fun testBuild_whenGraphBuilderThrowsException_thenThrowsException() {
        val expect = IllegalStateException("GraphBuilder throws exception")
        val actual = assertFailure {
            Graph<SampleState, SampleAction, SampleEffect> { throw expect }
        }
        actual.isEqualTo(expect)
    }

    @Test
    fun testBuild_whenActionBuilderThrowsException_thenThrowsException() {
        val expect = IllegalStateException("GraphBuilder#state throws exception")
        val actual = assertFailure {
            Graph<SampleState, SampleAction, SampleEffect> {
                state<SampleState.Idle> {
                    action<SampleAction.Start> { _, _ ->
                        transition(SampleState.Running(launchedCount = 1))
                    }
                    throw expect
                }
            }
        }
        actual.isEqualTo(expect)
    }
}
