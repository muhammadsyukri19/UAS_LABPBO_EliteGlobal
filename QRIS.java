class QRIS extends Pembayaran {
    @Override
    void prosesPembayaran() {
        System.out.println("Pembayaran diproses menggunakan QRIS...");
    }

    public String toString() {
        return "Metode Pembayaran: QRIS";
    }
}
