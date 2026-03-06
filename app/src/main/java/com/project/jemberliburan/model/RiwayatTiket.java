package com.project.jemberliburan.model;

public class RiwayatTiket {
    private String orderId;
    private String destinasi;
    private String email;
    private String tanggalKunjungan;
    private int jumlahTiket;
    private String totalBayar;
    private String statusTiket;
    private String paymentUrl;

    public RiwayatTiket(String orderId, String destinasi, String email, String tanggalKunjungan, int jumlahTiket, String totalBayar, String statusTiket, String paymentUrl) {
        this.orderId = orderId;
        this.destinasi = destinasi;
        this.email = email;
        this.tanggalKunjungan = tanggalKunjungan;
        this.jumlahTiket = jumlahTiket;
        this.totalBayar = totalBayar;
        this.statusTiket = statusTiket;
        this.paymentUrl = paymentUrl;
    }

    // Getter dan Setter

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDestinasi() {
        return destinasi;
    }

    public void setDestinasi(String destinasi) {
        this.destinasi = destinasi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTanggalKunjungan() {
        return tanggalKunjungan;
    }

    public void setTanggalKunjungan(String tanggalKunjungan) {
        this.tanggalKunjungan = tanggalKunjungan;
    }

    public int getJumlahTiket() {
        return jumlahTiket;
    }

    public void setJumlahTiket(int jumlahTiket) {
        this.jumlahTiket = jumlahTiket;
    }

    public String getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(String totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getStatusTiket() {
        return statusTiket;
    }

    public void setStatusTiket(String statusTiket) {
        this.statusTiket = statusTiket;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
