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

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.github.ryunen344.mutton.testing.SampleAction
import io.github.ryunen344.mutton.testing.SampleEffect
import io.github.ryunen344.mutton.testing.SampleState
import io.github.ryunen344.mutton.testing.SampleStateMachine
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class FallbackHandleTest {

    @Test
    fun testDispatch_whenTransitionThrowsException_thenCatchException() = runTest {
        val expectException = IllegalStateException("dispatch throws exception")
        var actualState: SampleState? = null
        var actualAction: SampleAction? = null
        var actualEffect: SampleEffect? = null
        var actualException: Throwable? = null
        val graph = Graph<SampleState, SampleAction, SampleEffect> {
            state<SampleState.Idle> {
                action<SampleAction.Start> { _, _ ->
                    transition(SampleState.Running(launchedCount = 1))
                }
                action<SampleAction.Stop> { _, _ ->
                    throw expectException
                }
            }
        }
        val stateMachine = SampleStateMachine(
            graph = graph,
            effectHandle = EffectHandle<SampleState, SampleAction, SampleEffect> { _, _, _, _ ->
                // noop
            },
            fallbackHandle = FallbackHandle<SampleState, SampleAction, SampleEffect> { state, action, effect, exception ->
                actualState = state
                actualAction = action
                actualEffect = effect
                actualException = exception
                SampleAction.Start
            },
            context = UnconfinedTestDispatcher(testScheduler),
        )
        stateMachine.dispatch(SampleAction.Stop)
        advanceUntilIdle()

        assertThat(actualState)
            .isNotNull()
            .isEqualTo(SampleState.Idle(launchedCount = 0))

        assertThat(actualAction)
            .isNotNull()
            .isEqualTo(SampleAction.Stop)

        assertThat(actualEffect).isNull()

        assertThat(actualException)
            .isNotNull()
            .isEqualTo(expectException)
    }

    @Test
    fun testDispatch_whenEffectThrowsException_thenCatchException() = runTest {
        val expectException = IllegalStateException("effect throws exception")
        var actualState: SampleState? = null
        var actualAction: SampleAction? = null
        var actualEffect: SampleEffect? = null
        var actualException: Throwable? = null
        val graph = Graph<SampleState, SampleAction, SampleEffect> {
            state<SampleState.Idle> {
                action<SampleAction.Start> { _, _ ->
                    transition(SampleState.Running(launchedCount = 1), SampleEffect.Started)
                }
            }
        }
        val stateMachine = SampleStateMachine(
            graph = graph,
            effectHandle = EffectHandle<SampleState, SampleAction, SampleEffect> { effect, _, _, _ ->
                when (effect) {
                    SampleEffect.Started -> throw expectException
                    SampleEffect.Stopped -> {
                        // noop
                    }
                }
            },
            fallbackHandle = FallbackHandle<SampleState, SampleAction, SampleEffect> { state, action, effect, exception ->
                actualState = state
                actualAction = action
                actualEffect = effect
                actualException = exception
                SampleAction.Clean
            },
            context = UnconfinedTestDispatcher(testScheduler),
        )
        stateMachine.dispatch(SampleAction.Start)
        advanceUntilIdle()

        assertThat(actualState)
            .isNotNull()
            .isEqualTo(SampleState.Running(launchedCount = 1))

        assertThat(actualAction).isNull()

        assertThat(actualEffect)
            .isNotNull()
            .isEqualTo(SampleEffect.Started)

        assertThat(actualException)
            .isNotNull()
            .isEqualTo(expectException)
    }
}
