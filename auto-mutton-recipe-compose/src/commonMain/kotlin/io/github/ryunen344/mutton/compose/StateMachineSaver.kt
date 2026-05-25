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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.StateMachine

/**
 * A [Saver] for [StateMachine] that saves the current state.
 */
public fun <T : StateMachine<S, *, *>, S : State> stateMachineSaver(restore: (value: S) -> T): Saver<T, S> = Saver(
    save = {
        it.state.value
    },
    restore = restore,
)

/**
 * Remember a [StateMachine] that is remembered across compositions and configuration changes.
 *
 * @sample io.github.ryunen344.mutton.compose.samples.StateMachineSaverSample
 */
@Deprecated(
    message =
        " 'rememberStateMachine' with a custom 'key' is no longer supported. It bypasses " +
            "positional scoping, leading to state bugs and inconsistent behavior (e.g., " +
            "unintentional state sharing or loss, issues in nested LazyLayouts). Please remove the " +
            "'key' parameter to use positional scoping for consistent, locally-scoped state. " +
            "See https://r.android.com/3610053 for details.",
)
@Composable
public fun <T : StateMachine<S, *, *>, S : State> rememberStateMachine(
    vararg inputs: Any?,
    initialState: S,
    key: String? = null,
    init: (state: S) -> T,
): T {
    return rememberSaveable(
        inputs = inputs,
        saver = stateMachineSaver(init),
        key = key,
    ) {
        init(initialState)
    }
}

/**
 * Remember a [StateMachine] that is remembered across compositions and configuration changes.
 *
 * @sample io.github.ryunen344.mutton.compose.samples.StateMachineSaverSample
 */
@Composable
public fun <T : StateMachine<S, *, *>, S : State> rememberStateMachine(
    vararg inputs: Any?,
    initialState: S,
    init: (state: S) -> T,
): T {
    return rememberSaveable(
        inputs = inputs,
        saver = stateMachineSaver(init),
    ) {
        init(initialState)
    }
}
