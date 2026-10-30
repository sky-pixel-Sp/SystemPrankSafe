# SystemPrankSafe 4.0 — NEXUS Utility

Aplikasi Android untuk simulasi visual fiksi bergaya game utility/injector. Desain awal sengaja sederhana seperti dashboard tool; video kejutan baru mulai setelah tombol **INITIALIZE ENGINE** ditekan.

## Urutan simulasi
1. Saat dibuka, tampil dashboard NEXUS Performance Engine dengan pilihan profil Free Fire / Roblox.
2. Pengguna menekan **INITIALIZE ENGINE**.
3. Progress inisialisasi lokal berjalan sekitar 2 detik.
4. Video MP4 dari folder `assets/` diputar fullscreen bersama audionya.
5. Setelah video selesai, muncul simulasi SYSTEM FAILURE.
6. Muncul 50 alert visual di dalam aplikasi (bukan notifikasi Android).
7. Recovery keypad memberi 10 kesempatan memasukkan kode demo.
8. Jika kesempatan habis atau tombol lanjut ditekan, muncul animasi RESTARTING lalu Activity ditutup. HP tidak direstart.

## Keamanan
- Tidak ada Device Admin, Accessibility Service, Lock Task / Screen Pinning, root, overlay, atau izin sensitif.
- Tidak ada akses ke PIN layar perangkat. Kode recovery hanya input demo yang langsung dibuang, tidak disimpan, dicatat, atau dikirim.
- Tidak mengirim notifikasi Android; 50 alert hanya kartu visual dalam aplikasi.
- Tidak ada perintah shutdown atau reboot perangkat.
- Tombol Back Android tetap menjadi jalan keluar.
- Tidak menyuntikkan kode ke Free Fire atau Roblox; pilihan profil hanya tampilan.

## Video kejutan
Workflow mencari MP4 pertama di folder `assets/` dan menyalinnya sebagai resource lokal `prank_video.mp4`. Video dan gambar antarmuka tidak memerlukan akses internet.

## Build
GitHub Actions membangun APK debug setiap push ke `main`. Artifact: `SystemPrankSafe-debug-apk`.

- Application ID: `com.my.newproject5`
- Launcher label: Game Utility
- Version: 4.0
- Minimum Android: API 23
- Compile / Target SDK: 35
- Output: `app/build/outputs/apk/debug/app-debug.apk`
