<?php
include 'koneksi.php';

if ($_SERVER['REQUEST_METHOD'] === 'PUT') {
    $data = json_decode(file_get_contents("php://input"), true);

    $id = $data['id']; // ID alumni yang akan diubah
    $npm = $data['npm'];
    $nama = $data['nama'];
    $tempat_lahir = $data['tempat_lahir'];
    $tgl_lahir = $data['tgl_lahir'];
    $email = $data['email'];
    $no_hp = $data['no_hp'];
    $alamat = $data['alamat'];
    $foto = $data['foto'];
    $id_tahun_lulus = $data['id_tahun_lulus'];
    $id_jurusan = $data['id_jurusan'];

    $query_update = "UPDATE alumni 
                    SET npm = '$npm', 
                        nama = '$nama', 
                        tempat_lahir = '$tempat_lahir', 
                        tgl_lahir = '$tgl_lahir', 
                        email = '$email', 
                        no_hp = '$no_hp', 
                        alamat = '$alamat', 
                        foto = '$foto', 
                        id_tahun_lulus = '$id_tahun_lulus', 
                        id_jurusan = '$id_jurusan' 
                    WHERE id = $id";

    $result = mysqli_query($koneksi, $query_update);

    if ($result) {
        echo json_encode(array("message" => "Data alumni berhasil diperbarui"));
    } else {
        echo json_encode(array("message" => "Gagal memperbarui data alumni"));
    }
} else {
    echo json_encode(array("message" => "Metode permintaan tidak valid"));
}
?>
