<?php

include 'koneksi.php';

$username = $_POST['username'];
$password = $_POST['password'];
$alamat = $_POST['alamat'];

$query_register = "SELECT * FROM admin WHERE username = '".$username."'";

$msql = mysqli_query($koneksi, $query_register);

$result = mysqli_num_rows($msql);

if (!empty($username) && !empty($password) && !empty($alamat)) {
    if ($result == 0) {
        $regis = "INSERT INTO admin (username, password, alamat) VALUES ('$username', '$password', '$alamat')";

        $msql_regis = mysqli_query($koneksi, $regis);

        echo "Daftar Berhasil";
    }else {
        echo "Username Sudah Digunakan";
    }
}else {
    echo "Semua Data Harus Diisi Lengkap";
}

?>