# DirectShareKit

Reusable Android document sharing library with WhatsApp-focused APIs and generic sharing helpers. Now with support for Telegram, Messenger, Gmail, Google Drive, and more.

Current release target: `1.0.0`

## What is included

- `com.csiw.directsharekit.ui.ShareManager` - Core sharing engine
- `com.csiw.directsharekit.data.FileRepository` - File operations and validation
- `com.csiw.directsharekit.domain.ShareUseCase` - Business logic layer
- `com.csiw.directsharekit.utils.*` - Utility classes (MIME types, app detection, constants)
- Built-in `FileProvider` (`${applicationId}.directshare.provider`)

## Import in this repository

`app/build.gradle.kts` already includes:

```kotlin
implementation(project(":directsharekit"))
```

## Import into another project (module dependency)

1. Copy the `directsharekit` module folder.
2. Add this in `settings.gradle.kts`:

```kotlin
include(":directsharekit")
```

3. Add this in your app module dependencies:

```kotlin
implementation(project(":directsharekit"))
```

## Usage Examples

### WhatsApp Sharing

#### Single File
```kotlin
ShareManager.shareToWhatsApp(
    context = this,
    uri = fileUri,
    mimeType = "application/pdf",
    phoneNumber = "919876543210", // required for direct chat target
    message = "Please check this file" // optional
)
```

#### Multiple Files
```kotlin
ShareManager.shareMultipleToWhatsApp(
    context = this,
    uris = selectedUris,
    phoneNumber = "919876543210", // optional
    message = "Please check these files" // optional
)
```

### Telegram Sharing

#### Single File
```kotlin
ShareManager.shareToTelegram(
    context = this,
    uri = fileUri,
    mimeType = "image/jpeg",
    message = "Check this out!" // optional
)
```

#### Multiple Files
```kotlin
ShareManager.shareMultipleToTelegram(
    context = this,
    uris = selectedUris,
    message = "Multiple files" // optional
)
```

### Messenger Sharing

#### Single File
```kotlin
ShareManager.shareToMessenger(
    context = this,
    uri = fileUri,
    mimeType = "image/png"
)
```

#### Multiple Files
```kotlin
ShareManager.shareMultipleToMessenger(
    context = this,
    uris = selectedUris
)
```

### Gmail Sharing

#### Single File
```kotlin
ShareManager.shareViaGmail(
    context = this,
    uri = fileUri,
    recipient = "user@example.com",
    subject = "Document",
    body = "Check this file"
)
```

#### Multiple Files
```kotlin
ShareManager.shareMultipleViaGmail(
    context = this,
    uris = selectedUris,
    recipient = "user@example.com",
    subject = "Documents",
    body = "Multiple files attached"
)
```

### Google Drive Sharing

#### Single File
```kotlin
ShareManager.shareViaDrive(
    context = this,
    uri = fileUri
)
```

#### Multiple Files
```kotlin
ShareManager.shareMultipleViaDrive(
    context = this,
    uris = selectedUris
)
```

### Generic Sharing (Share Chooser)

#### Single File
```kotlin
ShareManager.shareGeneric(
    context = this,
    uri = fileUri,
    mimeType = "application/pdf",
    title = "Share PDF"
)
```

#### Multiple Files
```kotlin
ShareManager.shareMultiple(
    context = this,
    uris = selectedUris,
    title = "Share Files"
)
```

## App Detection

Check if apps are installed before sharing:

```kotlin
if (ShareManager.isWhatsAppInstalled(this)) {
    // Show WhatsApp sharing option
}

if (ShareManager.isTelegramInstalled(this)) {
    // Show Telegram sharing option
}

if (ShareManager.isGmailInstalled(this)) {
    // Show Gmail sharing option
}

if (ShareManager.isDriveInstalled(this)) {
    // Show Drive sharing option
}

if (ShareManager.isMessengerInstalled(this)) {
    // Show Messenger sharing option
}
```

