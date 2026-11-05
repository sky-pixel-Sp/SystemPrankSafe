# Save Phone

<div align="center">

**Pendamping keamanan Android dengan alarm, penguncian layar, dan kamera manual.**

[![Build](https://github.com/sky-pixel-Sp/save-phone-build/actions/workflows/build-save-phone.yml/badge.svg?branch=save-phone-build)](https://github.com/sky-pixel-Sp/save-phone-build/actions/workflows/build-save-phone.yml)
![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-orange?logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min%20SDK-26-blue)
![Version](https://img.shields.io/badge/Version-2.0.0-informational)

[![DOWNLOAD SAVE PHONE v2.0.0](https://img.shields.io/badge/DOWNLOAD-SAVE%20PHONE%20v2.0.0-168B4E?style=for-the-badge&logo=android&logoColor=white)](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41962387449/artifacts/13386141162)

[Repository](https://github.com/sky-pixel-Sp/save-phone-build) · [Semua build](https://github.com/sky-pixel-Sp/save-phone-build/actions) · [Source Android](https://github.com/sky-pixel-Sp/save-phone-build/blob/save-phone-build/Save-Phone-Android.tar.xz)

</div>

---

## Unduh aplikasi

<blockquote>
<strong>Save Phone v2.0.0 — Debug APK</strong><br>
Build GitHub Actions #21 · 5 November 2026<br>
<a href="https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41962387449/artifacts/13386141162">Unduh Save-Phone-v2.0.0-debug-apk</a>
</blockquote>

**Cara mengunduh dan memasang:**

1. Buka tautan unduhan di atas.
2. Unduh file ZIP artifact dari GitHub.
3. Ekstrak ZIP, kemudian temukan file `app-debug.apk`.
4. Buka APK di Android dan ikuti dialog instalasi.
5. Bila Android meminta izin, izinkan pemasangan dari sumber tersebut hanya setelah memastikan file berasal dari repository ini.

**Penting:** file yang tersedia adalah *debug APK* untuk pengujian, bukan rilis produksi. Artifact GitHub Actions ini memiliki masa penyimpanan terbatas dan dijadwalkan kedaluwarsa pada **19 November 2026, sekitar 10.35 WIB**. Setelah kedaluwarsa, buka [halaman Actions](https://github.com/sky-pixel-Sp/save-phone-build/actions) dan unduh artifact dari build sukses terbaru.

## Tentang Save Phone

Save Phone adalah aplikasi Android pendamping keamanan perangkat. Tujuannya membantu pemilik perangkat merespons kondisi kehilangan atau akses yang tidak diinginkan melalui kontrol yang memang diizinkan Android.

Aplikasi ini **bukan** aplikasi pelacakan tersembunyi. Fitur kamera dimulai secara manual oleh pengguna dan tidak mengambil foto secara diam-diam.

## Fitur utama

| Fitur | Keterangan |
|---|---|
| Alarm keamanan | Alarm berjalan selama 60 detik dengan gain internal aplikasi maksimum. |
| Kunci layar | Menggunakan Android Device Admin dan `DevicePolicyManager.lockNow()` setelah izin administrator diaktifkan pemilik. |
| Kamera manual | Memanggil aplikasi kamera sistem setelah pengguna memilih kamera depan atau belakang. |
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
| Versi | `2.0.0` |
| Bahasa | Java |
| JDK | 17 |
| Compile SDK | 35 |
| Target SDK | 35 |
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

Build terakhir yang ditautkan di atas berasal dari workflow **Build Save Phone APK**, run #21.

| Pemeriksaan | Hasil |
|---|---|
| Parse file XML | Lulus — 7 file |
| Pencocokan komponen manifest dengan source Java | Lulus — 3 komponen |
| Pemeriksaan metadata v2, Device Admin, alarm tetap, dan kamera manual | Lulus |
| Gradle `clean assembleDebug` | **BUILD SUCCESSFUL** |
| Upload artifact APK | Berhasil |

Pemeriksaan struktur dan build CI tidak menggantikan pengujian pada perangkat fisik. Kompatibilitas kamera, audio, notifikasi, dan kebijakan OEM tetap perlu diuji langsung pada beberapa perangkat Android.

[Periksa log dan status run #21](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41962387449)

## Build dari source

Workflow CI membangun aplikasi dari dua input utama:

- `Save-Phone-Android.tar.xz` — source Android dasar.
- `v2patch/` — lima bagian patch v2.0.0 yang wajib digabung dan diterapkan.

Lingkungan build: JDK 17, Android SDK Platform 35, Build Tools 35.0.0, dan Gradle 8.9.

Contoh alur build lokal dari root repository menggunakan Bash:

```bash
# 1. Ekstrak source dasar
tar -xJf Save-Phone-Android.tar.xz

# 2. Gabungkan dan dekode patch v2
cat v2patch/save-phone-v2.patchb64.part*.txt > /tmp/save-phone-v2.patch.gz.b64
base64 --decode /tmp/save-phone-v2.patch.gz.b64 | gzip -d > /tmp/save-phone-v2.patch

# 3. Periksa lalu terapkan patch
git apply --check --directory=Save-Phone-Android /tmp/save-phone-v2.patch
git apply --directory=Save-Phone-Android /tmp/save-phone-v2.patch

# 4. Build APK debug
cd Save-Phone-Android
gradle --no-daemon clean assembleDebug
```

Hasil build berada di:

```
Save-Phone-Android/app/build/outputs/apk/debug/app-debug.apk
```

## Struktur repository

```text
.
├── .github/
│   └── workflows/
│       └── build-save-phone.yml
├── v2patch/
│   ├── save-phone-v2.patchb64.part01.txt
│   ├── save-phone-v2.patchb64.part02.txt
│   ├── save-phone-v2.patchb64.part03.txt
│   ├── save-phone-v2.patchb64.part04.txt
│   └── save-phone-v2.patchb64.part05.txt
├── Save-Phone-Android.tar.xz
└── README.md
```

Jangan menghapus arsip source, file patch, atau workflow: ketiganya merupakan input penting untuk menghasilkan APK melalui CI.

## Pengujian perangkat yang disarankan

Sebelum digunakan sebagai aplikasi keamanan utama, uji pada perangkat fisik:

- instalasi, pembukaan aplikasi, dan pembaruan;
- aktivasi Device Admin serta fungsi kunci layar;
- alarm 60 detik pada volume, mode senyap, dan DND yang berbeda;
- kamera depan/belakang serta penyimpanan, berbagi, dan penghapusan foto;
- notifikasi ketika aplikasi berada di latar belakang;
- perilaku setelah aplikasi dihentikan atau perangkat di-restart.

## Bantuan dan pelaporan masalah

Saat melaporkan bug, sertakan model perangkat, versi Android, langkah reproduksi, hasil yang diharapkan, hasil aktual, dan log yang relevan. Hapus data pribadi dari log sebelum membagikannya.

---

<div align="center">

**Save Phone · v2.0.0**  
[GitHub Repository](https://github.com/sky-pixel-Sp/save-phone-build) · [Download APK](https://github.com/sky-pixel-Sp/save-phone-build/actions/runs/41962387449/artifacts/13386141162)

</div>
