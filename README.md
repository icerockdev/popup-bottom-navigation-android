[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) ![JitPack](https://jitpack.io/v/icerockdev/popup-bottom-navigation-android.svg)

# Popup Bottom Navigation for Android

![android-demo](https://github.com/icerockdev/popup-bottom-navigation-android/assets/5010169/bcc01af9-a231-4be2-b7a3-f8edfd54b318)


## Installation
### root build.gradle
```groovy
allprojects {
    repositories {
        ...
		maven { url = uri("https://jitpack.io") }
    }
}
```

### project build.gradle
```groovy
dependencies {
    implementation("com.github.icerockdev:popup-bottom-navigation-android:0.1.0")
}
```

## Usage
```kotlin
@Composable
private fun SimpleDemo() {
    BottomNavigation(modifier = Modifier.fillMaxWidth()) {
        BottomNavigationItem(
            selected = true,
            onClick = {  },
            icon = { Icon(Icons.Default.Check, contentDescription = null) }
        )
        PopupBottomNavigationItem(
            selected = false,
            icon = { Icon(Icons.Default.Check, contentDescription = null) },
            nestedItems = listOf(
                PopupNavigationItem(
                    selected = false,
                    onClick = {  },
                    content = { selected, onClick ->
                        BottomNavigationItem(
                            selected = selected,
                            onClick = onClick,
                            icon = { Icon(Icons.Default.Call, contentDescription = null) }
                        )
                    }
                ),
                PopupNavigationItem(
                    selected = false,
                    onClick = {  },
                    content = { selected, onClick ->
                        BottomNavigationItem(
                            selected = selected,
                            onClick = onClick,
                            icon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                        )
                    }
                )
            )
        )
    }
}
```

## Contributing
All development (both new features and bug fixes) is performed in the `develop` branch. This way `master` always contains the sources of the most recently released version. Please send PRs with bug fixes to the `develop` branch. Documentation fixes in the markdown files are an exception to this rule. They are updated directly in `master`.

The `develop` branch is pushed to `master` on release.

For more details on contributing please see the [contributing guide](CONTRIBUTING.md).

## License

    Copyright 2023 IceRock MAG Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
