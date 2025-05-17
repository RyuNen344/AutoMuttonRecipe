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

package io.github.ryunen344.mutton.savedstate.testing

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import io.github.ryunen344.mutton.EffectHandle
import io.github.ryunen344.mutton.Graph
import io.github.ryunen344.mutton.State
import io.github.ryunen344.mutton.savedstate.SavedStateMachine

sealed class ParcelableState : State() {
    data class OK(val value: String) : ParcelableState(), Parcelable {

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(value)
        }

        companion object CREATOR : Parcelable.Creator<OK> {
            override fun createFromParcel(parcel: Parcel): OK {
                return requireNotNull(parcel.readString()?.let(::OK))
            }

            override fun newArray(size: Int): Array<OK?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Error(val message: String) : ParcelableState()
}

class ParcelableStateMachine(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialState: ParcelableState,
) : SavedStateMachine<ParcelableState, SavedAction, SavedEffect>(
    savedStateHandle = savedStateHandle,
    key = key,
    initialState = initialState,
    graph = Graph<ParcelableState, SavedAction, SavedEffect> { },
    effectHandle = EffectHandle<ParcelableState, SavedAction, SavedEffect> { _, _, _, _ -> },
)
