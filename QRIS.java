class QRIS extends Pembayaran {

    @Override
    void prosesPembayaran() {
        System.out.println("Pembayaran diproses menggunakan QRIS...");
        showLoadingAnimation("Memproses pembayaran", 100);
        tampilkanBarcode();
    }

    public String toString() {
        return "Metode Pembayaran: QRIS";
    }

    private void tampilkanBarcode() {
        // Simulasi tampilan barcode dengan karakter ASCII
        System.out.println("\n\nQRIS Barcode:");
        System.out.println("#########################");
        System.out.println("#                       #");
        System.out.println("#   ||||||||||||||||||  #");
        System.out.println("#   |||   |||   |||     #");
        System.out.println("#   |||   |||   |||     #");
        System.out.println("#   ||||||||||||||||||  #");
        System.out.println("#                       #");
        System.out.println("#########################");
    }

    private void showLoadingAnimation(String message, int totalSteps) {
        System.out.println(message + ", harap tunggu...");

        String[] spinner = {"|", "/", "-", "\\"};
        try {
            for (int i = 0; i <= totalSteps; i++) {
                // Simulasi loading (300ms dibagi rata untuk setiap langkah)
                System.out.print("\r" + message + " " + spinner[i % spinner.length] + " " + i + "%");
                Thread.sleep(15); // Delay 15ms per langkah
            }
            System.out.println();  // Pindah baris setelah loading selesai
        } catch (InterruptedException e) {
            System.out.println("Proses loading terganggu.");
        }
    }

}
