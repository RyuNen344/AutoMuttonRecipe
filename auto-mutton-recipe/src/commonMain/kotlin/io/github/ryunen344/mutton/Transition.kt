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

public class Transition<S, E> internal constructor(
    public val next: S,
    public val effect: E?,
) where S : State, E : Effect {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Transition<*, *>

        if (next != other.next) return false
        if (effect != other.effect) return false

        return true
    }

    override fun hashCode(): Int {
        var result = next.hashCode()
        result = 31 * result + (effect?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "Transition(next=$next, effect=$effect)"
}
