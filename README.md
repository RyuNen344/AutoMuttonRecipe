AutoMuttonRecipe
====

**AutoMuttonRecipe** is a type-safe DSL and pure Kotlin Finite State Machine (FSM) implementation, inspired by [Tinder/StateMachine](https://github.com/Tinder/StateMachine).

## Features
### TypeSafe DSL
AutoMuttonRecipe provides a type-safe DSL for defining state transitions and effects.
If you define an invalid state transition, the compiler will raise an error, ensuring robust state management.

### Atomic State Transition
AutoMuttonRecipe uses `kotlinx.coroutines.flow.StateFlow` to manage states, guaranteeing atomic state transitions.
If actions are dispatched in unexpected states, they will be ignored, preventing unintended behavior.

### Lightweight and Supports Multiplatform
AutoMuttonRecipe is a lightweight library designed for multiplatform projects.

## Installation
### Maven
```xml
<dependency>
  <groupId>io.github.ryunen344.mutton</groupId>
  <artifactId>auto-mutton-recipe</artifactId>
  <version>$version</version>
</dependency>
<dependency>
  <groupId>io.github.ryunen344.mutton</groupId>
  <artifactId>auto-mutton-recipe-compose</artifactId>
  <version>$version</version>
</dependency>
<dependency>
  <groupId>io.github.ryunen344.mutton</groupId>
  <artifactId>auto-mutton-recipe-savedstate</artifactId>
  <version>$version</version>
</dependency>
```

### Gradle
```gradle
implementation 'io.github.ryunen344.mutton:auto-mutton-recipe:$version'
implementation 'io.github.ryunen344.mutton:auto-mutton-recipe-compose:$version'
implementation 'io.github.ryunen344.mutton:auto-mutton-recipe-savedstate:$version'
```

## How to use
### Step 1: Define Nodes
```kotlin
sealed class ExampleState : State() {
    data object Idle : ExampleState()
    data object Loading : ExampleState()
    data class OK(val data: String) : ExampleState()
    data class NG(val throwable: Throwable?) : ExampleState()
}

sealed class ExampleAction : Action() {
    data object Load : ExampleAction()
    data class Success(val data: String) : ExampleAction()
    data class Failure(val throwable: Throwable?) : ExampleAction()
    data object Reset : ExampleAction()
}

sealed class ExampleEffect : Effect() {
    data object FetchRemoteData : ExampleEffect()
}
```

### Step 2: Define Transitions with DSL
```kotlin
val exampleGraph = Graph<ExampleState, ExampleAction, ExampleEffect> {
    // when state is Idle
    state<ExampleState.Idle> {
        // given action is Load
        action<ExampleAction.Load> { prev, action ->
            // then transition to Loading state and trigger FetchRemoteData effect
            transition(ExampleState.Loading, ExampleEffect.FetchRemoteData)
        }
    }
    state<ExampleState.Loading> {
        action<ExampleAction.Success> { prev, action ->
            transition(ExampleState.OK(action.data))
        }
        action<ExampleAction.Failure> { prev, action ->
            transition(ExampleState.NG(action.throwable))
        }
    }
    state<ExampleState.NG> {
        action<ExampleAction.Reset> { prev, action ->
            transition(ExampleState.Idle)
        }
    }
}

val exampleEffectHandle = EffectHandle<ExampleState, ExampleAction, ExampleEffect> { effect, prev, next, dispatch ->
    when (effect) {
        ExampleEffect.FetchRemoteData -> {
            runCatching {
                // simulate fetching remote data
            }.onSuccess {
                dispatch(ExampleAction.Success("Remote data"))
            }.onFailure {
                if (it !is CancellationException) {
                    dispatch(ExampleAction.Failure(it))
                } else {
                    throw it
                }
            }
        }
    }
}
```

### Step 3: Create StateMachine and Collect States
```kotlin
class ExampleStateMachine(context: CoroutineContext) : StateMachine<ExampleState, ExampleAction, ExampleEffect>(
    initialState = ExampleState.Idle,
    graph = exampleGraph,
    effectHandle = exampleEffectHandle,
    context = context,
)

fun main() {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    val stateMachine = ExampleStateMachine(coroutineScope.coroutineContext)

    coroutineScope.launch {
        stateMachine.state.collect {
            // Handle state changes here
        }
    }

    // Dispatch actions
    stateMachine.dispatch(ExampleAction.Load)
}
```

> [!TIP]
> You can use `SavedStateMachine` with `auto-mutton-recipe-savedstate` module to save and restore the state machine's state.

## License
```text
Copyright (C) 2025 RyuNen344

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
```
