# SystemPrankSafe

Aplikasi prank fullscreen. **Begitu dibuka, video langsung diputar otomatis** tanpa tombol mulai. Sesudah video selesai, muncul simulasi error sistem dan progres shutdown palsu, layar hitam singkat, lalu reveal **KENA PRANK!**.

## Sematan layar
Sekitar 1,2 detik setelah dibuka, aplikasi meminta Android menampilkan dialog resmi **screen pinning**. Pengguna harus menyetujuinya sendiri di dialog sistem; bila ditolak, aplikasi tetap berjalan normal. Sematan dapat dihentikan dengan tombol keluar pada reveal atau gesture/tombol unpin Android.

Aplikasi tidak memakai Device Owner/kiosk privileges, tidak mematikan perangkat, tidak menghapus data, dan tidak memakai root, Accessibility, Device Admin, atau izin daya. Aplikasi tidak dapat memaksa pengguna tetap berada di dalamnya.

## Video milikmu
Unggah video MP4 ke lokasi berikut di repository:

`assets/prank_video.mp4`

Workflow otomatis menyalinnya ke `app/src/main/res/raw/prank_video.mp4` sebelum build. Nama file harus persis `prank_video.mp4`. Video asli yang kamu unggah berdurasi sekitar 25 detik dan orientasi portrait. Karena repository ini bersifat publik, file video yang kamu unggah ke sana juga akan terlihat publik.

Sebelum video tersedia di repository, APK memakai intro diagnostik animasi bawaan sebagai fallback.

## Build
GitHub Actions membangun APK debug setiap push ke `main` atau saat dijalankan manual. Artifact: `SystemPrankSafe-debug-apk`.

- Application ID: `com.my.newproject5`
- Minimum Android: API 23
- Compile/Target SDK: 35
- APK output: `app/build/outputs/apk/debug/app-debug.apk`
