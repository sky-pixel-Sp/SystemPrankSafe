# SystemPrankSafe

Aplikasi Android visual fiksi dengan video otomatis dan simulasi pemulihan sistem.

## Alur
1. Video yang tersedia di `assets/*.mp4` diputar otomatis fullscreen saat aplikasi dibuka.
2. Setelah video, tampil layar SYSTEM FAILURE dengan progress animasi.
3. Tampil layar DEVICE RECOVERY dengan keypad dan 10 percobaan memasukkan kode simulasi.
4. Kode yang dimasukkan langsung dibuang di memori; tidak disimpan, dicatat, atau dikirim.
5. Jika 10 percobaan habis atau pengguna menekan GIVE UP / RESTART, aplikasi menampilkan animasi RESTARTING lalu menutup dirinya sendiri. HP Android tetap hidup dan normal.

## Batas keselamatan
- Input adalah kode simulasi. Jangan pernah masukkan PIN asli layar perangkat.
- Tidak ada Device Admin, Accessibility Service, overlay, root, atau izin sensitif.
- Tidak memakai Lock Task / Screen Pinning dan tidak menghalangi pengguna keluar.
- Tombol Back Android menutup aplikasi.
- Tidak ada perintah shutdown atau reboot perangkat. Animasi restart hanya tampilan di dalam aplikasi.

## Video
Taruh file MP4 di folder `assets/`. Workflow akan mengambil file MP4 pertama dari folder itu dan menyalinnya sebagai `app/src/main/res/raw/prank_video.mp4` sebelum build.

## Build
GitHub Actions membangun APK debug setiap push ke `main` atau saat dijalankan manual. Artifact: `SystemPrankSafe-debug-apk`.

- Application ID: `com.my.newproject5`
- Minimum Android: API 23
- Compile/Target SDK: 35
- Output: `app/build/outputs/apk/debug/app-debug.apk`
