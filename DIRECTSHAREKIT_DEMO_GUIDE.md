# DirectShareKit Demo Guide

This single guide explains the complete demo in `app/src/main/java/com/csiw/directshare/MainActivity.kt` and covers all `ShareManager` methods.

## What the demo screen now includes

- File selection:
  - `Pick single file` for single-share methods.
  - `Pick multiple files` for multi-share methods.
  - `Clear selection` to reset demo state.
- App checks:
  - `Refresh installed-app status` calls:
    - `isWhatsAppInstalled()`
    - `isTelegramInstalled()`
    - `isMessengerInstalled()`
    - `isGmailInstalled()`
    - `isDriveInstalled()`
- Share method buttons (all methods covered):
  - `shareToWhatsApp()`
  - `shareMultipleToWhatsApp()`
  - `shareToTelegram()`
  - `shareMultipleToTelegram()`
  - `shareToMessenger()`
  - `shareMultipleToMessenger()`
  - `shareViaGmail()`
  - `shareMultipleViaGmail()`
  - `shareViaDrive()`
  - `shareMultipleViaDrive()`
  - `shareGeneric()`
  - `shareMultiple()`

## Behavior rules in the demo

- Single-share buttons are enabled only when exactly 1 file is selected.
- Multi-share buttons are enabled only when 2+ files are selected.
- App-specific buttons are enabled only when the corresponding app is installed.
- Generic share methods do not require an installed app check.
- Toast messages guide the user for incorrect selection states.

## Optional parameters demonstrated

- WhatsApp single/multi use optional:
  - `phoneNumber`
  - `message`
  - `userName`
- Telegram uses optional `message`.
- Gmail single/multi use:
  - `recipient`
  - `subject`
  - `body`

## File references

- Demo activity: `app/src/main/java/com/csiw/directshare/MainActivity.kt`
- Demo layout: `app/src/main/res/layout/activity_main.xml`
- Demo strings: `app/src/main/res/values/strings.xml`
- Library methods: `directsharekit/src/main/java/com/csiw/directsharekit/ui/ShareManager.kt`

## Quick manual test checklist

1. Tap `Pick single file` and choose one file.
2. Verify single buttons become enabled for installed apps.
3. Test each single-share button.
4. Tap `Pick multiple files` and choose at least 2 files.
5. Verify multi buttons become enabled for installed apps.
6. Test each multi-share button.
7. Tap `Refresh installed-app status` and confirm status text updates.
8. Tap `Clear selection` and verify all share buttons disable again.

## Notes

- Gmail methods require Gmail installed on the test device.
- Drive methods require Google Drive installed on the test device.
- WhatsApp direct chat behavior depends on valid country-code phone input and app-side policy.

