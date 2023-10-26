<?php

include 'koneksi.php';

$npm = $_POST['npm'];
$nama = $_POST['nama'];
$tempat_lahir = $_POST['tempat_lahir'];
$tgl_lahir = $_POST['tgl_lahir'];
$jk = $_POST['jk'];
$email = $_POST['email'];
$no_hp = $_POST['no_hp'];
$alamat = $_POST['alamat'];
$foto = $_POST['foto'];
$id_tahun_lulus = $_POST['id_tahun_lulus'];
$id_jurusan = $_POST['id_jurusan'];

$query_create = "SELECT * FROM alumni WHERE npm = '".$npm."'";

$msql = mysqli_query($koneksi, $query_create);

$result = mysqli_num_rows($msql);

if (!empty($npm) && !empty($nama) && !empty($tempat_lahir) && !empty($tgl_lahir) && !empty($jk) && !empty($email) && !empty($no_hp) && !empty($alamat) && !empty($foto) && !empty($id_tahun_lulus) && !empty($id_jurusan)) {
    if ($result == 0) {
        $create_alumni = "INSERT INTO alumni (npm, nama, tempat_lahir, tgl_lahir, jk, email, no_hp, alamat, foto, id_tahun_lulus, id_jurusan) 
                    VALUES ('$npm', '$nama', '$tempat_lahir', '$tgl_lahir', '$jk', '$email', '$no_hp', '$alamat', '$foto', '$id_tahun_lulus', '$id_jurusan')";

        $msql_create = mysqli_query($koneksi, $create_alumni);

        echo "Tambah Data Berhasil";
    }else {
        echo "NPM telah terdaftar";
    }
}else {
    echo "Semua Data Harus Diisi Lengkap";
}

?>





//<?php
//include 'koneksi.php';

//if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  //  $data = json_decode(file_get_contents("php://input"), true);
//
  //  $npm = $data['npm'];
    //$nama = $data['nama'];
//    $tempat_lahir = $data['tempat_lahir'];
  //  $tgl_lahir = $data['tgl_lahir'];
    //$email = $data['email'];
    //$no_hp = $data['no_hp'];
    //$alamat = $data['alamat'];
    // $foto = $data['foto'];
    // $id_tahun_lulus = $data['id_tahun_lulus'];
    // $id_jurusan = $data['id_jurusan'];

    // $query_create = "INSERT INTO alumni (npm, nama, tempat_lahir, tgl_lahir, email, no_hp, alamat, foto, id_tahun_lulus, id_jurusan) 
//                     VALUES ('$npm', '$nama', '$tempat_lahir', '$tgl_lahir', '$email', '$no_hp', '$alamat', '$foto', '$id_tahun_lulus', '$id_jurusan')";

//     $result = mysqli_query($koneksi, $query_create);

//     if ($result) {
//         echo json_encode(array("message" => "Data alumni berhasil ditambahkan"));
//     } else {
//         echo json_encode(array("message" => "Gagal menambahkan data alumni"));
//     }
// } else {
//     echo json_encode(array("message" => "Metode permintaan tidak valid"));
// }
