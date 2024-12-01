class COD extends Pembayaran {

    @Override
    void prosesPembayaran() {
        System.out.print("Memproses pembayaran...");
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(500); // Delay 500 ms
                System.out.print(".");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\nPembayaran tunai (Cash on Delivery/COD) sedang diproses...");
    }

    @Override
    public String toString() {
        return "Metode Pembayaran: COD";
    }
}
