-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 14, 2025 at 01:42 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sampahin`
--

-- --------------------------------------------------------

--
-- Table structure for table ` admin`
--

CREATE TABLE ` admin` (
  `idAdmin` varchar(255) NOT NULL,
  `namaLengkap` varchar(255) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `noTelepon` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `plainPassword` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table ` akun`
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
-- Table structure for table ` mitra`
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
-- Table structure for table ` pengguna`
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
-- Table structure for table `penukaran`
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
-- Table structure for table ` sampah`
--

CREATE TABLE ` sampah` (
  `idSampah` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sampahanorganik`
--

CREATE TABLE `sampahanorganik` (
  `JenisSampah` varchar(255) NOT NULL,
  `PoinPerKg` decimal(50,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sampahb3`
--

CREATE TABLE `sampahb3` (
  `JenisSampah` varchar(255) NOT NULL,
  `PoinPerKg` decimal(50,0) NOT NULL,
  `PetunjukPenanganan` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sampahorganik`
--

CREATE TABLE `sampahorganik` (
  `JenisSampah` varchar(255) NOT NULL,
  `PoinPerKg` decimal(50,0) NOT NULL,
  `PerkiraanBusuk` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `titikkumpul`
--

CREATE TABLE `titikkumpul` (
  `idLokasi` varchar(255) NOT NULL,
  `NamaLokasi` varchar(255) NOT NULL,
  `Alamat` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transaksimitra`
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
-- Table structure for table `transaksisampah`
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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


insert into admin values ('1','alex','jl.ambaroni','12334','alex@gmail.com','aa alex','112233');