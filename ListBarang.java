import java.util.ArrayList;
import java.util.List;

class ListBarang {
    List<Barang> barang;

    ListBarang() {
        barang = new ArrayList<>();
    }

    boolean jikaBarangAda(String idBarang) {
        return barang.stream()
                .anyMatch(b -> b.getId().equals(idBarang));
    }

    void tambahBarang(Barang barang) {
        this.barang.add(barang);
    }

    void lihatBarang() {
        System.out.println("-------------------------------------------------------------------");
        System.out.printf("| %-10s | %-25s | %-13s  | %-5s |\n", "ID", "Nama Barang", "Harga", "Stok");
        System.out.println("-------------------------------------------------------------------");

        for (Barang b : this.barang) {
            System.out.printf("| %-10s | %-25s | Rp.%-11s | %-5s |\n", 
                b.getId(), b.getNama(), b.getHarga(), b.getStok());
        }

        System.out.println("-------------------------------------------------------------------");
    }
}
