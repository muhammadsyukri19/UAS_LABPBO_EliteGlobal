import java.util.ArrayList;
import java.util.List;

class Keranjang {
    List<Barang> barang;

    Keranjang() {
        this.barang = new ArrayList<>();
    }

    void tambahBarang(Barang barang) {
        this.barang.add(barang);
    }

    void lihatKeranjang() {
        if (barang.isEmpty()) {
            System.out.println("Keranjang kosong!");
            return;
        }

        System.out.println("Keranjang Belanja:");
        for (Barang item : this.barang) {
            System.out.printf("- %s (ID: %s, Harga: Rp.%d)%n", 
                item.getNama(), 
                item.getId(), 
                item.getHarga());
        }
    }
}
