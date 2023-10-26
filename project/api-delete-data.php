<?php
include 'koneksi.php';

if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    $data = json_decode(file_get_contents("php://input"), true);

    $id = $data['id']; // ID alumni yang akan dihapus

    $query_delete = "DELETE FROM alumni WHERE id = $id";
    $result = mysqli_query($koneksi, $query_delete);

    if ($result) {
        echo json_encode(array("message" => "Data alumni berhasil dihapus"));
    } else {
        echo json_encode(array("message" => "Gagal menghapus data alumni"));
    }
} else {
    echo json_encode(array("message" => "Metode permintaan tidak valid"));
}
