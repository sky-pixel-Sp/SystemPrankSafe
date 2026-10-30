# SystemPrankSafe

Aplikasi prank fullscreen. Begitu dibuka, aplikasi langsung memutar `app/src/main/res/raw/prank_video.mp4` bila aset video tersebut tersedia. Sesudah video, muncul simulasi error sistem dan progres shutdown palsu, layar hitam singkat, lalu reveal **KENA PRANK!**.

## Sematan layar
Sekitar 1,2 detik setelah dibuka, aplikasi meminta Android menampilkan dialog resmi **screen pinning**. Pengguna harus menyetujuinya sendiri di dialog sistem; bila ditolak, aplikasi tetap berjalan normal. Sematan bisa dilepas memakai gesture/tombol unpin Android atau tombol tutup setelah reveal.

Aplikasi tidak memakai Device Owner/kiosk privileges, tidak mematikan atau mengunci perangkat sungguhan, tidak menghapus data, dan tidak memakai root, Accessibility, Device Admin, atau izin daya.

## Video
Source ZIP lengkap menyertakan video pengguna di:
`app/src/main/res/raw/prank_video.mp4`

Untuk build dari repository ini dengan video tertanam, unggah file tersebut pada path yang sama lalu commit ke branch `main`. Sampai file MP4 tersedia di repository, APK GitHub Actions memakai intro diagnostik animasi bawaan sebagai fallback.

## Build
GitHub Actions membangun APK debug setiap push ke `main` atau saat dijalankan manual. Artifact: `SystemPrankSafe-debug-apk`.

- Application ID: `com.my.newproject5`
- Minimum Android: API 23
- Compile/Target SDK: 35
- APK output: `app/build/outputs/apk/debug/app-debug.apk`