## Repository + UseCase Usage

```kotlin
val repository = FileRepository(context)
val useCase = ShareUseCase(repository)

// Check if file exists
val prepared = useCase.prepareFile(filePath)
if (prepared != null) {
    val (_, uri) = prepared
    ShareManager.shareToWhatsApp(
        context = this,
        uri = uri,
        mimeType = "application/pdf",
        phoneNumber = "919876543210"
    )
}

// Get file metadata
val fileInfo = useCase.getFileInfo(filePath)
println("File: ${fileInfo?.name}, Size: ${fileInfo?.sizeFormatted}")

// Validate multiple files
if (useCase.validateFiles(filePaths)) {
    val uris = useCase.prepareMultiple(filePaths)
    ShareManager.shareMultipleToWhatsApp(this, uris)
}
```

## Utility Functions

### MIME Type Detection
```kotlin
val mimeType = MimeTypeUtils.getMimeType("/path/to/file.pdf")
val category = MimeTypeUtils.getFileCategory(mimeType)

// Type checking
if (MimeTypeUtils.isImage(mimeType)) { }
if (MimeTypeUtils.isVideo(mimeType)) { }
if (MimeTypeUtils.isAudio(mimeType)) { }
if (MimeTypeUtils.isDocument(mimeType)) { }
```

### App Utilities
```kotlin
if (AppUtils.isAppInstalled(context, "com.whatsapp")) {
    // App is installed
}

AppUtils.openPlayStore(context, "com.whatsapp")
```

## Constants

Use predefined constants for app packages and MIME types:

```kotlin
import com.csiw.directsharekit.utils.Constants

Constants.Apps.WHATSAPP
Constants.Apps.TELEGRAM
Constants.Apps.GMAIL
Constants.Apps.GOOGLE_DRIVE

Constants.MimeTypes.PDF
Constants.MimeTypes.IMAGE
Constants.MimeTypes.VIDEO
```

## Security Configuration

The library automatically configures FileProvider with the authority:
- `${applicationId}.directshare.provider`

The file paths configuration in `directshare_file_paths.xml` allows sharing from:
- App cache directory
- App internal files directory
- External files directory
- External cache directory
- External storage root

## Required Permissions

Add to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

For Android 11+, the library uses the `<queries>` block to detect installed apps.

Note: if your app shares files only through the system picker (`OpenDocument`/`OpenMultipleDocuments`), storage permissions may not be required.

## Best Practices

1. **Always check if app is installed** before attempting to share
2. **Use appropriate MIME types** for better app matching
3. **Provide optional metadata** (message) for better UX
4. **Handle file access errors gracefully** - FileProvider may throw exceptions
5. **Test with real devices** - App detection behavior varies

## Publish-Ready

- Release checklist: `directsharekit/PUBLISH_READY_CHECKLIST.md`
- Version/changelog template: `directsharekit/CHANGELOG.md`
- Recommended process:
  1. Update `CHANGELOG.md` (`Unreleased` -> version section)
  2. Tag release (`vX.Y.Z`)
  3. Publish artifact (JitPack/Maven/internal registry)
  4. Update integration snippet in this README if coordinates change

### Quick Deploy via JitPack

1. Update publishing properties in `gradle.properties`:
   - `POM_GROUP_ID`
   - `POM_VERSION`
   - `POM_URL`
   - `POM_DEVELOPER_*`
   - `POM_SCM_*`
2. Build and test locally:

```bash
./gradlew :directsharekit:assembleRelease --no-daemon
./gradlew :directsharekit:publishToMavenLocal --no-daemon
```

3. Push source and tag:

```bash
git add .
git commit -m "release: directsharekit v1.0.0"
git tag v1.0.0
git push origin main
git push origin v1.0.0
```

4. Consume from another app:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.github.DesaiVinubhai:DirectShare:v1.0.0")
}
```

## License

Part of the DirectShare Example Application
