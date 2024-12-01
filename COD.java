class COD extends Pembayaran {

    @Override
    void prosesPembayaran() {
        System.out.println("Pembayaran tunai (Cash on Delivery/COD) sedang diproses...");
    }

    @Override
    public String toString() {
        return "Metode Pembayaran: COD";
    }
}
