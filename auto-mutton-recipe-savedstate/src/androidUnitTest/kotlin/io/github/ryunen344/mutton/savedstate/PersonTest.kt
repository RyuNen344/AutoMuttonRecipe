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

package io.github.ryunen344.mutton.savedstate

import androidx.savedstate.read
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import kotlinx.serialization.Serializable
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class PersonTest {
    @Serializable
    data class Component(val list: List<String>)

    @Serializable
    data class Container(val component: Component)

    @Test
    fun test() {
        val component = Component(listOf("a", "b", "c"))
        val container = Container(component)

        val encoded = encodeToSavedState(container)
        encoded.read {
            assertThat(size()).isEqualTo(1)
            getSavedState("component").read {
                assertThat(size()).isEqualTo(1)
                assertThat(getStringList("list"))
                    .isEqualTo(listOf("a", "b", "c"))
            }
        }
        val decoded = decodeFromSavedState<Container>(encoded)
        assertThat(decoded).isDataClassEqualTo(container)
    }
}
