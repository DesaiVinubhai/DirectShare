# DirectShareKit Publish-Ready Checklist

Use this checklist before publishing DirectShareKit for external Android developers.

## 1) API & Code Quality

- [ ] Public API reviewed (`ShareManager`, `FileRepository`, `ShareUseCase`, utils)
- [ ] No accidental API removals or signature breaks
- [ ] Deprecated APIs documented with migration notes
- [ ] Lint warnings reviewed and accepted/fixed
- [ ] `./gradlew :directsharekit:compileDebugKotlin` passes

## 2) Behavior Validation

- [ ] Single-file share works (WhatsApp, Telegram, Messenger, Gmail, Drive, generic)
- [ ] Multi-file share works (WhatsApp, Telegram, Messenger, Gmail, Drive, generic)
- [ ] App detection methods return expected values on real devices
- [ ] FileProvider URIs are readable by target apps
- [ ] Error/fallback behavior is user-safe (no crashes)

## 3) Packaging & Build

- [ ] `directsharekit/build.gradle.kts` has correct `namespace`, `minSdk`, `compileSdk`
- [ ] `consumer-rules.pro` matches actual public API
- [ ] `AndroidManifest.xml` provider and `<queries>` entries are correct
- [ ] `directshare_file_paths.xml` is least-privilege for your use case

## 4) Documentation

- [ ] `directsharekit/README.md` examples match current method signatures
- [ ] Integration steps are accurate (`settings.gradle.kts`, dependency, provider authority)
- [ ] Known limitations are documented (app version/device variance)
- [ ] Release notes prepared in `directsharekit/CHANGELOG.md`

## 5) Versioning & Release

- [ ] Choose version using SemVer:
  - [ ] MAJOR for breaking changes
  - [ ] MINOR for backward-compatible features
  - [ ] PATCH for backward-compatible fixes
- [ ] Move `Unreleased` notes into a dated version section in changelog
- [ ] Create git tag: `vX.Y.Z`
- [ ] Publish artifact (JitPack/Maven Central/internal repo)
- [ ] Verify clean integration in a fresh sample app

## 6) Post-Release

- [ ] Update changelog links and docs
- [ ] Announce upgrade notes and migration tips
- [ ] Track issues from early adopters and patch quickly

