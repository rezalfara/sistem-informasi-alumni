package com.example.sisteminformasialumni;

public class Db_Contract {
    public static String ip = "10.115.230.207";
    public static final String urlRegister = "http://"+ip+"/project/api-register.php";
    public static final String urlRegisterAlumni = "http://"+ip+"/project/api-register-alumni.php";
    public static final String urlLogin = "http://"+ip+"/project/api-login.php";
    public static final String urlLoginAlumni = "http://"+ip+"/project/api-login-alumni.php";
    public static final String urlCreate = "http://"+ip+"/project/api-create-data.php";
    public static final String urlRead = "http://"+ip+"/project/api-read-data.php";
    public static final String urlReadAdmin = "http://"+ip+"/project/api-read-data-admin.php";
    public static final String urlReadById = "http://"+ip+"/project/api-read-data-by-id.php?id_alumni=";
    public static final String urlUpdate = "http://"+ip+"/project/api-update-data.php";
    public static final String urlUpdateAdmin = "http://"+ip+"/project/api-update-data-admin.php";
    public static final String urlDelete = "http://"+ip+"/project/api-delete-data.php";
    public static final String pathImage = "http://"+ip+"/project/img/";
    public static final String pathImageAdmin = "http://"+ip+"/project/imgAdmin/";
    public static final String urlGetJurusan = "http://"+ip+"/project/api-get-jurusan.php";
    public static final String urlGetJurusanById = "http://"+ip+"/project/api-read-jurusan-by-id.php?id_jurusan=";
    public static final String urlGetTahunLulus = "http://"+ip+"/project/api-get-tahunLulus.php";
    public static final String urlGetTahunLulusById = "http://"+ip+"/project/api-read-tahunLulus-by-id.php?id_tahun_lulus=";
    public static final String urlGetPasswordByNpm = "http://"+ip+"/project/api-get-password-by-npm.php?npm=";
    public static final String urlGetPasswordByUsername = "http://"+ip+"/project/api-get-password-by-username.php?username=";
    public static final String urlUpdatePasswordByNpm = "http://"+ip+"/project/api-update-password.php";
    public static final String urlUpdatePasswordByUsername = "http://"+ip+"/project/api-update-password-admin.php";

}
