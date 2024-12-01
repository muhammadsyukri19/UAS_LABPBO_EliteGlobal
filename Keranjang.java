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

        // Menampilkan jumlah barang di keranjang
        System.out.println("Keranjang Belanja:");
        System.out.printf("Total Barang: %d%n", barang.size()); // Menampilkan jumlah barang

        // Menampilkan jumlah untuk setiap barang yang unik
        System.out.println("Daftar Barang:");
        for (Barang item : barang) {
            int count = 0;

            // Hitung jumlah setiap barang
            for (Barang b : barang) {
                if (item.getId().equals(b.getId())) {
                    count++;
                }
            }

            // Hanya tampilkan barang unik
            if (count > 0) {
                System.out.printf("- %s (ID: %s, Harga: Rp.%d) x%d%n", 
                    item.getNama(), 
                    item.getId(), 
                    item.getHarga(), 
                    count);
                
                // Hapus barang yang sudah ditampilkan untuk mencegah duplikasi
                barang.removeIf(b -> b.getId().equals(item.getId()));
            }
        }
    }
}
