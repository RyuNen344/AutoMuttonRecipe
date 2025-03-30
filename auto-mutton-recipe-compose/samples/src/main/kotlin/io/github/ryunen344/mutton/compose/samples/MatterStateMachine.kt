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

package io.github.ryunen344.mutton.compose.samples

import io.github.ryunen344.mutton.Action
import io.github.ryunen344.mutton.Effect
import io.github.ryunen344.mutton.EffectHandle
import io.github.ryunen344.mutton.Graph
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.StateMachine
import kotlin.coroutines.CoroutineContext

@Suppress("JavaIoSerializableObjectMustHaveReadResolve")
public sealed class MatterState : State(), java.io.Serializable {
    public data object Solid : MatterState()
    public data object Liquid : MatterState()
    public data object Gas : MatterState()
}

public sealed class MatterAction : Action() {
    public object Melt : MatterAction()
    public object Freeze : MatterAction()
    public object Vaporize : MatterAction()
    public object Condense : MatterAction()
}

public sealed class MatterEffect : Effect()

public class MatterStateMachine(
    initialState: MatterState,
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
    context = context,
)
