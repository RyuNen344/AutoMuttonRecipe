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

package io.github.ryunen344.mutton.testing

import io.github.ryunen344.mutton.EffectHandle
import io.github.ryunen344.mutton.FallbackHandle
import io.github.ryunen344.mutton.Graph
import io.github.ryunen344.mutton.StateMachine
import kotlin.coroutines.CoroutineContext

class SampleStateMachine(
    initialState: SampleState = SampleState.Idle(0),
    graph: Graph<SampleState, SampleAction, SampleEffect>,
    effectHandle: EffectHandle<SampleState, SampleAction, SampleEffect>,
    fallbackHandle: FallbackHandle<SampleState, SampleAction, SampleEffect>? = null,
    context: CoroutineContext,
) : StateMachine<SampleState, SampleAction, SampleEffect>(
    initialState = initialState,
    graph = graph,
    effectHandle = effectHandle,
    fallbackHandle = fallbackHandle,
    logger = TestLogger,
    context = context,
)
