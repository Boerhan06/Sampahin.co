-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 25 Des 2025 pada 08.03
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_sampahin`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id_akun` int(11) NOT NULL,
  `id_admin` varchar(50) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `alamat` text DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id_akun`, `id_admin`, `nama_lengkap`, `alamat`, `no_telepon`, `email`, `username`, `password`, `is_active`, `created_at`, `updated_at`) VALUES
(1, 'ADM-1422', 'Burhan', 'Purwakarta', '089501901000', 'burhan@gmail.com', 'Burhann', '$2a$10$egiBiiinO1GDj5aGyVFJwedBAUXdgWNOtkTSKPEBowFB/HgcV48he', 1, '2025-12-22 04:03:17', '2025-12-22 04:03:17'),
(2, 'ADM-2320', 'Zulfaa', 'Bandung', '08976120012', 'zulzul@gmail.com', 'Zulpa', '$2a$10$.swIrskb3uaq1GiCMezAneMQ.QxZEj8NT3DxA2rtDR8tOh/e7rXXK', 1, '2025-12-23 07:22:21', '2025-12-23 07:22:21'),
(3, 'ADM-5348', 'Burhan1', 'Bogor', '089789209000', 'burhan@gmail.com', 'Hann', '$2a$10$uw2zWtFsGgbERzfdn3lcfe.M21TcAzBvrrRa7Q3MRM1bGd.JSKBV6', 1, '2025-12-23 12:42:30', '2025-12-23 12:42:30');

-- --------------------------------------------------------

--
-- Struktur dari tabel ` akun`
--

CREATE TABLE ` akun` (
  `idAkun` int(255) NOT NULL,
  `namaLengkap` varchar(255) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `NoTelepon` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `hashedPassword` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel ` mitra`
--

CREATE TABLE ` mitra` (
  `NamaLengkap` varchar(255) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `noTelepon` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `plainPassword` varchar(255) NOT NULL,
  `IdMitra` int(255) NOT NULL,
  `lokasiTugas` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel ` pengguna`
--

CREATE TABLE ` pengguna` (
  `IdKartu` varchar(2555) NOT NULL,
  `nomorKartu` varchar(255) NOT NULL,
  `saldoPoin` decimal(50,0) NOT NULL,
  `saldo` decimal(50,0) NOT NULL,
  `tanggalDaftar` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `penukaran`
--

CREATE TABLE `penukaran` (
  `idPenukaran` int(255) NOT NULL,
  `pengguna` varchar(255) NOT NULL,
  `poinDitukar` decimal(50,0) NOT NULL,
  `nilaiRupiah` decimal(50,0) NOT NULL,
  `keterangan` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel ` sampah`
--

CREATE TABLE ` sampah` (
  `idSampah` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `sampahanorganik`
--

CREATE TABLE `sampahanorganik` (
  `JenisSampah` varchar(255) NOT NULL,
  `PoinPerKg` decimal(50,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `sampahb3`
--

CREATE TABLE `sampahb3` (
  `JenisSampah` varchar(255) NOT NULL,
  `PoinPerKg` decimal(50,0) NOT NULL,
  `PetunjukPenanganan` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `sampahorganik`
--

CREATE TABLE `sampahorganik` (
  `JenisSampah` varchar(255) NOT NULL,
  `PoinPerKg` decimal(50,0) NOT NULL,
  `PerkiraanBusuk` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `titikkumpul`
--

CREATE TABLE `titikkumpul` (
  `idLokasi` varchar(255) NOT NULL,
  `NamaLokasi` varchar(255) NOT NULL,
  `Alamat` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksimitra`
--

CREATE TABLE `transaksimitra` (
  `IdTransaksi` varchar(255) NOT NULL,
  `titikPengumpulan` varchar(255) NOT NULL,
  `adminPencatat` varchar(255) NOT NULL,
  `namaMitraBisnis` varchar(255) NOT NULL,
  `kategoriSampahTerjual` varchar(255) NOT NULL,
  `beratTotalKg` decimal(50,0) NOT NULL,
  `nilaiRupiahDidapat` decimal(50,0) NOT NULL,
  `status` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksisampah`
--

CREATE TABLE `transaksisampah` (
  `idTransaksi` varchar(255) NOT NULL,
  `pengguna` varchar(255) NOT NULL,
  `mitra` varchar(255) NOT NULL,
  `lokasi` varchar(255) NOT NULL,
  `sampah` varchar(255) NOT NULL,
  `beratKg` decimal(50,0) NOT NULL,
  `totalPoinDidapat` decimal(50,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_akun`),
  ADD UNIQUE KEY `id_admin` (`id_admin`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `admin`
--
ALTER TABLE `admin`
  MODIFY `id_akun` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
