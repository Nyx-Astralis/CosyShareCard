# CosyShareCard — Usage Guide

This guide explains how to add CosyShareCard to an Android project, configure a card, and share the generated image.

- [Before you start](#before-you-start)
- [Install the library](#install-the-library)
- [Create and share a card](#create-and-share-a-card)
- [Configure card content](#configure-card-content)
- [Choose a theme](#choose-a-theme)
- [Configure background blur](#configure-background-blur)
- [Understand the share result](#understand-the-share-result)
- [Validation and errors](#validation-and-errors)
- [Troubleshooting](#troubleshooting)

## Before you start

CosyShareCard is a Java Android library. It can be used from Java or Kotlin Android applications.

| Requirement | Minimum / value |
| --- | --- |
| Android | API 26 (Android 8.0) |
| Compile SDK | 37 or higher |
| Java compatibility | Java 11 |

Your Android Gradle Plugin and Gradle versions must be compatible with the rest of your Android project.

## Install the library

Use one dependency source. Maven Central is recommended; JitPack is available as an alternative.

### Option A: Maven Central

Make sure `mavenCentral()` is listed in your dependency repositories. For example, in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

Then add the dependency to the app module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.nyx-astralis:cosysharecard:1.0.0")
}
```

Groovy equivalent (`build.gradle`):

```groovy
dependencies {
    implementation 'io.github.nyx-astralis:cosysharecard:1.0.0'
}
```

Maven Central coordinates:

| Coordinate | Value |
| --- | --- |
| Group ID | `io.github.nyx-astralis` |
| Artifact ID | `cosysharecard` |
| Version | `1.0.0` |

The initial release can take some time to propagate after it is published. If version `1.0.0` is not yet resolving from Maven Central, use JitPack as a temporary alternative while repository mirrors update.

### Option B: JitPack

Add the JitPack repository to `dependencyResolutionManagement.repositories` in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then add the JitPack dependency to the app module:

```kotlin
dependencies {
    implementation("com.github.Nyx-Astralis:CosyShareCard:1.0.0")
}
```

Groovy equivalent:

```groovy
dependencies {
    implementation 'com.github.Nyx-Astralis:CosyShareCard:1.0.0'
}
```

Do not remove repositories your application already needs. Avoid adding the same repository more than once. After changing dependencies, sync the project with Gradle.

## Create and share a card

The main entry points are `CosyShareCardBuilder`, `CosyShareCard`, and `CosyShareCardTheme`.

Create the builder with an Activity context, set the content, request the share intent, and launch it:

```java
import android.content.Intent;
import android.widget.Toast;

import com.studio868.cosysharecard.CosyShareCard;
import com.studio868.cosysharecard.CosyShareCardBuilder;
import com.studio868.cosysharecard.CosyShareCardTheme;

import java.io.IOException;

private void shareCard() {
    CosyShareCardBuilder builder = new CosyShareCardBuilder(this)
            .setHeader("A LITTLE UPDATE")
            .setTitle("A moment worth sharing")
            .setDescription("A custom image card created inside my Android app.")
            .setSideText("MY UPDATE")
            .setAuthor("Your Name")
            .setBrand("Your App")
            .setShareText("Here's something I wanted to share.")
            .setShareLink("https://example.com")
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

Call this method from an Activity, such as a button's click listener. `createShareIntent()` returns the chooser `Intent`; your Activity launches it with `startActivity()`.

## Configure card content

All text fields are optional. Set only the values you need.

| Builder method | What it changes | Printed on image? |
| --- | --- | --- |
| `setHeader(String)` | Small heading at the top of the card. | Yes |
| `setTitle(String)` | Main title. The fixed layout displays up to three lines. | Yes |
| `setDescription(String)` | Supporting description. The fixed layout displays up to four lines. | Yes |
| `setSideText(String)` | Rotated label along the right edge. | Yes |
| `setAuthor(String)` | Author text in the footer. | Yes |
| `setBrand(String)` | Brand text in the footer. | Yes |
| `setShareText(String)` | Text included in the Android share message. | No |
| `setShareLink(String)` | Link appended to the Android share message. | No |

The consuming app's launcher icon is included automatically. There is no builder option for supplying a different icon in version 1.0.0.

## Choose a theme

Pass one of the `CosyShareCardTheme` values to `setTheme()`:

```java
builder.setTheme(CosyShareCardTheme.LIGHT);
builder.setTheme(CosyShareCardTheme.DARK);
builder.setTheme(CosyShareCardTheme.AUTO);
```

| Theme | Behavior |
| --- | --- |
| `LIGHT` | Always renders using the light theme. |
| `DARK` | Always renders using the dark theme. |
| `AUTO` | Follows the device/app night-mode setting. |

## Configure background blur

Use `setBlurRadius(float)` to change the background blur radius. The default is `28f` dp.

```java
builder.setBlurRadius(18f); // Softer blur.
builder.setBlurRadius(0f);  // Disable blur.
```

Negative values are invalid and are rejected.

## Understand the share result

When `createShareIntent()` is called, the library:

1. Inflates its bundled card layout and applies the builder content and theme.
2. Reads the consuming application's launcher icon.
3. Renders the design as a 16:9 PNG image.
4. Writes the PNG into the app's internal cache under `cache/shared_images/`.
5. Creates a read-granted content URI through the library's `FileProvider` configuration.
6. Returns an Android share chooser intent using `Intent.ACTION_SEND`.

The image is shared as a content URI, not as a public filesystem path. The library manifest includes the provider declaration and uses an authority derived from the consuming app's application ID. The Android manifest merger normally handles this automatically; do not add a duplicate provider declaration without first checking the merged manifest.

Generated images remain in the app's cache and are not automatically saved to the user's gallery. Android can remove cache files when it needs storage space. No public-storage permission is required.

## Validation and errors

- Call the rendering/share-intent method on the Android main/UI thread because it uses Android Views.
- Handle `IOException` from `createShareIntent()`. For example, writing the generated PNG can fail if the cache directory is unavailable.
- A null context, null builder, null theme, or negative blur radius is invalid and can cause `IllegalArgumentException`.
- The `setShareText()` and `setShareLink()` values are added to the share message (`Intent.EXTRA_TEXT`); they are not drawn on the PNG.

## Troubleshooting

### Gradle cannot find the dependency

- Confirm the dependency coordinates match the selected repository.
- For Maven Central, confirm `mavenCentral()` is present in the dependency repositories.
- For JitPack, confirm `maven { url = uri("https://jitpack.io") }` is present in the dependency repositories.
- Check that the version is `1.0.0`, then sync Gradle again.
- A new Maven Central release may need time to propagate. Try JitPack while Central mirrors and indexes update.

### The share chooser does not appear

`createShareIntent()` creates and returns the intent; it does not launch the chooser. Start the returned intent from your Activity:

```java
Intent shareIntent = CosyShareCard.createShareIntent(builder);
startActivity(shareIntent);
```

### The PNG is not saved in the gallery

This is expected. CosyShareCard writes the generated image to the app's internal cache for sharing. It does not save a permanent copy to the public gallery.

### The app icon in the card is not the icon I expected

The library reads the consuming application's launcher icon. Version 1.0.0 does not expose a method to provide a custom footer icon.

## Version 1.0.0 scope

The public API in version 1.0.0 supports text fields, theme selection, share-message text/link, and background blur. The card artwork and layout are bundled with the library. Custom background images, individual color overrides, layout rearrangement, and custom footer icons are not part of this version's public API.
