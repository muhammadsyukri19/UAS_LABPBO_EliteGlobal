import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
        System.out.printf("Total Barang: %d%n", barang.size());

        // Buat map untuk menghitung jumlah setiap barang
        Map<String, Integer> itemCount = new HashMap<>();
        Map<String, Barang> itemDetails = new HashMap<>();

        // Hitung jumlah setiap barang
        for (Barang item : barang) {
            itemCount.put(item.getId(), itemCount.getOrDefault(item.getId(), 0) + 1);
            itemDetails.put(item.getId(), item);
        }

        // Tampilkan barang unik dengan jumlahnya
        System.out.println("Daftar Barang:");
        for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
            Barang item = itemDetails.get(entry.getKey());
            System.out.printf("- %s (ID: %s, Harga: Rp.%d) x%d%n", 
                item.getNama(), 
                item.getId(), 
                item.getHarga(), 
                entry.getValue());
        }
    }
}
