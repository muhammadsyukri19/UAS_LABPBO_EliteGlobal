class Bank extends Pembayaran {
    private String bankName;

    Bank(String namaBank) {
        this.bankName = namaBank;
    }

    @Override
    void prosesPembayaran() {
        System.out.println("Pembayaran sedang diproses melalui " + bankName + "...");
    }

    @Override
    public String toString() {
        return "Pembayaran melalui " + bankName;
    }
}