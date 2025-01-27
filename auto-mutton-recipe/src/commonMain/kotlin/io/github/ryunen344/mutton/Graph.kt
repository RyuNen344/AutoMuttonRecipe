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

import kotlin.reflect.KClass

/**
 * A graph that represents the state machine.
 *
 * example:
 * ```kotlin
 * val graph = Graph<State, Action, Effect> {
 *     state<InitialState> {
 *         action<InitialAction> { prev, action ->
 *            transition(NextState, NextEffect)
 *         }
 *         action<AnotherAction> { prev, action ->
 *            transition(AnotherState)
 *         }
 *     }
 * }
 * ```
 */
public class Graph<S, A, E>(
    public val edges: Map<KClass<S>, Map<KClass<A>, (prev: S, action: A) -> Transition<S, E>>>,
) where S : State, A : Action, E : Effect {
    public companion object {
        public operator fun <S, A, E> invoke(
            builder: GraphBuilder<S, A, E>.() -> Unit,
        ): Graph<S, A, E> where S : State, A : Action, E : Effect {
            return GraphBuilder<S, A, E>().apply(builder).build()
        }
    }
}
