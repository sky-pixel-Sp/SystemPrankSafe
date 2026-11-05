# Save Phone

<div align="center">

**Pendamping keamanan Android dengan alarm, penguncian layar, kamera manual, dan animasi antarmuka yang halus.**

[![Build](https://github.com/sky-pixel-Sp/save-phone-build/actions/workflows/build-save-phone.yml/badge.svg?branch=save-phone-build)](https://github.com/sky-pixel-Sp/save-phone-build/actions/workflows/build-save-phone.yml)
![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-orange?logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min%20SDK-26-blue)
![Version](https://img.shields.io/badge/Version-2.1.0-informational)

[![DOWNLOAD SAVE PHONE v2.1.0](https://img.shields.io/badge/DOWNLOAD-SAVE%20PHONE%20v2.1.0-168B4E?style=for-the-badge&logo=android&logoColor=white)](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41966558930/artifacts/13387637703)

[Repository](https://github.com/sky-pixel-Sp/save-phone-build) · [Semua build](https://github.com/sky-pixel-Sp/save-phone-build/actions) · [Build v2.1.0](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41966558930)

</div>

---

## Animasi visual

<div align="center">

<img src="https://www.icosix.com/uploads/gifs/2026/07/safe-box.gif?cache=1784054494" width="220" alt="GIF animasi safe box bertema perlindungan perangkat" />

**Perlindungan perangkat · Save Phone**

</div>

Animasi Safe Box di atas di-embed dari [IcoSix — Safe Box GIF](https://www.icosix.com/gif/681/safe-box). Halaman sumber mencantumkan lisensi **CC0 1.0 Universal**. GIF di-host oleh situs sumber sehingga repository tidak menyimpan salinan aset tersebut.

Referensi animasi keamanan lainnya: [LottieFiles — Security Shield](https://lottiefiles.com/free-animation/security-shield-JNSBdZYT7r) dan [LottieFiles — Android Security](https://lottiefiles.com/free-animation/android-security-nJTjX7Fdau). Keduanya ditandai sebagai animasi gratis pada halaman LottieFiles.

## Unduh aplikasi

<blockquote>
<strong>Save Phone v2.1.0 — Debug APK</strong><br>
GitHub Actions build #25 · 5 November 2026<br>
<a href="https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41966558930/artifacts/13387637703">Unduh Save-Phone-v2.1.0-debug-apk</a>
</blockquote>

**Cara instalasi:**

1. Buka tautan unduhan di atas.
2. Unduh ZIP artifact dari GitHub.
3. Ekstrak ZIP dan cari file `app-debug.apk`.
4. Buka APK di Android, lalu ikuti dialog instalasi.
5. Izinkan pemasangan dari sumber tersebut hanya setelah memastikan file berasal dari repository ini.

**Catatan:** APK ini adalah *debug build* untuk pengujian, bukan rilis produksi. Artifact disimpan selama 14 hari dan dijadwalkan kedaluwarsa pada **19 November 2026, pukul 11.15 WIB**. Setelah kedaluwarsa, buka [halaman Actions](https://github.com/sky-pixel-Sp/save-phone-build/actions) dan pilih build sukses terbaru.

## Apa yang baru di v2.1.0

- **Hero banner animatif** pada Beranda dengan ikon shield yang berdenyut lembut.
- **Transisi halaman** berupa efek fade dan gerakan naik singkat saat berpindah menu.
- **Efek sentuh tombol** dengan animasi scale yang singkat.
- **Navigasi bawah interaktif** dengan umpan balik animasi saat tab dipilih.
- **Animasi lokal dan offline:** aplikasi tidak perlu mengunduh GIF atau menghubungi server animasi.
- Animasi menghormati pengaturan animator Android. Jika animasi sistem dimatikan, seluruh konten dan tombol tetap dapat digunakan.

## Tentang Save Phone

Save Phone adalah aplikasi Android pendamping keamanan perangkat. Tujuannya membantu pemilik perangkat merespons kondisi kehilangan atau akses yang tidak diinginkan melalui kontrol yang memang diizinkan Android.

Aplikasi ini **bukan** aplikasi pelacakan tersembunyi. Fitur kamera dimulai secara manual oleh pengguna dan tidak mengambil foto secara diam-diam.

## Fitur utama

| Fitur | Keterangan |
|---|---|
| Alarm keamanan | Alarm berjalan selama 60 detik dengan gain internal aplikasi maksimum. |
| Kunci layar | Menggunakan Android Device Admin dan `DevicePolicyManager.lockNow()` setelah izin administrator diaktifkan pemilik. |
| Kamera manual | Membuka aplikasi kamera sistem setelah pengguna memilih kamera depan atau belakang. |
| Riwayat foto | Melihat, membagikan, dan menghapus foto dari fitur kamera manual. |
| Peringatan akses gagal | Mengirim notifikasi setelah ambang kegagalan tercapai; pengguna dapat memilih tindakan untuk membunyikan alarm. |

## Batasan Android dan keamanan

Save Phone mengikuti batas keamanan Android. Beberapa hal penting:

- **Volume alarm tidak dapat dipaksa mengunci volume fisik 100%.** Volume sistem, mode senyap, Do Not Disturb, tombol volume, kebijakan perangkat, dan force-stop tetap dapat memengaruhi suara.
- Aplikasi biasa tidak dapat mematikan perangkat secara paksa, memblokir semua tombol perangkat keras, membaca PIN/pola/kata sandi layar, atau melewati autentikasi pemilik.
- Kamera tidak berjalan diam-diam dan tidak otomatis mengambil foto ketika alarm aktif. Pengambilan foto harus dimulai pengguna.
- Notifikasi percobaan akses gagal tidak otomatis menyalakan alarm; pengguna harus memilih tindakan alarm.
- Pemilihan kamera depan/belakang bergantung pada dukungan aplikasi kamera bawaan dan perangkat (OEM).
- Foto disimpan di penyimpanan privat aplikasi. Aplikasi tidak mengirim foto ke server dan tidak menyediakan pelacakan lokasi jarak jauh.

## Informasi teknis

| Komponen | Detail |
|---|---|
| Nama aplikasi | Save Phone |
| Application ID | `id.savephone.app` |
| Versi | `2.1.0` |
| Version code | `5` |
| Bahasa | Java |
| JDK | 17 |
| Compile / Target SDK | 35 / 35 |
| Minimum SDK | 26 (Android 8.0) |
| Sistem build | Gradle 8.9 |
| CI | GitHub Actions |

## Izin aplikasi

- **Camera:** untuk pengambilan foto manual.
- **Device Admin:** untuk fungsi kunci layar setelah aktivasi eksplisit oleh pemilik.
- **Notifications:** untuk menampilkan peringatan dan tindakan alarm.
- **Audio/foreground service:** digunakan sesuai konfigurasi dan ketentuan Android untuk pemutaran alarm.

Periksa setiap dialog izin sebelum menyetujuinya. Administrator perangkat dapat dinonaktifkan kembali melalui pengaturan Android.

## Hasil build dan pemeriksaan

Build v2.1.0 dibuat melalui workflow **Build Save Phone APK**, run #25.

| Pemeriksaan | Hasil |
|---|---|
| Parse file XML | Lulus — 8 file |
| Pencocokan komponen manifest dengan source Java | Lulus — 3 komponen |
| Device Admin, metadata v2.1, alarm, kamera manual, dan hook animasi | Lulus |
| Gradle `clean assembleDebug` | **BUILD SUCCESSFUL** |
| Upload source dan APK | Berhasil |

Pemeriksaan CI membuktikan validator dan kompilasi APK berhasil dijalankan. Namun, hal tersebut **tidak menggantikan pengujian pada perangkat fisik**. Kamera, audio, notifikasi, animasi, dan kebijakan OEM tetap perlu diuji langsung pada perangkat Android target.

[Periksa log dan status build #25](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41966558930)

## Build dari source

Workflow CI membangun aplikasi dari tiga input utama:

- `Save-Phone-Android.tar.xz` — source Android dasar.
- `v2patch/` — lima bagian patch source v2.0.0.
- `v2_1patch/` — lima bagian patch motion/UI v2.1.0.

Lingkungan build: JDK 17, Android SDK Platform 35, Build Tools 35.0.0, dan Gradle 8.9. Workflow menerapkan kedua patch secara berurutan, menjalankan validator, lalu menjalankan `assembleDebug`.

Source hasil build juga tersedia sebagai artifact [Save-Phone-source-v2.1.0](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41966558930/artifacts/13387473716).

## Struktur repository

```text
.
├── .github/workflows/build-save-phone.yml
├── v2patch/                 # patch source v2.0.0
├── v2_1patch/               # patch animasi UI v2.1.0
├── Save-Phone-Android.tar.xz
└── README.md
```

Jangan menghapus arsip source, file patch, atau workflow: semuanya merupakan input penting untuk menghasilkan APK melalui CI.

## Pengujian perangkat yang disarankan

Sebelum digunakan sebagai aplikasi keamanan utama, uji pada perangkat fisik:

- instalasi, pembukaan aplikasi, dan pembaruan;
- aktivasi Device Admin serta fungsi kunci layar;
- alarm 60 detik pada volume, mode senyap, dan DND yang berbeda;
- kamera depan/belakang serta penyimpanan, berbagi, dan penghapusan foto;
- notifikasi ketika aplikasi berada di latar belakang;
- transisi halaman, animasi shield, efek sentuh, dan pengaturan animator sistem;
- perilaku setelah aplikasi dihentikan atau perangkat di-restart.

## Bantuan dan pelaporan masalah

Saat melaporkan bug, sertakan model perangkat, versi Android, langkah reproduksi, hasil yang diharapkan, hasil aktual, dan log yang relevan. Hapus data pribadi dari log sebelum membagikannya.

---

<div align="center">

**Save Phone · v2.1.0**  
[GitHub Repository](https://github.com/sky-pixel-Sp/save-phone-build) · [Download APK](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41966558930/artifacts/13387637703)

</div>
