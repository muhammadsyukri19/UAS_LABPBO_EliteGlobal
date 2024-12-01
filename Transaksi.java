import java.text.DecimalFormat;
import java.util.List;

class Transaksi {
    static int hitungTransaksi;
    int IDTransaksi;
    Akun akun;
    List<Barang> barang;
    Pembayaran pembayaran;
    int totalHarga;

    Transaksi(Akun akun, List<Barang> barang, Pembayaran pembayaran) {
        this.IDTransaksi = hitungTransaksi++;
        this.akun = akun;
        this.barang = barang;
        this.pembayaran = pembayaran;
        this.totalHarga = hitungTotalHarga();
    }

    int getTotalHarga() {
        return totalHarga;
    }

    private int hitungTotalHarga() {
        int total = 0;
        for (Barang item : barang) {
            total += item.getHarga();
        }
        return total;
    }

    void prosesPembayaran() {
        pembayaran.prosesPembayaran();
    }

    void cetakResi() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.println("+----------------------------------------+");
        System.out.println("+\t     <<< EG Distro >>>\t         +");               
        System.out.println("+----------------------------------------+");
        System.out.println("+\t     Resi Transaksi #" + IDTransaksi + "\t         +");
        System.out.println("+----------------------------------------+");
        System.out.println(" User: " + akun.id + "\t         ");
        System.out.println(" Metode Pembayaran: " + pembayaran);
        System.out.println("+----------------------------------------+");
        System.out.println("+ Daftar Barang:");

        for (Barang barang : barang) {
            System.out.println("+ - " + barang.getNama() + " (ID: " + barang.getId() + ", Harga: " + barang.getHarga() + ")");
        }

        System.out.println("+----------------------------------------+");
        System.out.println("+ Total Harga: Rp " + formatter.format(totalHarga));
        System.out.println("+----------------------------------------+");
        System.out.println("<<<< Terima kasih telah berbelanja di EG Distro >>>>");
        System.out.println("------------------------------------------");
    }
}
