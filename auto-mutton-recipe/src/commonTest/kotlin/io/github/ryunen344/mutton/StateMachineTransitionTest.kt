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

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.ryunen344.mutton.testing.TestLogger
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test

class StateMachineTransitionTest {

    sealed class MatterState : State() {
        data object Solid : MatterState()
        data object Liquid : MatterState()
        data object Gas : MatterState()
    }

    sealed class MatterAction : Action() {
        object Melt : MatterAction()
        object Freeze : MatterAction()
        object Vaporize : MatterAction()
        object Condense : MatterAction()
    }

    sealed class MatterEffect : Effect()

    class MatterStateMachine(
        initialState: MatterState = MatterState.Solid,
        context: CoroutineContext,
    ) : StateMachine<MatterState, MatterAction, MatterEffect>(
        initialState = initialState,
        graph = Graph<MatterState, MatterAction, MatterEffect> {
            state<MatterState.Solid> {
                action<MatterAction.Melt> { _, _ ->
                    transition(MatterState.Liquid)
                }
            }
            state<MatterState.Liquid> {
                action<MatterAction.Freeze> { _, _ ->
                    transition(MatterState.Solid)
                }
                action<MatterAction.Vaporize> { _, _ ->
                    transition(MatterState.Gas)
                }
            }
            state<MatterState.Gas> {
                action<MatterAction.Condense> { _, _ ->
                    transition(MatterState.Liquid)
                }
            }
        },
        effectHandle = EffectHandle<MatterState, MatterAction, MatterEffect> { _, _, _, _ ->
            // noop
        },
        logger = TestLogger,
        context = context,
    )

    // region Solid
    @Test
    fun testMatter_givenSolid_whenDispatchMelt_thenTransitionToLiquid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Solid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Solid)
            stateMachine.dispatch(MatterAction.Melt)
            assertThat(awaitItem()).isEqualTo(MatterState.Liquid)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenSolid_whenDispatchFreeze_thenRemainSolid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Solid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Solid)
            stateMachine.dispatch(MatterAction.Freeze)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenSolid_whenDispatchVaporize_thenRemainSolid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Solid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Solid)
            stateMachine.dispatch(MatterAction.Vaporize)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenSolid_whenDispatchCondense_thenRemainSolid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Solid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Solid)
            stateMachine.dispatch(MatterAction.Condense)
            ensureAllEventsConsumed()
        }
    }
    // endregion

    // region Liquid
    @Test
    fun testMatter_givenLiquid_whenDispatchMelt_thenRemainLiquid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Liquid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Liquid)
            stateMachine.dispatch(MatterAction.Melt)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenLiquid_whenDispatchFreeze_thenTransitionToSolid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Liquid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Liquid)
            stateMachine.dispatch(MatterAction.Freeze)
            assertThat(awaitItem()).isEqualTo(MatterState.Solid)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenLiquid_whenDispatchVaporize_thenTransitionToGas() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Liquid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Liquid)
            stateMachine.dispatch(MatterAction.Vaporize)
            assertThat(awaitItem()).isEqualTo(MatterState.Gas)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenLiquid_whenDispatchCondense_thenRemainLiquid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Liquid, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Liquid)
            stateMachine.dispatch(MatterAction.Condense)
            ensureAllEventsConsumed()
        }
    }
    // endregion

    // region Gas
    @Test
    fun testMatter_givenGas_whenDispatchMelt_thenRemainGas() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Gas, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Gas)
            stateMachine.dispatch(MatterAction.Melt)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenGas_whenDispatchFreeze_thenRemainGas() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Gas, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Gas)
            stateMachine.dispatch(MatterAction.Freeze)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenGas_whenDispatchVaporize_thenRemainGas() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Gas, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Gas)
            stateMachine.dispatch(MatterAction.Vaporize)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun testMatter_givenGas_whenDispatchCondense_thenTransitionToLiquid() = runTest {
        val stateMachine = MatterStateMachine(initialState = MatterState.Gas, context = testScheduler)
        stateMachine.state.test {
            assertThat(awaitItem()).isEqualTo(MatterState.Gas)
            stateMachine.dispatch(MatterAction.Condense)
            assertThat(awaitItem()).isEqualTo(MatterState.Liquid)
            ensureAllEventsConsumed()
        }
    }
    // endregion
}
