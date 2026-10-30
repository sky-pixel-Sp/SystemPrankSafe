# SystemPrankSafe

Aplikasi prank fullscreen. Begitu dibuka, video pengguna langsung diputar fullscreen. Sesudah video, muncul simulasi error sistem dan progres shutdown palsu, layar hitam singkat, lalu reveal **KENA PRANK!**.

## Sematan layar
Sekitar 1,2 detik setelah dibuka, aplikasi meminta Android menampilkan dialog resmi **screen pinning**. Pengguna harus menyetujuinya sendiri di dialog sistem; bila ditolak, aplikasi tetap berjalan normal. Pengguna bisa melepaskan sematan dengan gesture/tombol unpin Android yang ditampilkan oleh sistem atau menutup prank dari tombol setelah reveal.

Aplikasi tidak memakai Device Owner/kiosk privileges, tidak mematikan atau mengunci perangkat sungguhan, tidak menghapus data, tidak memakai root, Accessibility, Device Admin, atau izin daya.

## Video
Video pengguna ada di `app/src/main/res/raw/prank_video.mp4`, diputar langsung ketika aplikasi dibuka.

## Build
GitHub Actions workflow `.github/workflows/android.yml` membangun APK debug setiap push ke `main` atau saat dijalankan manual. Artifact: `SystemPrankSafe-debug-apk`.

- Application ID: `com.my.newproject5`
- Minimum Android: API 23
- Compile/Target SDK: 35
- APK output: `app/build/outputs/apk/debug/app-debug.apk`
