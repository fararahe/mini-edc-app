# Mini EDC Merchant App

Aplikasi Android yang mensimulasikan terminal Electronic Data Capture (EDC) untuk merchant. Mini proyek ini digunakan sebagai assesment test sebagai Kotlin Android Develoer dengan tujuan untuk mengevaluasi penguasaan clean code, modular, dan maintainable Android applikasi untuk pengembangan EDC kedepannya

## 🚦 Memulai (Getting Started)

1. **Clone repository.**
2. **Buka di Android Studio**: Versi yang direkomendasikan adalah **Ladybug (2024.2.1)** atau yang lebih baru Panda 4(2025.3.4 Patch 1)
3. **Sync Gradle**: Proyek ini menggunakan **Version Catalog** (`libs.versions.toml`) dan **Kotlin DSL**.
4. **Jalankan**: Pilih modul `:app` dan jalankan di emulator atau perangkat fisik (Min SDK 24).

---

## 🏗 Keputusan Arsitektur (Architectural Decisions)

Aplikasi ini mengikuti prinsip **Clean Architecture** untuk memastikan logika bisnis tidak tercampur dengan perangkat keras (hardware) dan UI.

- **Domain-Layer**: Logika inti berada di lapisan `domain`
- **Single Source of Truth**: **Room Database** sebagai sumber data lokal utama untuk semua transaksi, sedangkan **DataStore** menangani pengaturan seperti status aktivasi dan nomor trace.
- **Reactive State Management**: State UI dikelola menggunakan `StateFlow` di ViewModel, memastikan aliran data satu arah (UDF) dan membuat UI lebih dapat diprediksi.
- **Hilt Dep. Injection**: Aplikasi menggunakan Hilt untuk mengelola dependency secara otomatis dan terpusat.
---

## 🔄 Alur Inti (Core Flows)

### 1. Alur Aktivasi
1. Pengguna memasukkan **Terminal ID**, **Merchant ID**, dan **Kode Aktivasi**.
2. `ActivationViewModel` memvalidasi input dan memanggil `ActivateDeviceUseCase`.
3. **Aturan Mock**: Kode memeriksa apakah kode aktivasi adalah `123456`.
4. Setelah berhasil, data merchant dan `AuthToken` mock disimpan di **DataStore**.
5. Aplikasi berpindah ke Halaman Utama.

### 2. Alur Transaksi Sale
1. Pengguna memasukkan nominal di **Layar Sale**.
2. `processSale` memicu `ProcessSaleUseCase`.
3. **Pembuatan Trace**: Nomor trace 6-digit yang unik dibuat secara otomatis melalui `AuthDataStore`.
4. **Aturan Simulasi**:
    - Digit terakhir `1` -> `PENDING`
    - Digit terakhir `2` -> `FAILED`
   - Digit terakhir `0` atau lainnya -> `SUCCESS`
5. Transaksi disimpan ke **Room Database** sebelum hasilnya dikembalikan ke UI.

### 3. Alur Struk & Mock Printer
1. Setelah penjualan berhasil, `GenerateReceiptUseCase` memformat data transaksi.
2. `PrinterService` (Interface) dipanggil untuk "mencetak" struk.
3. `PrinterServiceImpl` (Mock) mensimulasikan pencetakan fisik dengan delay 2 detik.
4. **Kegagalan Printer**: Jika **Nomor Trace** berakhir dengan angka `9`, sistem akan mensimulasikan error "Out of Paper" (Kehabisan Kertas).
---

## 📋 Cheat-Sheet Simulasi (Testing)

| Skenario | Pemicu |
| :--- | :--- |
| **Aktivasi Berhasil** | Gunakan Kode `123456` |
| **Transaksi Berhasil**| Nominal berakhiran `0` (misal: 1000) |
| **Transaksi Pending**| Nominal berakhiran `1` (misal: 1001) |
| **Transaksi Gagal**  | Nominal berakhiran `2` (misal: 1002) |
| **Retry Sync Gagal** | Retry transaksi pending dengan nominal berakhiran `5` |
| **Printer Gagal**    | Transaksi apa pun dengan Nomor Trace berakhiran `9` |

---

## 📝 Lisensi
Copyright © 2024 Faradyna. All rights reserved.
