import java.util.List;
import java.util.Scanner;

class CustomerDriver extends Driver {
    private final Customer customer;
    private final Scanner scanner;
    
    public CustomerDriver(Customer customer, Transaksi transaksi, ListBarang barang) {
        this.customer = customer;
        this.scanner = new Scanner(System.in);
    }

    @Override
    void Menu(ListBarang listBarang, List<Transaksi> listTransaksi) {
        boolean isLoggedIn = true;

        while (isLoggedIn) {
            displayMenu();
            int option = getUserInput();

            switch (option) {
                case 1 -> listBarang.lihatBarang();
                case 2 -> handleAddToCart(listBarang);
                case 3 -> customer.lihatKeranjang();
                case 4 -> handleCheckout(listTransaksi, listBarang);
                case 5 -> customer.lihatHistori();
                case 6 -> {
                    isLoggedIn = false;
                    System.out.println("Kembali ke Menu Utama...");
                }
                default -> System.out.println("Opsi tidak valid.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n+--------------------------------+");
        System.out.println("|\t   MENU CUSTOMER\t |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Lihat List Barang\t\t |");
        System.out.println("| 2. Masukkan Barang ke Keranjang|");
        System.out.println("| 3. Lihat Keranjang\t\t |");
        System.out.println("| 4. Checkout\t\t\t |");
        System.out.println("| 5. Lihat History Belanja\t |");
        System.out.println("| 6. Kembali ke menu utama\t |");
        System.out.println("+--------------------------------+");
    }

    private int getUserInput() {
        System.out.print("Pilih opsi: ");
        return scanner.nextInt();
    }

    private void handleAddToCart(ListBarang listBarang) {
        System.out.print("Masukkan ID barang untuk ditambahkan ke keranjang: ");
        String idBarang = scanner.next();
        System.out.print("Masukkan jumlah barang: ");
        int jumlahBarang = scanner.nextInt();

        Barang barangPilihan = listBarang.barang.stream()
                .filter(barang -> barang.getId().equals(idBarang))
                .findFirst()
                .orElse(null);

        if (barangPilihan != null) {
            if (barangPilihan.getStok() >= jumlahBarang) {
                customer.tambahKeranjang(barangPilihan, jumlahBarang);
                barangPilihan.setStok(barangPilihan.getStok() - jumlahBarang);
            } else {
                System.out.println("Stok tidak mencukupi!");
            }
        } else {
            System.out.println("Produk dengan ID tersebut tidak ditemukan!");
        }
    }

    private void handleCheckout(List<Transaksi> listTransaksi, ListBarang listBarang) {
        System.out.println("Pilih metode pembayaran:");
        System.out.println("1. QRIS");
        System.out.println("2. Bank");
        System.out.println("3. COD");
        System.out.print("Masukkan pilihan: ");
        int pembayaranOption = scanner.nextInt();

        Pembayaran pembayaran = switch (pembayaranOption) {
            case 1 -> new QRIS();
            case 2 -> handleBankPayment();
            case 3 -> new COD();
            default -> {
                System.out.println("Pilihan metode pembayaran tidak valid.");
                yield null;
            }
        };

        if (pembayaran != null) {
            customer.checkout(pembayaran, listTransaksi, listBarang);
        }
    }

    private Pembayaran handleBankPayment() {
        customer.displayBankOptions();
        int bankOption = scanner.nextInt();
        return switch (bankOption) {
            case 1 -> new Bank("BSI");
            case 2 -> new Bank("Mandiri");
            case 3 -> new Bank("Bank Aceh");
            case 4 -> new Bank("BCA");
            default -> {
                System.out.println("Pilihan bank tidak valid.");
                yield null;
            }
        };
    }
}
