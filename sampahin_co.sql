-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 20, 2025 at 10:20 AM
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
-- Database: `sampahin.co`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `idAdmin` varchar(255) NOT NULL,
  `Nama_lengkap` varchar(255) NOT NULL,
  `Alamat` varchar(255) NOT NULL,
  `No_Telepon` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `hashed_Password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`idAdmin`, `Nama_lengkap`, `Alamat`, `No_Telepon`, `Email`, `Username`, `hashed_Password`) VALUES
('24006759', 'Iqbal Imanudin', 'jl.bahagia 22', '0895602480504', 'iqbalbaik@gmail.com', 'ball', 'iqbal678\r\n'),
('24007854', 'I Gede Dio Devan Satria Prawira', 'jl.baru 20', '085156461257', 'DIO@gmail.com', 'diosans@gmail.com', 'DIO2345\r\n'),
('24010203', 'Muhammad Haikal Alfaridzi', 'jl.veteran 2', '082122345578', 'haikal@gmail.com', 'haikal', 'ekal12345'),
('24045243', 'Burhan Abdur Rahman', 'jl.mawar 12', '089501901000', 'Burhan@gmail.com', 'hann12', 'burhan12345'),
('24807135', 'Zulfa Nurlatifah', 'jl.Mawar 15', '083137367695', 'Zulfa@gmail.com', 'Zull', 'zulfa567');

-- --------------------------------------------------------

--
-- Table structure for table `mitra`
--

CREATE TABLE `mitra` (
  `Nama_Lengkap` varchar(255) NOT NULL,
  `Alamat` varchar(255) NOT NULL,
  `No_Telepon` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Hashe_Password` varchar(255) NOT NULL,
  `idMitra` int(255) NOT NULL,
  `Lokasi_Tugas` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mitra`
--

INSERT INTO `mitra` (`Nama_Lengkap`, `Alamat`, `No_Telepon`, `Email`, `Username`, `Hashe_Password`, `idMitra`, `Lokasi_Tugas`) VALUES
('pedulisekitar', 'tangerang,jl.konoha, no.15', '082172649671', 'pedulisekitar@gmail.com', 'pedulisekitar', 'pedulidulu223344', 29583750, 'Jl. Veteran No.8, Purwakarta, Jawa Barat, 41115.');

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `idKartu` varchar(255) NOT NULL,
  `Nomor_Kartu` varchar(255) NOT NULL,
  `Saldo_Poin` decimal(50,0) NOT NULL,
  `Saldo_Rupiah` decimal(50,0) NOT NULL,
  `Tanggal_Daftar` timestamp(6) NOT NULL DEFAULT current_timestamp(6) ON UPDATE current_timestamp(6),
  `Nama_Lengkap` varchar(255) NOT NULL,
  `Alamat` varchar(100) NOT NULL,
  `No_Telepon` varchar(15) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Hashed_Password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`idKartu`, `Nomor_Kartu`, `Saldo_Poin`, `Saldo_Rupiah`, `Tanggal_Daftar`, `Nama_Lengkap`, `Alamat`, `No_Telepon`, `Email`, `Username`, `Hashed_Password`) VALUES
('10475835', '1', 50000, 50000, '2025-11-20 08:57:15.199038', 'Randi Kurniawan', 'jl.melati, no.17', '082146583346', 'Randitampan@gmail.com', 'randkurn', 'randilekbong123');

-- --------------------------------------------------------

--
-- Table structure for table `penukaran`
--

CREATE TABLE `penukaran` (
  `idPengguna` varchar(255) NOT NULL,
  `Poin_Ditukar` decimal(50,0) NOT NULL,
  `Nilai_Rupiah` decimal(50,0) NOT NULL,
  `Keterangan` varchar(255) NOT NULL,
  `Status` varchar(255) NOT NULL,
  `Timestap` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penukaran`
--

INSERT INTO `penukaran` (`idPengguna`, `Poin_Ditukar`, `Nilai_Rupiah`, `Keterangan`, `Status`, `Timestap`) VALUES
('10475835', 20000, 20000, 'telah menukar poin sebanyak 20.000 poin', '', '2025-11-20 08:59:59');

-- --------------------------------------------------------

--
-- Table structure for table `sampah`
--

