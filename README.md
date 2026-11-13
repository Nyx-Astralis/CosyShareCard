<div align="center">

# CosyShareCard

**Create and share polished image cards from your Android app.**

A reusable Android library written in Java. Build a branded 16:9 PNG card with a small builder API, then share it through Android's system share chooser.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.nyx-astralis/cosysharecard?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.nyx-astralis/cosysharecard)
[![JitPack](https://jitpack.io/v/Nyx-Astralis/CosyShareCard.svg)](https://jitpack.io/#Nyx-Astralis/CosyShareCard)
[![Release](https://img.shields.io/badge/release-1.0.0-blue)](https://github.com/Nyx-Astralis/CosyShareCard/releases/tag/1.0.0)
[![Min SDK](https://img.shields.io/badge/minSdk-26-brightgreen)](https://developer.android.com/about/versions/oreo)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

---

## Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Requirements](#requirements)
- [Installation](#installation)
  - [Maven Central](#maven-central)
  - [JitPack](#jitpack)
- [Quick start](#quick-start-java)
- [Builder API](#builder-api)
- [Themes](#themes)
- [How sharing works](#how-sharing-works)
- [Design scope](#design-scope-in-version-100)
- [Important notes](#important-notes)
- [Documentation](#documentation)
- [Support](#support-the-project)
- [License](#license)

## Features

- Generate a PNG share card with a compact Java builder API.
- Add a header, title, description, side label, author name, and brand label.
- Automatically display the consuming app's launcher icon.
- Choose light, dark, or automatic theme colors.
- Adjust the background blur or disable it entirely.
- Add optional share text and a link to the Android share message.
- Share through Android's `ACTION_SEND` chooser using a `FileProvider` content URI.
- Store generated images in the app's internal cache; no public-storage permission is required.

## Screenshots

<div align="center">
  <img src="docs/images/full1.png" width="240" alt="CosyShareCard full-size preview 1" />
  <img src="docs/images/full2.png" width="240" alt="CosyShareCard full-size preview 2" />
</div>

<div align="center">
  <img src="docs/images/mini1.jpg" width="240" alt="CosyShareCard compact preview 1" />
  <img src="docs/images/mini2.jpg" width="240" alt="CosyShareCard compact preview 2" />
</div>

## Requirements

- Android 8.0 (API 26) or higher.
- An Android project compiling with Android SDK 37 or higher.
- Java 11-compatible compile options.
- Android Gradle Plugin and Gradle versions compatible with the consuming project.

CosyShareCard is implemented in Java and can also be called from Kotlin projects.

## Installation

Choose either Maven Central (recommended) or JitPack. You only need one of these dependency sources.

### Maven Central

Maven Central is the recommended installation source. Add `mavenCentral()` to your dependency repositories if it is not already present.

For projects that declare repositories in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

Add the library to your app module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.nyx-astralis:cosysharecard:1.0.0")
}
```

For Groovy Gradle (`build.gradle`):

```groovy
dependencies {
    implementation 'io.github.nyx-astralis:cosysharecard:1.0.0'
}
```

The first Maven Central release may take a little time to become available through repository mirrors and search indexes after publication. If Gradle cannot resolve version `1.0.0` yet, use the JitPack option below temporarily.

### JitPack

CosyShareCard is also available through [JitPack](https://jitpack.io/#Nyx-Astralis/CosyShareCard).

If your project centralizes dependency repositories in `settings.gradle.kts`, add JitPack inside `dependencyResolutionManagement.repositories`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the dependency to your app module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.Nyx-Astralis:CosyShareCard:1.0.0")
}
```

For Groovy Gradle (`build.gradle`):

```groovy
dependencies {
    implementation 'com.github.Nyx-Astralis:CosyShareCard:1.0.0'
}
```

Keep the other repositories already used by your project, and add each repository only once. Sync Gradle after adding the dependency.

## Quick start (Java)

Create a builder, configure the card content and theme, obtain the share intent, and launch it from an `Activity`:

```java
import android.content.Intent;
import android.widget.Toast;

import com.studio868.cosysharecard.CosyShareCard;
import com.studio868.cosysharecard.CosyShareCardBuilder;
import com.studio868.cosysharecard.CosyShareCardTheme;

import java.io.IOException;

// Call this from an Activity, for example from a button click.
private void shareDemoCard() {
    CosyShareCardBuilder builder = new CosyShareCardBuilder(this)
            .setHeader("A LITTLE UPDATE")
            .setTitle("Hello from Nyx")
            .setDescription("This card was generated using CosyShareCard.")
            .setSideText("VERSION 1.0")
            .setAuthor("Nyx Astralis")
            .setBrand("CosyShareCard")
            .setShareText("Check out my new share card!")
            .setShareLink("https://github.com/Nyx-Astralis/CosyShareCard")
            .setTheme(CosyShareCardTheme.AUTO);

    try {
        Intent shareIntent = CosyShareCard.createShareIntent(builder);
        startActivity(shareIntent);
    } catch (IOException e) {
        Toast.makeText(
                this,
                "Unable to create share card: " + e.getMessage(),
                Toast.LENGTH_LONG
        ).show();
    }
}
```

`createShareIntent()` returns an `Intent`; it does not launch the chooser by itself. Call `startActivity()` with the returned intent.

## Builder API

All text values are optional. Any text field that is not set is rendered as empty text.

| Method | Description |
| --- | --- |
| `setHeader(String)` | Small header displayed at the top of the card. |
| `setTitle(String)` | Main card title. The fixed layout displays up to three lines. |
| `setDescription(String)` | Supporting description. The fixed layout displays up to four lines. |
| `setSideText(String)` | Rotated label along the right side of the card. |
| `setAuthor(String)` | Author text in the footer. |
| `setBrand(String)` | Brand text in the footer. |
| `setShareText(String)` | Optional text added to the share message; it is not printed on the image. |
| `setShareLink(String)` | Optional link appended to the share message; it is not printed on the image. |
| `setTheme(CosyShareCardTheme)` | Selects `AUTO`, `LIGHT`, or `DARK`. |
| `setBlurRadius(float)` | Background blur radius in dp. Defaults to `28f`; use `0f` to disable blur. |

### Theme selection

```java
builder.setTheme(CosyShareCardTheme.LIGHT); // Always use the light theme.
builder.setTheme(CosyShareCardTheme.DARK);  // Always use the dark theme.
builder.setTheme(CosyShareCardTheme.AUTO);  // Follow the device/app night mode.
```

## How sharing works

1. The library inflates its bundled card layout and applies the builder values.
2. It reads the consuming app's launcher icon for the card footer.
3. It renders the card into a 16:9 PNG bitmap.
4. It writes the PNG to the app's internal cache directory at `cache/shared_images/`.
5. It creates a read-granted `content://` URI through the library's merged `FileProvider` declaration and returns an Android share-chooser intent.

The library manifest supplies a provider authority based on the consuming app's application ID. Android's manifest merger adds the provider automatically in a normal Android Gradle project, so you should not need to declare another provider yourself.

Generated images are stored in the app cache, not the user's public gallery. Android may remove cached files when storage space is needed. The library does not require public or external storage permission.

## Design scope in version 1.0.0

Version 1.0.0 exposes text, theme, and blur configuration. The background artwork and card layout are bundled resources. This version does not provide a public API for supplying a custom background image, changing individual colors, or rearranging the card layout. The app icon is obtained automatically from the consuming application's launcher icon.

## Important notes

- Call `createShareIntent()` on the Android main/UI thread because rendering uses Android Views.
- Handle the declared `IOException`, which can occur if the cache directory cannot be created or the PNG cannot be written.
- A null context, null builder, null theme, or negative blur radius is invalid and can cause `IllegalArgumentException`.
- Use `setBlurRadius(0f)` to disable backdrop blur. Negative blur values are rejected.
- `setShareText()` and `setShareLink()` contribute to `Intent.EXTRA_TEXT`; they do not appear in the rendered PNG.

## Documentation

See **[Usage guide](docs/USAGE.md)** for a step-by-step integration walkthrough, API reference, sharing behavior, and troubleshooting notes.

## Support the project

Enjoying **CosyShareCard**? Your support helps maintain the library, develop new features, and create more open-source tools. Every contribution is greatly appreciated.

<div align="center">
  <a href="https://ko-fi.com/nyx_astralis">
    <img src="https://ko-fi.com/img/githubbutton_sm.svg" alt="Support Nyx Astralis on Ko-fi" />
  </a>
  <p>Thank you for supporting independent development! ❤️</p>
</div>

## License

The original CosyShareCard library code is licensed under the MIT License. See [LICENSE](LICENSE).

The library also bundles third-party typefaces and depends on Android libraries with their own licenses. See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md). The MIT license does not replace or override those third-party terms.

## Project links

- **Source code:** [GitHub repository](https://github.com/Nyx-Astralis/CosyShareCard)
- **Maven Central:** [CosyShareCard artifact](https://central.sonatype.com/artifact/io.github.nyx-astralis/cosysharecard)
- **JitPack:** [CosyShareCard on JitPack](https://jitpack.io/#Nyx-Astralis/CosyShareCard)
- **Issues and feature requests:** [GitHub Issues](https://github.com/Nyx-Astralis/CosyShareCard/issues)
- **Releases:** [GitHub Releases](https://github.com/Nyx-Astralis/CosyShareCard/releases)
