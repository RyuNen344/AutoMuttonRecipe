# Changelog

## [1.3.0](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.2.1...1.3.0) (2025-05-18)

- **Breaking Change:** JVM compatibility has been changed from 1.8 to 11

### Features

* **compose:** implement compose support for multiple platforms ([#65](https://github.com/RyuNen344/AutoMuttonRecipe/issues/65)) ([a7c41a7](https://github.com/RyuNen344/AutoMuttonRecipe/commit/a7c41a74c31692b88f0b632128c116add11deab5))
* **savedstate:** saved state supports multiple platforms ([#63](https://github.com/RyuNen344/AutoMuttonRecipe/issues/63)) ([6819309](https://github.com/RyuNen344/AutoMuttonRecipe/commit/68193095322e4037c6f62ad6cc72bb593f82f66d))


### Bug Fixes

* update artifact source elements configuration ([#66](https://github.com/RyuNen344/AutoMuttonRecipe/issues/66)) ([54396d9](https://github.com/RyuNen344/AutoMuttonRecipe/commit/54396d9382f782f6ece207fc951f6db6d09e3b8c))

## [1.2.1](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.2.0...1.2.1) (2025-05-14)


### Dependency Updates

* **gradle:** bump com.vanniktech.maven.publish from 0.31.0 to 0.32.0 ([#60](https://github.com/RyuNen344/AutoMuttonRecipe/issues/60)) ([1e3cfcc](https://github.com/RyuNen344/AutoMuttonRecipe/commit/1e3cfcc9ce5ffd57fbdfa7ef612f9161fe1dfcb7))
* **gradle:** bump kotlin from 2.1.20 to 2.1.21 ([#59](https://github.com/RyuNen344/AutoMuttonRecipe/issues/59)) ([aceb4b2](https://github.com/RyuNen344/AutoMuttonRecipe/commit/aceb4b20b25a49f7e2efcb6be857d42ee2d125c1))


### Chores

* update tag filter ([#61](https://github.com/RyuNen344/AutoMuttonRecipe/issues/61)) ([09ae2cc](https://github.com/RyuNen344/AutoMuttonRecipe/commit/09ae2cc3e3b974cedea23f730a4f86fc7db6b0f0))

## [1.2.0](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.1.1...1.2.0) (2025-05-08)

### **New: SavedStateMachine supports updates via SavedStateHandle**

- The `SavedStateMachine` now supports updating the state via `SavedStateHandle`. 
- This allows you to use `StateMachine#dispatch` or `SavedStateHandle` to update the state.

```kotlin
class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    
    val stateMachine = ExampleSavedStateMachine(
        savedStateHandle = savedStateHandle,
        key = "example_key",
        initialState = ExampleState.Idle,
    )
    
    fun dispatch(action: ExampleAction) {
        // You can update state through StateMachine#dispatch
        stateMachine.dispatch(action)

        // **New**
        // or update state via SavedStateHandle
        savedStateHandle["example_key"] = action
    }
}
```


### Dependency Updates

* **gradle:** bump androidx-compose-ui from 1.8.0 to 1.8.1 ([#53](https://github.com/RyuNen344/AutoMuttonRecipe/issues/53)) ([eca7228](https://github.com/RyuNen344/AutoMuttonRecipe/commit/eca72284ac899a4cc2f3c83e892751bd6a6411f1))
* **gradle:** bump androidx-lifecycle from 2.8.7 to 2.9.0 ([#55](https://github.com/RyuNen344/AutoMuttonRecipe/issues/55)) ([5b1a50f](https://github.com/RyuNen344/AutoMuttonRecipe/commit/5b1a50f5cfd5041d655b3a12e259cdc76857dcd2))
* **gradle:** bump androidx.compose.runtime:runtime-saveable from 1.8.0 to 1.8.1 ([#54](https://github.com/RyuNen344/AutoMuttonRecipe/issues/54)) ([2b85a38](https://github.com/RyuNen344/AutoMuttonRecipe/commit/2b85a38dc0df438d8c169b24c1061407c2e38582))
* **gradle:** bump com.android.library from 8.9.2 to 8.10.0 ([#52](https://github.com/RyuNen344/AutoMuttonRecipe/issues/52)) ([0feebe6](https://github.com/RyuNen344/AutoMuttonRecipe/commit/0feebe69b292fba2139500df72110f01506f4c9d))


### Chores

* release 1.2.0 ([#57](https://github.com/RyuNen344/AutoMuttonRecipe/issues/57)) ([6d58a6f](https://github.com/RyuNen344/AutoMuttonRecipe/commit/6d58a6f52c17195f70f524b14ba0f8542712ce1c))
* update Java code style settings and enable trailing commas ([#58](https://github.com/RyuNen344/AutoMuttonRecipe/issues/58)) ([60fecd9](https://github.com/RyuNen344/AutoMuttonRecipe/commit/60fecd98f8bc97feabd41bcb9df665a2a84c1630))

## [1.1.1](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.1.0...1.1.1) (2025-04-24)


### Bug Fixes

* **state machine:** ensure state transition yields correctly ([#45](https://github.com/RyuNen344/AutoMuttonRecipe/issues/45)) ([7718519](https://github.com/RyuNen344/AutoMuttonRecipe/commit/7718519ef0ac18001ea251215cca6ce33469a04a))


### Dependency Updates

* **gradle:** bump androidx-compose-ui from 1.7.8 to 1.8.0 ([#51](https://github.com/RyuNen344/AutoMuttonRecipe/issues/51)) ([3f022c9](https://github.com/RyuNen344/AutoMuttonRecipe/commit/3f022c9d531f0985ff35c0cfcec634a65998b50e))
* **gradle:** bump androidx.compose.runtime:runtime-saveable from 1.7.8 to 1.8.0 ([#50](https://github.com/RyuNen344/AutoMuttonRecipe/issues/50)) ([3f1c6e2](https://github.com/RyuNen344/AutoMuttonRecipe/commit/3f1c6e2323b6fb8eafbfe23e6bc048835874734b))
* **gradle:** bump com.android.library from 8.9.1 to 8.9.2 ([#49](https://github.com/RyuNen344/AutoMuttonRecipe/issues/49)) ([0dd4ec7](https://github.com/RyuNen344/AutoMuttonRecipe/commit/0dd4ec7ee666fbe6fd06cb3869a1f916a3f22f4f))
* **gradle:** bump coroutines from 1.10.1 to 1.10.2 ([#46](https://github.com/RyuNen344/AutoMuttonRecipe/issues/46)) ([35409a2](https://github.com/RyuNen344/AutoMuttonRecipe/commit/35409a2aa2d252a296326e847ece9a7231b6906d))
* **gradle:** bump nl.littlerobots.version-catalog-update from 0.8.5 to 1.0.0 ([#43](https://github.com/RyuNen344/AutoMuttonRecipe/issues/43)) ([e9b3d8d](https://github.com/RyuNen344/AutoMuttonRecipe/commit/e9b3d8d52d3f6b7cb72f0d2eda7d698f3330dd62))
* **gradle:** bump org.jetbrains.kotlinx:kotlinx-serialization-core from 1.8.0 to 1.8.1 ([#44](https://github.com/RyuNen344/AutoMuttonRecipe/issues/44)) ([43bc20c](https://github.com/RyuNen344/AutoMuttonRecipe/commit/43bc20c289d8f1c2b044f4bbc940c8ec83d37995))


### Chores

* **deps:** bump gradle wrapper to 8.13 ([#42](https://github.com/RyuNen344/AutoMuttonRecipe/issues/42)) ([c0f5c05](https://github.com/RyuNen344/AutoMuttonRecipe/commit/c0f5c05e6c5a4d2271ccfc16daa6f326234b991c))
* improve layout api ([#47](https://github.com/RyuNen344/AutoMuttonRecipe/issues/47)) ([39b230c](https://github.com/RyuNen344/AutoMuttonRecipe/commit/39b230c4da77ad54fad1f781fd319f4645576643))
* indent ([#48](https://github.com/RyuNen344/AutoMuttonRecipe/issues/48)) ([713b808](https://github.com/RyuNen344/AutoMuttonRecipe/commit/713b8082c6c883f03dee1f2a34b8d2ed2438d10e))
* update version tag patterns in pages.yml ([#40](https://github.com/RyuNen344/AutoMuttonRecipe/issues/40)) ([d40b319](https://github.com/RyuNen344/AutoMuttonRecipe/commit/d40b3198c428a5f6ebcb19e2f59fd7b95ab49c10))

## [1.1.0](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.0.2...1.1.0) (2025-03-30)

### **New: Jetpack Compose Support**

#### How to Use
To use the Jetpack Compose support, you need to add the following dependency to your `build.gradle` file:

```groovy
dependencies {
    implementation "io.github.ryunen344.mutton:auto-mutton-recipe-compose:1.1.0"
}
```

#### Example Usage
```kotlin
@Composable
fun Example() {
    val coroutineScope = rememberCoroutineScope()
    val stateMachine = rememberStateMachine<ExampleStateMachine, ExampleState>(initialState = ExampleState.Idle) { state ->
        ExampleStateMachine(initialState = state, context = coroutineScope.coroutineContext)
    }
    val state by stateMachine.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        stateMachine.dispatch(ExampleAction.Start)
    }
}
```

### Features

* **compose:** compose support ([#36](https://github.com/RyuNen344/AutoMuttonRecipe/issues/36)) ([9bd696e](https://github.com/RyuNen344/AutoMuttonRecipe/commit/9bd696ed195548b78083ff2f8d78e6033a7c0261))


### Dependency Updates

* **gradle:** bump com.android.library from 8.9.0 to 8.9.1 ([#34](https://github.com/RyuNen344/AutoMuttonRecipe/issues/34)) ([e6300af](https://github.com/RyuNen344/AutoMuttonRecipe/commit/e6300affed3546b6db632b4603718b4a2c0646be))


### Chores

* **actions:** simplify actions ([#37](https://github.com/RyuNen344/AutoMuttonRecipe/issues/37)) ([ee22946](https://github.com/RyuNen344/AutoMuttonRecipe/commit/ee229462856a5c6e0787eb932fd6946f863c438d))
* **agp:** bump compileSdk version from 34 to 35 ([#38](https://github.com/RyuNen344/AutoMuttonRecipe/issues/38)) ([efc76fb](https://github.com/RyuNen344/AutoMuttonRecipe/commit/efc76fb70ea122acb7d9d732dd2adab664665d6d))

## [1.0.2](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.0.1...1.0.2) (2025-03-21)


### Dependency Updates

* **github-actions:** bump dorny/test-reporter from 1 to 2 ([#32](https://github.com/RyuNen344/AutoMuttonRecipe/issues/32)) ([d052556](https://github.com/RyuNen344/AutoMuttonRecipe/commit/d052556c15f80fa172e00a4cbc2f40546a3d00f5))
* **gradle:** bump com.android.library from 8.7.3 to 8.8.0 ([#21](https://github.com/RyuNen344/AutoMuttonRecipe/issues/21)) ([86751a2](https://github.com/RyuNen344/AutoMuttonRecipe/commit/86751a230930b5fe6ac0e37c68f160f0ab2fdec3))
* **gradle:** bump com.android.library from 8.8.0 to 8.8.1 ([#26](https://github.com/RyuNen344/AutoMuttonRecipe/issues/26)) ([edd7fec](https://github.com/RyuNen344/AutoMuttonRecipe/commit/edd7fec04686717cc2fa1cfd82998728ac2027dc))
* **gradle:** bump com.android.library from 8.8.1 to 8.8.2 ([#28](https://github.com/RyuNen344/AutoMuttonRecipe/issues/28)) ([03539fa](https://github.com/RyuNen344/AutoMuttonRecipe/commit/03539fa71fbfc7dfabad70fd2780b512c13866ad))
* **gradle:** bump com.android.library from 8.8.2 to 8.9.0 ([#29](https://github.com/RyuNen344/AutoMuttonRecipe/issues/29)) ([b2826de](https://github.com/RyuNen344/AutoMuttonRecipe/commit/b2826de441bd35017a9085c809cb093ebdb43c52))
* **gradle:** bump com.vanniktech.maven.publish from 0.30.0 to 0.31.0 ([#30](https://github.com/RyuNen344/AutoMuttonRecipe/issues/30)) ([03751cb](https://github.com/RyuNen344/AutoMuttonRecipe/commit/03751cbfd853839cdc833e8ebb198ad6bf52a28d))
* **gradle:** bump detekt from 1.23.7 to 1.23.8 ([#27](https://github.com/RyuNen344/AutoMuttonRecipe/issues/27)) ([d65686f](https://github.com/RyuNen344/AutoMuttonRecipe/commit/d65686f8f8f89078f721909e881cbcfab037af75))
* **gradle:** bump kotlin from 2.1.0 to 2.1.10 ([#19](https://github.com/RyuNen344/AutoMuttonRecipe/issues/19)) ([ae2fb47](https://github.com/RyuNen344/AutoMuttonRecipe/commit/ae2fb47fc77cb52c3487d25b17fef1b6aee6ace6))
* **gradle:** bump kotlin from 2.1.10 to 2.1.20 ([#33](https://github.com/RyuNen344/AutoMuttonRecipe/issues/33)) ([1c6f567](https://github.com/RyuNen344/AutoMuttonRecipe/commit/1c6f567bf0f34bc53795f6cc72f5201ff4d71d4f))


### Chores

* typo ([#31](https://github.com/RyuNen344/AutoMuttonRecipe/issues/31)) ([bf84ad9](https://github.com/RyuNen344/AutoMuttonRecipe/commit/bf84ad9bcafaa06bf83df7c9338640e5e8a615f0))

## [1.0.1](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.0.0...1.0.1) (2025-02-03)


### Features

* create gradle module for android extension ([#17](https://github.com/RyuNen344/AutoMuttonRecipe/issues/17)) ([690731a](https://github.com/RyuNen344/AutoMuttonRecipe/commit/690731ac35ea0f9623e8d72602b9f2d8c419b0a1))
* implement SavedStateMachine with SavedStateHandle support ([#23](https://github.com/RyuNen344/AutoMuttonRecipe/issues/23)) ([ecc5d02](https://github.com/RyuNen344/AutoMuttonRecipe/commit/ecc5d0202332df834b74e4f919cb65f320ec5cae))

A new `auto-mutton-recipe-savedstate` artifact has been created that focuses on supporting apps that use `SavedStateHandle` for state management.

### Chores

* release 1.0.1 ([#22](https://github.com/RyuNen344/AutoMuttonRecipe/issues/22)) ([3aeec98](https://github.com/RyuNen344/AutoMuttonRecipe/commit/3aeec984801107d08438beca136d5534d72d67fb))
* release 1.1.0 ([#18](https://github.com/RyuNen344/AutoMuttonRecipe/issues/18)) ([4434a94](https://github.com/RyuNen344/AutoMuttonRecipe/commit/4434a94ab507e3ae386ff2c914bebe971a1e3b77))
* update concurrency group names in workflow files ([#15](https://github.com/RyuNen344/AutoMuttonRecipe/issues/15)) ([7eaa077](https://github.com/RyuNen344/AutoMuttonRecipe/commit/7eaa0770357433ba9abb2769b1805e15144befa9))
* update path for Dokka HTML output in GitHub Actions workflow ([#20](https://github.com/RyuNen344/AutoMuttonRecipe/issues/20)) ([addf66e](https://github.com/RyuNen344/AutoMuttonRecipe/commit/addf66ef79d4fa6427cc9db42a9ea635c54c5cf0))

## [1.0.0](https://github.com/RyuNen344/AutoMuttonRecipe/compare/1.0.0-alpha01...1.0.0) (2025-01-29)


### Chores

* add Dependabot configuration for Gradle and GitHub Actions updates ([#12](https://github.com/RyuNen344/AutoMuttonRecipe/issues/12)) ([d0b5890](https://github.com/RyuNen344/AutoMuttonRecipe/commit/d0b5890d2f5cb25431115974c8ea6f1b44c979e1))
* add GitHub workflow for Gradle dependency report ([#11](https://github.com/RyuNen344/AutoMuttonRecipe/issues/11)) ([d674251](https://github.com/RyuNen344/AutoMuttonRecipe/commit/d6742516445ce679c8e72f56a1b2046594e2250b))
* release 1.0.0 ([#9](https://github.com/RyuNen344/AutoMuttonRecipe/issues/9)) ([13458e2](https://github.com/RyuNen344/AutoMuttonRecipe/commit/13458e2a49d000f8862eadf2e184c12570d7c8d1))
* setup konan cache ([#10](https://github.com/RyuNen344/AutoMuttonRecipe/issues/10)) ([48f0b6b](https://github.com/RyuNen344/AutoMuttonRecipe/commit/48f0b6bceef522046930ca72eff17cd469de458c))
* update concurrency cancellation condition in workflow ([#13](https://github.com/RyuNen344/AutoMuttonRecipe/issues/13)) ([86be1f4](https://github.com/RyuNen344/AutoMuttonRecipe/commit/86be1f489833334646c0f0d963816070880abe9e))
* update Gradle wrapper to version 8.12.1 ([#8](https://github.com/RyuNen344/AutoMuttonRecipe/issues/8)) ([4ae237e](https://github.com/RyuNen344/AutoMuttonRecipe/commit/4ae237e5dfeed52890d464ae2dbcb7413876f6d4))
* update test reporter name format in workflow ([#6](https://github.com/RyuNen344/AutoMuttonRecipe/issues/6)) ([c893828](https://github.com/RyuNen344/AutoMuttonRecipe/commit/c893828a5cd67705ba2a0d0f97cae7e5cfb3be4c))

## [1.0.0-alpha01](https://github.com/RyuNen344/AutoMuttonRecipe/compare/v1.0.0-alpha01...1.0.0-alpha01) (2025-01-29)


### Chores

* release 1.0.0 ([95f8ff2](https://github.com/RyuNen344/AutoMuttonRecipe/commit/95f8ff2ee8467429d52bce3e38c71436bcd70253))
* release 1.0.0-alpha01 ([7b9d2db](https://github.com/RyuNen344/AutoMuttonRecipe/commit/7b9d2db292ea2fc36da519e46d46f3eb14f35d6a))
* update version file ([1e2ce3f](https://github.com/RyuNen344/AutoMuttonRecipe/commit/1e2ce3f9a9e61b719fbea3dac5013746e5babbf1))


### Tests

* add unit tests for FallbackHandle and Graph functionality ([#5](https://github.com/RyuNen344/AutoMuttonRecipe/issues/5)) ([2b75882](https://github.com/RyuNen344/AutoMuttonRecipe/commit/2b758827f064f263677a553fcea9b2d0f98e932e))
* add unit tests for ReentrantMutex functionality ([#3](https://github.com/RyuNen344/AutoMuttonRecipe/issues/3)) ([7267ead](https://github.com/RyuNen344/AutoMuttonRecipe/commit/7267ead8d3d725ed5434e8fd87e482fe9cbc1b96))
* setup pr checks ([#1](https://github.com/RyuNen344/AutoMuttonRecipe/issues/1)) ([9b53f18](https://github.com/RyuNen344/AutoMuttonRecipe/commit/9b53f18b678b6259f8098aa5965aa4f8bad1c123))
* tests basic transitions ([#4](https://github.com/RyuNen344/AutoMuttonRecipe/issues/4)) ([c592623](https://github.com/RyuNen344/AutoMuttonRecipe/commit/c59262376cf3cd4e9230cc8f4fbb11bf2de21758))
