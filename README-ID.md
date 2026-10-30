# SystemPrankSafe

Aplikasi Android prank visual yang aman. Ini adalah simulasi di dalam aplikasi dan **tidak pernah mematikan atau mengunci HP sungguhan**.

## Urutan aplikasi
1. Memutar `app/src/main/res/raw/prank_video.mp4` jika file tersebut tersedia.
2. Jika video belum tersedia, menampilkan intro diagnostik animasi bawaan selama sekitar 4 detik.
3. Menampilkan animasi palsu `CRITICAL SYSTEM ERROR` dan progres shutdown.
4. Menampilkan layar hitam sekitar 1,1 detik.
5. Menampilkan pesan `KENA PRANK!` dan tombol `TUTUP PRANK`.

Tombol Back tetap menjadi jalan keluar. Aplikasi tidak memakai izin Device Admin, Accessibility, root, overlay, atau izin daya.

## Build APK otomatis
Workflow GitHub Actions berada di `.github/workflows/android.yml`. Setiap push ke branch `main` akan menjalankan build debug dan mengunggah artifact bernama `SystemPrankSafe-debug-apk`.

Buka tab **Actions**, pilih workflow **Build SystemPrankSafe APK**, lalu unduh artifact setelah status berhasil.

## Menambahkan video pribadi
Tempatkan video MP4 yang ingin dipakai di:
`app/src/main/res/raw/prank_video.mp4`

Nama file harus huruf kecil dan underscore. Setelah file ditambahkan dan di-push ke branch `main`, workflow akan membangun ulang APK dengan video tersebut tertanam.

## Info teknis
- Application ID: `com.my.newproject5`
- Minimum Android: API 23 (Android 6)
- Compile/Target SDK: 35
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
