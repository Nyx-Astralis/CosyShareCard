# CosyShareCard Usage Guide

This guide covers integrating CosyShareCard 1.0.0 into an Android application and using its public Java API.

## 1. Add the repository and dependency

In the project's `settings.gradle.kts`, add JitPack inside the existing dependency repositories:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Do not remove repositories your project already needs. Then add the library to `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.Nyx-Astralis:CosyShareCard:1.0.0")
}
```

Sync the project.

## 2. Create a share card

The public entry point is `CosyShareCard.createShareIntent(CosyShareCardBuilder)`. The builder takes an Android `Context` and provides chainable setters.

```java
private void shareCard() {
    CosyShareCardBuilder builder = new CosyShareCardBuilder(this)
        .setHeader("ANNOUNCEMENT")
        .setTitle("Something worth sharing")
        .setDescription("Add a short description of the update here.")
        .setSideText("NEW RELEASE")
        .setAuthor("Nyx Astralis")
        .setBrand("MY APP")
        .setShareText("Take a look at this update.")
        .setShareLink("https://example.com")
        .setTheme(CosyShareCardTheme.AUTO)
        .setBlurRadius(28f);

    try {
        startActivity(CosyShareCard.createShareIntent(builder));
    } catch (IOException e) {
        // Show an error to the user or log it for diagnosis.
        Toast.makeText(this, "Could not create the card.", Toast.LENGTH_LONG).show();
    }
}
```

Use the Activity context when creating the builder from an Activity. The library also reads the consuming app's launcher icon for the card footer.

## 3. Select the theme

`CosyShareCardTheme` has three values:

- `AUTO`: follows the current UI night-mode setting.
- `LIGHT`: always renders the light palette.
- `DARK`: always renders the dark palette.

```java
builder.setTheme(CosyShareCardTheme.DARK);
```

Do not pass `null` as the theme.

## 4. Configure backdrop blur

The default blur radius is `28f` dp. Reduce it for a softer blur or pass zero to leave the bundled backdrop unblurred:

```java
builder.setBlurRadius(12f);
builder.setBlurRadius(0f); // Disable blur
```

Negative values are rejected.

## 5. Understand the generated content

The card image is rendered at a 16:9 aspect ratio. The app's launcher icon is inserted automatically. The text fields map to these areas:

| Builder field | Rendered location |
| --- | --- |
| `header` | Top header |
| `title` | Main title |
| `description` | Main body copy |
| `sideText` | Rotated right-side label |
| `author` | Footer, next to the app icon |
| `brand` | Footer, aligned to the right |

The fixed layout allows up to three lines for the title and four lines for the description; longer text is ellipsized. Keep those fields concise for the best result.

`shareText` and `shareLink` are not visible in the image. If either is non-empty, the library appends it to the outgoing share message. When both are supplied, the message is separated by a blank line.

## 6. File sharing and manifest integration

The library saves the generated PNG in the app's internal cache under `shared_images/` and creates a `content://` URI through AndroidX `FileProvider`. The library's own manifest contributes the provider declaration and file-path resource. The authority is `${applicationId}.cosysharecard.fileprovider`.

In a standard Gradle Android library dependency, manifest merging handles this for the app. Avoid adding a duplicate provider with the same authority. No public storage permission is required.

The resulting intent is an `ACTION_SEND` intent with MIME type `image/png`, wrapped in a chooser. The library returns the chooser intent; the host Activity is responsible for launching it.

## 7. Threading and errors

Call `createShareIntent()` on the main/UI thread. The implementation inflates, measures, and draws Android Views while generating the PNG.

The method declares `IOException`. Handle it in the caller. Invalid inputs can also cause `IllegalArgumentException`, including a null builder/context/theme or a negative blur radius.

## 8. Troubleshooting

### Gradle cannot resolve the dependency

- Check that `maven { url = uri("https://jitpack.io") }` is inside the repository block used by dependency resolution.
- Confirm the dependency coordinate is exactly `com.github.Nyx-Astralis:CosyShareCard:1.0.0`.
- Sync Gradle again and inspect the JitPack build page for the `1.0.0` tag.

### Sharing fails while creating the image

- Make sure the app has enough internal cache space.
- Catch and inspect the `IOException` from `createShareIntent()`.
- Do not replace the library's provider authority with a hard-coded package name.

### The app icon does not appear as expected

The icon is read from the consuming application's launcher icon. Check the app's launcher icon resources and manifest configuration.

## 9. Public API reference

### `CosyShareCard`

```java
public static Intent createShareIntent(CosyShareCardBuilder builder) throws IOException
```

Creates the PNG and returns an Android chooser `Intent` for sharing it.

### `CosyShareCardBuilder`

```java
public CosyShareCardBuilder(Context context)
public CosyShareCardBuilder setHeader(String header)
public CosyShareCardBuilder setTitle(String title)
public CosyShareCardBuilder setDescription(String description)
public CosyShareCardBuilder setSideText(String sideText)
public CosyShareCardBuilder setAuthor(String author)
public CosyShareCardBuilder setBrand(String brand)
public CosyShareCardBuilder setShareText(String shareText)
public CosyShareCardBuilder setShareLink(String shareLink)
public CosyShareCardBuilder setBlurRadius(float blurRadius)
public CosyShareCardBuilder setTheme(CosyShareCardTheme theme)
```

### `CosyShareCardTheme`

```java
public enum CosyShareCardTheme {
    AUTO,
    LIGHT,
    DARK
}
```
