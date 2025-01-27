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

/**
 * Handles **unhandled** exception that occur during a state transition or handling an effect.
 */
public fun interface FallbackHandle<S, A, E> where S : State, A : Action, E : Effect {
    /**
     * If [action] is null, it indicates the exception was thrown during effect handling.
     * If [effect] is null, it indicates the exception was thrown during action handling.
     *
     * @param state The state before the transition.
     * @param action The action that was dispatched.
     * @param effect The effect that was triggered.
     * @param exception The exception that was thrown.
     * @return The action to dispatch.
     */
    public operator fun invoke(state: S?, action: A?, effect: E?, exception: Throwable): A
}
