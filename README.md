# Save Phone — Android Anti-Theft Companion

This branch contains the Save Phone Android source package and a GitHub Actions workflow.

- App name: Save Phone
- Application ID: id.savephone.app
- Source archive: Save-Phone-Android.tar.xz
- Build workflow: Actions → Build Save Phone APK
- Artifact after a successful build: Save-Phone-debug-apk

The source archive expands into `Save-Phone-Android/`. The workflow installs JDK 17, Android SDK 35, and Gradle 8.9, runs the structural validator, then builds a debug APK.

This project is currently a source/build candidate. A successful CI run and physical-device test are required before calling the APK verified.
