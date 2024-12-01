import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

class AdminDriver extends Driver {
    private final Scanner scanner;

    public AdminDriver(ListBarang listBarang, List<Transaksi> listTransaksi) {
        this.scanner = new Scanner(System.in);
    }

    @Override
    void Menu(ListBarang listBarang, List<Transaksi> listTransaksi) {
        boolean isManagingBarang = true;

        while (isManagingBarang) {
            displayMenu();
            int option = getUserInput();
            
            switch (option) {
                case 1 -> listBarang.lihatBarang();
                case 2 -> adminTambahBarang(listBarang);
                case 3 -> {
                    listBarang.lihatBarang();
                    editBarang(listBarang);
                }
                case 4 -> hapusBarang(listBarang);
                case 5 -> lihatTransaksi(listTransaksi);
                case 6 -> {
                    isManagingBarang = false;
                    System.out.println("Kembali ke Menu Utama...");
                }
                default -> System.out.println("Opsi yang anda masukkan tidak valid!");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n+--------------------------------+");
        System.out.println("|\t   MENU ADMIN\t\t |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Lihat Daftar Barang\t |");
        System.out.println("| 2. Tambah Barang\t\t |");
        System.out.println("| 3. Edit Barang\t\t |");
        System.out.println("| 4. Hapus Barang\t\t |");
        System.out.println("| 5. Lihat List Transaksi\t |");
        System.out.println("| 6. Kembali ke Menu Utama\t |");
        System.out.println("+--------------------------------+");
    }

    private int getUserInput() {
        System.out.print("Masukkan Pilihan: ");
        return scanner.nextInt();
    }

    private void adminTambahBarang(ListBarang listBarang) {
        System.out.print("Masukkan ID Barang: ");
        String idBarang = scanner.next();
        scanner.nextLine(); 

        if (listBarang.jikaBarangAda(idBarang)) {
            System.out.printf("Barang dengan ID %s sudah ada dalam daftar! Barang gagal ditambahkan.%n", idBarang);
        } else {
            System.out.print("Masukkan Nama Barang: ");
            String namaBarang = scanner.nextLine();
            System.out.print("Masukkan Harga Barang: ");
            int hargaBarang = scanner.nextInt();
            System.out.print("Masukkan Jumlah Barang/stok: ");
            int jumlahBarang = scanner.nextInt();

            Barang barang = new Barang(idBarang, namaBarang, hargaBarang, jumlahBarang);
            listBarang.tambahBarang(barang);
            
            // Tambahkan ke file products.txt
            try {
                FileHandler.appendProductToFile(barang);
                System.out.println("Barang berhasil ditambahkan ke dalam daftar dan disimpan ke file.");
            } catch (Exception e) {
                System.out.println("Gagal menyimpan barang ke file: " + e.getMessage());
            }
        }
    }

    private void editBarang(ListBarang listBarang) {
        System.out.print("Masukkan ID Barang yang ingin diedit: ");
        String idBarang = scanner.next();
        scanner.nextLine();
        Barang barangToEdit = getProductById(listBarang, idBarang);

        if (barangToEdit != null) {
            System.out.print("Masukkan Nama Baru Barang: ");
            String namaBarangBaru = scanner.nextLine();
            System.out.print("Masukkan Harga Baru Barang: ");
            int hargaBarangBaru = scanner.nextInt();
            System.out.print("Masukkan Stok Baru Barang: ");
            int stokBaru = scanner.nextInt();

            barangToEdit.nama = namaBarangBaru;
            barangToEdit.harga = hargaBarangBaru;
            barangToEdit.stok = stokBaru;

            // Update file products.txt
            try {
                // Tulis ulang semua produk ke file
                PrintWriter writer = new PrintWriter(new FileWriter("products.txt"));
                for (Barang barang : listBarang.barang) {
                    writer.printf("%s,%s,%d,%d%n",
                        barang.getId(),
                        barang.getNama(),
                        barang.getHarga(),
                        barang.getStok());
                }
                writer.close();
                System.out.println("Barang berhasil diedit!");
            } catch (IOException e) {
                System.out.println("Error updating products file: " + e.getMessage());
            }
        } else {
            System.out.println("ID Barang tidak ditemukan!");
        }
    }

    private void hapusBarang(ListBarang listBarang) {
        System.out.print("Masukkan ID Barang yang ingin dihapus: ");
        String idBarang = scanner.next();
        Barang barangToDelete = getProductById(listBarang, idBarang);

        if (barangToDelete != null) {
            listBarang.barang.remove(barangToDelete);
            
            // Update file products.txt
            try {
                // Tulis ulang semua produk ke file
                PrintWriter writer = new PrintWriter(new FileWriter("products.txt"));
                for (Barang barang : listBarang.barang) {
                    writer.printf("%s,%s,%d,%d%n",
                        barang.getId(),
                        barang.getNama(),
                        barang.getHarga(),
                        barang.getStok());
                }
                writer.close();
                System.out.println("Barang berhasil dihapus!");
            } catch (IOException e) {
                System.out.println("Error updating products file: " + e.getMessage());
            }
        } else {
            System.out.println("ID Barang tidak ditemukan!");
        }
    }

    private Barang getProductById(ListBarang listBarang, String idBarang) {
        return listBarang.barang.stream()
                .filter(barang -> barang.id.equals(idBarang))
                .findFirst()
                .orElse(null);
    }

    private void lihatTransaksi(List<Transaksi> listTransaksi) {
        System.out.println("-----------------------");
        System.out.println("Daftar List Transaksi:");
        System.out.println("-----------------------");
        for (Transaksi transaksi : listTransaksi) {
            System.out.printf("""
                ID Transaksi: %s
                Akun: %s
                Metode Pembayaran: %s
                Total Biaya: Rp.%d
                -----------------------
                """, transaksi.IDTransaksi, transaksi.akun.id, transaksi.pembayaran, transaksi.totalHarga);
        }
    }
}
