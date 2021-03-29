# Openpay SDK for Android

![Build and Test][ci-badge] [![ktlint][ktlint-badge]][ktlint]

The Openpay SDK for Android provides frictionless integration of the Openpay experience in to your Android app with accessible custom UI components to help you help your customers to **Buy Now. Pay Smarter.**

- [Installation](#installation)
- [Features](#features)
- [Integration](#integration)
- [UI Components](#ui-components)
    - [Buttons](#buttons)
    - [Badges](#badges)
    - [Logos](#logos)
- [Contributing](#contributing)
- [License](#license)

## Installation

Download the [latest JAR][jar] from Maven central or add a Gradle dependency:

```gradle
implementation "au.com.openpay:sdk-android:0.0.1"
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snapshots].

The Openpay SDK for Android requires Android API 23+.

## Features

The SDK provides easy integration of the Openpay web checkout process and accessible badges and buttons.

## Integration

Start the Openpay checkout flow by launching the ActivityResultLauncher provided by the SDK with a given checkout URL and transaction token.

```kotlin
class ExampleFragment: Fragment() {

    private lateinit var checkoutWithOpenpay: CheckoutWithOpenpay

    override fun onCreate(savedInstanceState: Bundle) {
        // ...

        checkoutWithOpenpay = checkoutWithOpenpay {
            when (it.status) {
                SUCCESS -> TODO("Checkout completed; payment captured") 
                LODGED -> TODO("Checkout completed; payment not captured")
                CANCELLED -> TODO("Checkout cancelled by user")
                FAILURE -> TODO("Checkout failed to complete")
                REQUEST_ERROR -> TODO("Checkout failed to load")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ...

        val checkoutButton = findViewById<Button>(R.id.button_checkout)
        checkoutButton.setOnClickListener { 
            // Dispatch cart to merchant server to create plan
            val (url, token) = viewModel.checkout(cart) 
            checkoutWithOpenpay(url, token)
        }
}
```

## UI Components

The provided components offer several different color options and have been designed with accessibility in mind by adhering to recommended touch state sizes and by providing support for Android's TalkBack screen reader service.

All constraints mentioned below apply by default and must continue to do so after any customisation.

### Buttons

![Granite on Amber payment button][button-granite-amber]
![Amber on Granite payment button][button-amber-granite]

![Granite on White payment button][button-granite-white]
![White on Granite payment button][button-white-granite]

**Attributes**
```xml
app:buttonColors="graniteOnAmber|amberOnGranite|graniteOnWhite|whiteOnGranite"
app:cornerRadius="4dp"
```
**Constraints**
- Minimum width: `218dp`
- Maximum width: `380dp`
- Minimum height: `48dp`

### Badges

![Granite on Amber badge][badge-granite-amber]
![Amber on Granite badge][badge-amber-granite]

**Attributes**
```xml
app:badgeColors="graniteOnAmber|amberOnGranite"
app:cornerRadius="3dp"
```
**Constraints**
- Minimum width: `75dp`

### Logos

![Granite badge][logo-granite]
![White badge][logo-white]

**Attributes**
```xml
app:logoColors="granite|white"
```
**Constraints**
- Minimum width: `80dp`

## Contributing

All contributions are welcome! Please refer to our [contribution guide][contributing] before making a submission.

## License

	Copyright 2021 Openpay

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


<!-- Links: -->
[ci-badge]: https://github.com/openpay-innovations/sdk-android/workflows/Build%20and%20Test/badge.svg?branch=main&event=push
[ktlint]: https://ktlint.github.io
[ktlint-badge]: https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg
[jar]: https://search.maven.org/classic/remote_content?g=au.com.openpay.sdkandroid&a=openpay&v=LATEST
[snapshots]: https://oss.sonatype.org/content/repositories/snapshots/au/com/openpay/sdk-android/
[button-granite-amber]: images/button_granite_on_amber.png
[button-amber-granite]: images/button_amber_on_granite.png
[button-granite-white]: images/button_granite_on_white.png
[button-white-granite]: images/button_white_on_granite.png
[badge-granite-amber]: images/badge_granite_on_amber.png
[badge-amber-granite]: images/badge_amber_on_granite.png
[logo-granite]: images/logo_granite.png
[logo-white]: images/logo_white.png
[contributing]: CONTRIBUTING.md
