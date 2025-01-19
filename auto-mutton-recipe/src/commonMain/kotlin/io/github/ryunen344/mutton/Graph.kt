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

internal typealias Actions<S, A, E> = LinkedHashMap<KClass<S>, Transitions<S, A, E>>
internal typealias Transitions<S, A, E> = LinkedHashMap<KClass<A>, (prev: S, action: A) -> Transition<S, E>?>

public class Graph<S, A, E>(
    public val definitions: Map<KClass<S>, Map<KClass<A>, (prev: S, action: A) -> Transition<S, E>?>>,
) where S : State, A : Action, E : Effect

public fun <S, A, E> Graph(
    builder: GraphBuilder<S, A, E>.() -> Unit,
): Graph<S, A, E> where S : State, A : Action, E : Effect {
    return GraphBuilder<S, A, E>().apply(builder).build()
}

public class GraphBuilder<S, A, E> where S : State, A : Action, E : Effect {

    public val actions: Actions<S, A, E> = linkedMapOf()

    public inline fun <reified S1 : S> state(noinline block: ActionBuilder<S1>.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        val stateKey = S1::class as KClass<S>
        actions[stateKey] = ActionBuilder<S1>().apply(block).transitions
    }

    public fun build(): Graph<S, A, E> {
        return Graph(actions)
    }

    public inner class ActionBuilder<S1> where S1 : S {

        public val transitions: Transitions<S, A, E> = linkedMapOf()

        public inline fun <reified A1 : A> action(noinline block: TransitionBuilder<S>.(prev: S1, action: A1) -> Transition<S, E>) {
            @Suppress("UNCHECKED_CAST")
            val actionKey = A1::class as KClass<A>
            val invoker: (S1, A1) -> Transition<S, E>? =
                { prev, action -> TransitionBuilder<S>().apply { block(prev, action) }.build() }

            @Suppress("UNCHECKED_CAST")
            transitions.put(actionKey, invoker as (S, A) -> Transition<S, E>?)
        }
    }

    public inner class TransitionBuilder<S1> where S1 : S {

        private var transition: Transition<S, E>? = null

        public fun transition(state: S, effect: E? = null): Transition<S, E> {
            return Transition(state, effect).also {
                transition = it
            }
        }

        public fun build(): Transition<S, E>? {
            return transition
        }
    }
}
