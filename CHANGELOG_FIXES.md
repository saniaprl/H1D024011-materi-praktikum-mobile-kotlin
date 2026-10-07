# Catatan Perbaikan

### Fix #1 — Nama launcher HomeActivity: label activity menimpa nama aplikasi

| Kolom | Detail |
|---|---|
| Tanggal | 2026-10-07 |
| File | `app/src/main/AndroidManifest.xml` |
| Masalah | Nama aplikasi pada launcher tampil `HomeActivity`, bukan `Jualan`. |
| Akar | `HomeActivity` adalah activity `MAIN`/`LAUNCHER` dan memiliki `android:label="@string/title_activity_home"`; nilai string tersebut adalah `HomeActivity`, sehingga menimpa label aplikasi. |
| Fix | Mengarahkan label `HomeActivity` ke `@string/app_name`, yang bernilai `Jualan`. |
| Verifikasi | `:app:assembleDebug --offline` berhasil dalam 21 detik: 36 task, 7 dieksekusi dan 29 up-to-date. Sebelum fix ada 1 label launcher bernilai `HomeActivity`; sesudah fix label launcher merujuk `app_name` (`Jualan`). Belum diuji langsung pada launcher perangkat. |
| Pelajaran | Label pada activity launcher lebih spesifik daripada label `<application>` dan menentukan nama yang dapat ditampilkan launcher. |
| Log Keyword | `HomeActivity`, `android:label`, `app_name`, `launcher`, `Jualan` |
| Deploy | BELUM DEPLOY — APK debug berhasil dibangun, belum dipasang dan diverifikasi pada perangkat. |
