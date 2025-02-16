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
 * Handles the effect triggered after a state transition.
 */
public fun interface EffectHandle<S, A, E> where S : State, A : Action, E : Effect {
    /**
     * @param effect The effect that was triggered.
     * @param prev The state before the transition.
     * @param current The state after the transition.
     * @param dispatch A function to dispatch actions during effect handling.
     */
    public suspend operator fun invoke(effect: E, prev: S, current: S, dispatch: suspend (A) -> Unit)
}
