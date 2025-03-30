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

import io.github.ryunen344.mutton.Action
import io.github.ryunen344.mutton.Effect
import io.github.ryunen344.mutton.EffectHandle
import io.github.ryunen344.mutton.Graph
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.StateMachine
import kotlin.coroutines.CoroutineContext

sealed class SampleState : State(), java.io.Serializable {
    abstract val launchedCount: Int

    data class Idle(override val launchedCount: Int) : SampleState()
    data class Running(override val launchedCount: Int) : SampleState()
    data class Stopped(override val launchedCount: Int) : SampleState()
}

sealed class SampleAction : Action() {
    data object Start : SampleAction()
    data object Stop : SampleAction()
    data object Clean : SampleAction()
}

sealed class SampleEffect : Effect()

class SampleStateMachine(
    initialState: SampleState,
    context: CoroutineContext,
) : StateMachine<SampleState, SampleAction, SampleEffect>(
    initialState = initialState,
    graph = Graph<SampleState, SampleAction, SampleEffect> {
        state<SampleState.Idle> {
            action<SampleAction.Start> { prev, _ ->
                transition(SampleState.Running(launchedCount = prev.launchedCount + 1))
            }
        }
        state<SampleState.Running> {
            action<SampleAction.Stop> { prev, _ ->
                transition(SampleState.Stopped(launchedCount = prev.launchedCount))
            }
        }
        state<SampleState.Stopped> {
            action<SampleAction.Clean> { prev, _ ->
                transition(SampleState.Idle(launchedCount = prev.launchedCount))
            }
        }
    },
    effectHandle = EffectHandle<SampleState, SampleAction, SampleEffect> { _, _, _, _ ->
        // noop
    },
    context = context,
)
