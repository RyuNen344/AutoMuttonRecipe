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

import app.cash.turbine.turbineScope
import io.github.ryunen344.mutton.testing.SampleAction
import io.github.ryunen344.mutton.testing.SampleEffect
import io.github.ryunen344.mutton.testing.SampleState
import io.github.ryunen344.mutton.testing.SampleStateMachine
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StateMachineTest {
    @Test
    fun testGraph() = runTest {
        println(graph)
        graph.edges.forEach { (state, actions) ->
            println("when state is $state")
            actions.forEach { (action, transition) ->
                println("given action $action")
                println("then transition to $transition")
            }
        }
    }

    @Test
    fun testStateMachine() = runTest {
        turbineScope {
            val stateMachine = SampleStateMachine(
                graph = graph,
                effectHandle = effectHandle,
                fallbackHandle = fallbackHandle,
                context = UnconfinedTestDispatcher(testScheduler),
            )
            val receiver = stateMachine.state.testIn(this)
            async { stateMachine.dispatch(SampleAction.Start) }
            async { stateMachine.dispatch(SampleAction.Start) }
            advanceUntilIdle()
            stateMachine.dispatch(SampleAction.Start)
            advanceUntilIdle()
            receiver.cancelAndConsumeRemainingEvents().forEach {
                println(it)
            }
        }
    }
}

val graph = Graph<SampleState, SampleAction, SampleEffect> {
    state<SampleState.Idle> {
        action<SampleAction.Start> { prev, action ->
            transition(SampleState.Running(prev.launchedCount + 1), SampleEffect.Started)
        }
    }
    state<SampleState.Running> {
        action<SampleAction.Stop> { prev, action ->
            transition(SampleState.Stopped(prev.launchedCount), SampleEffect.Stopped)
        }
    }
    state<SampleState.Stopped> {
        action<SampleAction.Clean> { prev, action ->
            transition(SampleState.Idle(prev.launchedCount))
        }
    }
}

val effectHandle = EffectHandle<SampleState, SampleAction, SampleEffect> { effect, prev, next, dispatch ->
    when (effect) {
        SampleEffect.Started -> {
            delay(3000L)
            dispatch(SampleAction.Stop)
        }

        SampleEffect.Stopped -> {
            delay(3000L)
            dispatch(SampleAction.Clean)
        }
    }
}

val fallbackHandle = FallbackHandle<SampleState, SampleAction, SampleEffect> { state, action, effect, throwable ->
    SampleAction.Clean
}
