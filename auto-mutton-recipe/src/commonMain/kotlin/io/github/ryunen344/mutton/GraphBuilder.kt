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

import io.github.ryunen344.mutton.annotation.StateMachineDslMarker
import kotlin.reflect.KClass

private typealias Edges<S, A, E> = LinkedHashMap<KClass<S>, Actions<S, A, E>>
private typealias Actions<S, A, E> = LinkedHashMap<KClass<A>, (prev: S, action: A) -> Transition<S, E>>

@StateMachineDslMarker
public class GraphBuilder<S, A, E> where S : State, A : Action, E : Effect {

    public val edges: Edges<S, A, E> = linkedMapOf()

    public inline fun <reified SubS : S> state(block: ActionBuilder<SubS>.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        val key = SubS::class as KClass<S>
        edges[key] = ActionBuilder<SubS>().apply(block).actions
    }

    public fun build(): Graph<S, A, E> {
        return Graph(edges)
    }

    @StateMachineDslMarker
    public inner class ActionBuilder<SubS> where SubS : S {

        public val actions: Actions<S, A, E> = linkedMapOf()

        public inline fun <reified SubA : A> action(noinline block: TransitionBuilder<SubS>.(prev: SubS, action: SubA) -> Transition<S, E>) {
            @Suppress("UNCHECKED_CAST")
            val key = SubA::class as KClass<A>

            @Suppress("UNCHECKED_CAST")
            val transition = { prev: SubS, action: SubA -> TransitionBuilder<SubS>().block(prev, action) } as (S, A) -> Transition<S, E>

            actions.put(key, transition)
        }

        @StateMachineDslMarker
        public inner class TransitionBuilder<SubS> where SubS : S {
            public fun transition(state: S, effect: E? = null): Transition<S, E> {
                return Transition(state, effect)
            }
        }
    }
}
