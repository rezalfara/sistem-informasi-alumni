package com.example.sisteminformasialumni;

public class Db_Contract {

    public static String ip = "10.140.177.48";
    public static final String urlRegister = "http://"+ip+"/project/api-register.php";
    public static final String urlRegisterAlumni = "http://"+ip+"/project/api-register-alumni.php";
    public static final String urlLogin = "http://"+ip+"/project/api-login.php";
    public static final String urlLoginAlumni = "http://"+ip+"/project/api-login-alumni.php";
    public static final String urlCreate = "http://"+ip+"/project/api-create-data.php";
    public static final String urlRead = "http://"+ip+"/project/api-read-data.php";
    public static final String urlReadById = "http://"+ip+"/project/api-read-data-by-id.php?id_alumni=";
    public static final String urlUpdate = "http://"+ip+"/project/api-update-data.php";
    public static final String urlDelete = "http://"+ip+"/project/api-delete-data.php";
    public static final String pathImage = "http://"+ip+"/project/img/";
    public static final String urlGetJurusan = "http://"+ip+"/project/api-get-jurusan.php";
    public static final String urlGetJurusanById = "http://"+ip+"/project/api-read-jurusan-by-id.php?id_jurusan=";
    public static final String urlGetTahunLulus = "http://"+ip+"/project/api-get-tahunLulus.php";
    public static final String urlGetTahunLulusById = "http://"+ip+"/project/api-read-tahunLulus-by-id.php?id_tahun_lulus=";

}
