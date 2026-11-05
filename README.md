# Save Phone

**Save Phone** adalah aplikasi Android pendamping keamanan perangkat dengan fitur alarm, penguncian layar, dan kamera manual. Aplikasi ini dirancang untuk membantu pemilik perangkat merespons situasi kehilangan atau akses yang tidak diinginkan—bukan sebagai alat pelacakan tersembunyi.

> **Status proyek:** kandidat build v2.0.0. Source dan workflow CI tersedia di branch `save-phone-build`. Keberhasilan build otomatis tidak sama dengan validasi di perangkat fisik. Uji kamera, audio, notifikasi, dan Device Admin pada perangkat Android nyata masih perlu dilakukan sebelum rilis stabil.

## Fitur

- **Alarm keamanan 60 detik** — pemutaran alarm dilakukan oleh aplikasi dengan gain internal maksimum dan mekanisme pengulangan suara.
- **Kunci layar** — menggunakan Android Device Admin dan `DevicePolicyManager.lockNow()` setelah pengguna mengaktifkan izin administrator perangkat.
- **Kamera manual** — pengguna membuka aplikasi lalu memilih kamera depan atau belakang. Aplikasi memanggil aplikasi kamera sistem; hasil foto disimpan di penyimpanan privat aplikasi.
- **Riwayat foto** — melihat, membagikan, dan menghapus foto yang diambil melalui fitur kamera manual.
- **Peringatan percobaan akses gagal** — setelah ambang kegagalan yang dikonfigurasi tercapai, aplikasi menampilkan notifikasi dengan tindakan untuk membunyikan alarm.

## Batasan keamanan dan Android

Save Phone mengikuti batasan keamanan Android dan tidak menjanjikan kendali yang tidak diberikan kepada aplikasi biasa.

- Aplikasi **tidak dapat menjamin alarm selalu terdengar pada volume fisik 100%**. Volume sistem, mode senyap, Do Not Disturb, tombol volume, kebijakan perangkat, dan force-stop tetap dapat memengaruhi pemutaran.
- Aplikasi tidak dapat mematikan perangkat secara paksa, memblokir seluruh tombol perangkat keras, membaca PIN/pola/kata sandi layar, atau melewati autentikasi pemilik.
- Kamera **tidak berjalan diam-diam** dan tidak otomatis mengambil foto saat alarm aktif. Pengambilan foto memerlukan tindakan manual dari pengguna dan bergantung pada dukungan aplikasi kamera/OEM.
- Peringatan percobaan akses gagal memberikan notifikasi; alarm tidak otomatis dimulai tanpa tindakan pengguna.
- Pemilihan kamera depan/belakang bersifat best-effort karena perilaku aplikasi kamera berbeda antarperangkat.
- Aplikasi tidak mengirim foto ke server dan tidak menyediakan pelacakan lokasi jarak jauh.

## Informasi teknis

| Komponen | Nilai |
|---|---|
| Nama aplikasi | Save Phone |
| Application ID | `id.savephone.app` |
| Versi sumber | `2.0.0` |
| Bahasa | Java |
| Java toolchain | JDK 17 |
| Compile / Target SDK | 35 / 35 |
| Minimum SDK | 26 (Android 8.0) |
| Build | Gradle melalui GitHub Actions |

## Mendapatkan APK dari GitHub Actions

1. Buka tab **Actions** pada repository.
2. Pilih workflow **Build Save Phone APK**.
3. Buka run yang berstatus **completed** dan periksa apakah hasilnya sukses.
4. Di bagian **Artifacts**, unduh artifact **`Save-Phone-v2.0.0-debug-apk`**.
5. Ekstrak file ZIP artifact, lalu instal APK pada perangkat uji.

APK debug ditujukan untuk pengujian, bukan distribusi produksi. Jangan instal build dari sumber yang tidak dipercaya.

## Build sendiri

Workflow CI menggunakan alur berikut:

1. Mengekstrak `Save-Phone-Android.tar.xz` ke direktori `Save-Phone-Android/`.
2. Menggabungkan bagian patch di `v2patch/`, mendekode patch, lalu menerapkannya ke source.
3. Menyiapkan JDK 17, Android SDK Platform 35, Build Tools 35.0.0, dan Gradle 8.9.
4. Menjalankan validator source dan `gradle --no-daemon clean assembleDebug`.

Untuk mereplikasi build secara lokal, jalankan alur yang sama dari root repository menggunakan Bash (Linux, macOS, WSL, atau Git Bash di Windows). **Jangan melewati langkah penerapan patch v2**: arsip source dan patch v2 adalah dua input terpisah dari workflow.

Perintah build setelah patch diterapkan:

```bash
cd Save-Phone-Android
gradle --no-daemon clean assembleDebug
```

APK debug dihasilkan di:

```
Save-Phone-Android/app/build/outputs/apk/debug/app-debug.apk
```

## Izin yang digunakan

- **Kamera:** hanya untuk alur pengambilan foto manual yang dimulai pengguna.
- **Device Admin:** diperlukan untuk fungsi kunci layar; aktivasi dilakukan secara eksplisit oleh pemilik perangkat.
- **Notifikasi:** untuk menyampaikan peringatan dan menyediakan tindakan alarm.
- **Foreground service/audio (jika diperlukan oleh konfigurasi Android):** untuk mendukung pemutaran alarm sesuai batasan versi Android.

Tinjau dialog izin Android sebelum mengaktifkan fitur. Cabut izin atau nonaktifkan administrator perangkat melalui pengaturan Android jika ingin menghentikan fungsi terkait.

## Pengujian

Sebelum menyatakan rilis siap digunakan, lakukan pengujian pada perangkat fisik dan catat model perangkat serta versi Android:

- instalasi, pembukaan aplikasi, dan pembaruan;
- aktivasi/nonaktivasi Device Admin dan fungsi kunci layar;
- alarm selama 60 detik pada volume, mode senyap, dan DND yang berbeda;
- kamera depan/belakang, penyimpanan foto, berbagi, dan penghapusan;
- notifikasi saat aplikasi berada di latar belakang;
- perilaku setelah aplikasi dihentikan atau perangkat di-restart.

**Catatan:** status CI hanya membuktikan tahapan yang benar-benar dijalankan oleh workflow. Status tersebut tidak membuktikan kompatibilitas semua perangkat atau keberhasilan pengujian fisik.

## Struktur repository

- `.github/workflows/build-save-phone.yml` — workflow build APK.
- `Save-Phone-Android.tar.xz` — arsip source Android dasar yang digunakan workflow.
- `v2patch/` — lima bagian patch source v2.0.0 yang diperlukan oleh workflow.

## Kontribusi dan pelaporan masalah

Laporkan masalah dengan menyertakan:
- model perangkat dan versi Android;
- langkah untuk mereproduksi masalah;
- hasil yang diharapkan dan hasil aktual;
- log build atau logcat yang relevan (hapus data pribadi sebelum membagikan).

---

**Repository:** [sky-pixel-Sp/save-phone-build](https://github.com/sky-pixel-Sp/save-phone-build)  
**Branch pengembangan:** `save-phone-build`
