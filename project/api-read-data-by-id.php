<?php
include 'koneksi.php';

// Ambil ID dari URL
$alumniNpm = $_GET['npm'];

// Query SQL untuk mengambil data berdasarkan ID
$sql = "SELECT alumni.npm, alumni.nama, alumni.tempat_lahir, alumni.tgl_lahir, alumni.jk, alumni.email, alumni.no_hp, alumni.alamat, alumni.foto, jurusan.nama_jurusan, tahun_lulus.tahun_lulus FROM alumni JOIN jurusan ON alumni.id_jurusan = jurusan.id_jurusan JOIN tahun_lulus ON alumni.id_tahun_lulus = tahun_lulus.id_tahun_lulus WHERE npm = $alumniNpm;";

$result = $koneksi->query($sql);

if ($result->num_rows > 0) {
    // Mengembalikan data dalam format JSON
    $row = $result->fetch_assoc();
    echo json_encode($row);
} else {
    // Jika data tidak ditemukan
    echo "Data tidak ditemukan";
}

$koneksi->close();
?>