CREATE TABLE `sampah` (
  `idSampah` varchar(255) NOT NULL,
  `Jenis_Sampah` text NOT NULL,
  `Harga_Poin_Per_Kg` decimal(50,0) NOT NULL,
  `Kategori` text NOT NULL,
  `Perkiraan_Busuk` datetime(6) NOT NULL,
  `Petunjuk_Penanganan` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sampah`
--

INSERT INTO `sampah` (`idSampah`, `Jenis_Sampah`, `Harga_Poin_Per_Kg`, `Kategori`, `Perkiraan_Busuk`, `Petunjuk_Penanganan`) VALUES
('30857457', 'kulit buah', 100, 'organik', '2025-11-29 16:03:15.000000', '-');

-- --------------------------------------------------------

--
-- Table structure for table `titik_kumpul`
--

CREATE TABLE `titik_kumpul` (
  `idLokasi` varchar(255) NOT NULL,
  `Nama_Lokasi` varchar(255) NOT NULL,
  `Alamat_Lokasi` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `titik_kumpul`
--

INSERT INTO `titik_kumpul` (`idLokasi`, `Nama_Lokasi`, `Alamat_Lokasi`) VALUES
('10574956\r\n\r\n', 'UPI Purwakarta', 'jl. Veteran No.8, Purwakarta, Jawa Barat, 41115\r\n\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi_mitra`
--

CREATE TABLE `transaksi_mitra` (
  `IdTransaksi` varchar(255) NOT NULL,
  `idAdmin` varchar(255) NOT NULL,
  `Nama_Mitra_Bisnis` varchar(255) NOT NULL,
  `Kategori_Sampah_Terjual` varchar(255) NOT NULL,
  `Berat_Total_Kg` decimal(5,0) NOT NULL,
  `Nilai_Rupiah_Didapat` decimal(5,0) NOT NULL,
  `Timestap` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi_mitra`
--

INSERT INTO `transaksi_mitra` (`IdTransaksi`, `idAdmin`, `Nama_Mitra_Bisnis`, `Kategori_Sampah_Terjual`, `Berat_Total_Kg`, `Nilai_Rupiah_Didapat`, `Timestap`) VALUES
('40678292', '29583750\r\n', 'pedulisekitar', 'Organik', 100, 10000, '2025-11-20 09:20:30');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi_sampah`
--

CREATE TABLE `transaksi_sampah` (
  `idPengguna` varchar(255) NOT NULL,
  `idMitra` varchar(255) NOT NULL,
  `idLokasi` varchar(255) NOT NULL,
  `idSampah` varchar(255) NOT NULL,
  `Total_Poin_Didapat` decimal(50,0) NOT NULL,
  `Berat_Kg` decimal(50,0) NOT NULL,
  `Timestap` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi_sampah`
--

INSERT INTO `transaksi_sampah` (`idPengguna`, `idMitra`, `idLokasi`, `idSampah`, `Total_Poin_Didapat`, `Berat_Kg`, `Timestap`) VALUES
('10475835\r\n', '29583750\r\n', '10574956\r\n\r\n', '30857457', 20000, 200, '2025-11-20 09:17:40');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`idAdmin`);

--
-- Indexes for table `mitra`
--
ALTER TABLE `mitra`
  ADD PRIMARY KEY (`idMitra`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`idKartu`);

--
-- Indexes for table `penukaran`
--
ALTER TABLE `penukaran`
  ADD PRIMARY KEY (`idPengguna`);

--
-- Indexes for table `sampah`
--
ALTER TABLE `sampah`
  ADD PRIMARY KEY (`idSampah`);

--
-- Indexes for table `titik_kumpul`
--
ALTER TABLE `titik_kumpul`
  ADD PRIMARY KEY (`idLokasi`);

--
-- Indexes for table `transaksi_mitra`
--
ALTER TABLE `transaksi_mitra`
  ADD PRIMARY KEY (`IdTransaksi`);

--
-- Indexes for table `transaksi_sampah`
--
ALTER TABLE `transaksi_sampah`
  ADD PRIMARY KEY (`idLokasi`),
  ADD UNIQUE KEY `idPengguna` (`idPengguna`,`idMitra`,`idSampah`),
  ADD UNIQUE KEY `idLokasi` (`idLokasi`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
