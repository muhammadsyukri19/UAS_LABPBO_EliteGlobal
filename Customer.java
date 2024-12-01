import java.util.ArrayList;
import java.util.List;

class Customer extends Akun {
    private Keranjang keranjang;
    private List<Transaksi> historiTransaksi;

    public Customer(String id, String password) {
        super(id, password);
        this.keranjang = new Keranjang();
        this.historiTransaksi = new ArrayList<>();
        loadSavedCart();
    }

    private void loadSavedCart() {
        List<Barang> savedItems = FileHandler.loadCart(this.getId());
        for (Barang item : savedItems) {
            this.keranjang.tambahBarang(item);
        }
    }

    public Keranjang getKeranjang() {
        return keranjang;
    }

    public void tambahKeranjang(Barang barang, int jumlah) {
        for (int i = 0; i < jumlah; i++) {
            keranjang.tambahBarang(barang);
        }
        FileHandler.saveCart(this.getId(), keranjang.barang);
        System.out.println("Barang berhasil ditambahkan ke keranjang!");
    }

    public void lihatKeranjang() {
        keranjang.lihatKeranjang();
    }

    public void checkout(Pembayaran pembayaran, List<Transaksi> listTransaksi, ListBarang listBarang) {
        if (keranjang.barang.isEmpty()) {
            System.out.println("Keranjang kosong!");
            return;
        }

        Transaksi transaksi = new Transaksi(this, new ArrayList<>(keranjang.barang), pembayaran);
        transaksi.prosesPembayaran();
        transaksi.cetakResi();
        
        listTransaksi.add(transaksi);
        historiTransaksi.add(transaksi);
        keranjang.barang.clear();
        FileHandler.clearCart(this.getId());
    }

    public void lihatHistori() {
        if (historiTransaksi.isEmpty()) {
            System.out.println("Belum ada histori transaksi");
            return;
        }

        System.out.println("\nHistori Transaksi:");
        for (Transaksi transaksi : historiTransaksi) {
            System.out.println("-----------------------");
            System.out.println("ID Transaksi: " + transaksi.IDTransaksi);
            System.out.println("Total: Rp." + transaksi.getTotalHarga());
            System.out.println("Metode Pembayaran: " + transaksi.pembayaran);
        }
    }

    public void displayBankOptions() {
        System.out.println("Pilih Bank:");
        System.out.println("1. BSI");
        System.out.println("2. Mandiri");
        System.out.println("3. Bank Aceh");
        System.out.println("4. BCA");
        System.out.print("Masukkan pilihan: ");
    }

    @Override
    public String toString() {
        return String.format("Customer [ID: %s, Role: Customer]", getId());
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean login(String id, String password) {
        String hashedInputPassword = FileHandler.hashPassword(password);
        return this.id.equals(id) && this.password.equals(hashedInputPassword);
    }
}
