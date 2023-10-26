<?php
include 'koneksi.php';

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query_read = "SELECT alumni.npm, alumni.nama, alumni.tempat_lahir, alumni.tgl_lahir, alumni.jk, alumni.email, alumni.no_hp, alumni.alamat, alumni.foto, jurusan.nama_jurusan, tahun_lulus.tahun_lulus FROM alumni JOIN jurusan ON alumni.id_jurusan = jurusan.id_jurusan JOIN tahun_lulus ON alumni.id_tahun_lulus = tahun_lulus.id_tahun_lulus;";
    $result = mysqli_query($koneksi, $query_read);

    if (mysqli_num_rows($result) > 0) {
        $data = array();
        while ($row = mysqli_fetch_assoc($result)) {
            $data[] = $row;
        }
        echo json_encode($data);
    } else {
        echo json_encode(array("message" => "Data alumni tidak ditemukan"));
    }
} else {
    echo json_encode(array("message" => "Metode permintaan tidak valid"));
}
?>



<!-- //include 'koneksi.php'; -->

<!-- $query_read = "SELECT * FROM alumni";
$result = mysqli_query($koneksi, $query_read);

if (mysqli_num_rows($result) > 0) {
    $data = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = $row;
    }
    echo json_encode($data);
} else {
    echo json_encode(array("message" => "Data tidak ditemukan"));
}
?> -->
