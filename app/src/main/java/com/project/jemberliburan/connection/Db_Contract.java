package com.project.jemberliburan.connection;

public class Db_Contract {

    // IP atau base URL dari API
    public static String baseUrl = "https://sijeli.my.id/Jeli_API/";


    // Definisikan URL untuk setiap endpoint
    public static final String urlRegisterActivity = baseUrl + "api-register.php";
    public static final String urlLoginActivity = baseUrl + "api-login.php";
    public static final String urlSendVerificationCode = baseUrl + "send-verification-code.php";
    public static final String urlResetPassword = baseUrl + "reset-password.php"; // Tambahkan URL reset password
    public static final String urlVerifyCode = baseUrl + "verify-code.php";

    public static final String urlUpdateUsers = baseUrl + "api-update-users.php";

    public static final String urlCekTiket = baseUrl + "cek-tiket.php";
    public static final String urlRiwayatTiket = baseUrl + "riwayat-tiket.php";

    public static final String urlAddReview = baseUrl + "add-review.php";     // Endpoint untuk menambahkan ulasan
    public static final String urlGetReviews = baseUrl + "get-reviews.php";   // Endpoint untuk mengambil ulasan
    public static final String urlGetReviewsByUser = baseUrl + "get-user-reviews.php"; // Endpoint untuk mengambil riwayat ulasan pengguna
}
