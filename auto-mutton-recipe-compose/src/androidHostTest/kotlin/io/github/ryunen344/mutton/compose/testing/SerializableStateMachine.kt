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

package io.github.ryunen344.mutton.compose.testing

import android.os.Parcel
import android.os.Parcelable
import androidx.savedstate.serialization.serializers.JavaSerializableSerializer
import androidx.savedstate.serialization.serializers.ParcelableSerializer
import io.github.ryunen344.mutton.Action
import io.github.ryunen344.mutton.EffectHandle
import io.github.ryunen344.mutton.Graph
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.StateMachine
import kotlinx.serialization.Serializable
import kotlin.coroutines.CoroutineContext

private class IdleJavaSerializableSerializer : JavaSerializableSerializer<SerializableState.Idle>()
private class RunningParcelableSerializer : ParcelableSerializer<SerializableState.Running>()

@Serializable
sealed class SerializableState : State() {
    abstract val launchedCount: Int

    @Serializable(with = IdleJavaSerializableSerializer::class)
    data class Idle(override val launchedCount: Int) : SerializableState(), java.io.Serializable

    @Serializable(with = RunningParcelableSerializer::class)
    data class Running(override val launchedCount: Int) : SerializableState(), Parcelable {
        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(launchedCount)
        }

        companion object CREATOR : Parcelable.Creator<Running> {
            override fun createFromParcel(parcel: Parcel): Running {
                return Running(parcel.readInt())
            }

            override fun newArray(size: Int): Array<Running?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Stopped(override val launchedCount: Int) : SerializableState()
}

sealed class SerializableAction : Action() {
    data object Start : SerializableAction()
    data object Stop : SerializableAction()
    data object Clean : SerializableAction()
}

class SerializableStateMachine(
    initialState: SerializableState,
    context: CoroutineContext,
) : StateMachine<SerializableState, SerializableAction, ComposeEffect>(
    initialState = initialState,
    graph = Graph<SerializableState, SerializableAction, ComposeEffect> {
        state<SerializableState.Idle> {
            action<SerializableAction.Start> { prev, _ ->
                transition(SerializableState.Running(launchedCount = prev.launchedCount + 1))
            }
        }
        state<SerializableState.Running> {
            action<SerializableAction.Stop> { prev, _ ->
                transition(SerializableState.Stopped(launchedCount = prev.launchedCount))
            }
        }
        state<SerializableState.Stopped> {
            action<SerializableAction.Clean> { prev, _ ->
                transition(SerializableState.Idle(launchedCount = prev.launchedCount))
            }
        }
    },
    effectHandle = EffectHandle<SerializableState, SerializableAction, ComposeEffect> { _, _, _, _ ->
        // noop
    },
    context = context,
)
