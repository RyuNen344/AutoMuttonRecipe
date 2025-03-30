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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ryunen344.mutton.compose.rememberStateMachine

@Composable
public fun StateMachineSaverSample() {
    val coroutineScope = rememberCoroutineScope()
    val stateMachine = rememberStateMachine<MatterStateMachine, MatterState>(initialState = MatterState.Solid) { state ->
        MatterStateMachine(initialState = state, context = coroutineScope.coroutineContext)
    }

    val state by stateMachine.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        println("MatterStateMachine state: $state")
    }
}
