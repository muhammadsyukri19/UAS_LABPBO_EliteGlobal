import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String PRODUCTS_FILE = "produk.txt";  // File produk
    private static final String CUSTOMERS_DIR = "customers/";   // Direktori untuk menyimpan data customer
    private static final String CART_FILE = "keranjang.txt";    // File untuk menyimpan data keranjang

    // Method untuk menyimpan customer baru
    public static void saveCustomer(Customer customer) {
        try {
            // Pastikan folder customers ada
            new File(CUSTOMERS_DIR).mkdirs();
            String filename = CUSTOMERS_DIR + customer.getId() + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                // Menyimpan ID (username) dan password yang sudah di-hash
                writer.println(customer.getId());
                writer.println(hashPassword(customer.getPassword()));
            }
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    // Method untuk memuat daftar customer
    public static List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        File dir = new File(CUSTOMERS_DIR);
        
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((_, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String id = reader.readLine();
                        String password = reader.readLine();
                        if (id != null && password != null) {
                            customers.add(new Customer(id, password));
                        }
                    } catch (IOException e) {
                        System.out.println("Error loading customer: " + e.getMessage());
                    }
                }
            }
        }
        return customers;
    }

    // Method untuk menyimpan keranjang belanja
    public static void saveCart(String customerId, List<Barang> items) {
        try {
            // Buka file keranjang untuk customer tertentu
            try (PrintWriter writer = new PrintWriter(new FileWriter(CART_FILE, true))) { // Append mode
                writer.printf("Customer ID: %s%n", customerId);  // Menyimpan ID pelanggan
                for (Barang item : items) {
                    writer.printf("%s,%s,%d,%d%n", 
                        item.getId(), 
                        item.getNama(), 
                        item.getHarga(), 
                        item.getStok());
                }
                writer.println(); // Menambahkan garis baru setelah menyimpan keranjang
            }
        } catch (IOException e) {
            System.out.println("Error saving cart: " + e.getMessage());
        }
    }

    // Method untuk memuat keranjang belanja berdasarkan ID pelanggan
    public static List<Barang> loadCart(String customerId) {
        List<Barang> items = new ArrayList<>();
        
        File cartFile = new File(CART_FILE);
        
        if (cartFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cartFile))) {
                String line;
                boolean isTargetCustomer = false;
                while ((line = reader.readLine()) != null) {
                    // Memeriksa apakah baris berisi ID pelanggan
                    if (line.startsWith("Customer ID: " + customerId)) {
                        isTargetCustomer = true;  // Mulai membaca keranjang pelanggan tersebut
                        continue;  // Skip baris ini, lanjutkan ke baris berikutnya
                    }

                    if (isTargetCustomer && !line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length == 4) {
                            items.add(new Barang(
                                parts[0], 
                                parts[1], 
                                Integer.parseInt(parts[2]), 
                                Integer.parseInt(parts[3])
                            ));
                        }
                    }

                    // Jika menemukan keranjang pelanggan lain, hentikan membaca
                    if (isTargetCustomer && line.trim().isEmpty()) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading cart: " + e.getMessage());
            }
        }
        return items;
    }

    // Method untuk menghapus keranjang belanja
    public static void clearCart(String customerId) {
        File cartFile = new File(CART_FILE);
        if (cartFile.exists()) {
            try {
                List<String> lines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(cartFile))) {
                    String line;
                    boolean isTargetCustomer = false;
                    while ((line = reader.readLine()) != null) {
                        // Cek apakah baris berisi ID pelanggan
                        if (line.startsWith("Customer ID: " + customerId)) {
                            isTargetCustomer = true;  // Mulai menghapus keranjang untuk pelanggan ini
                        } 
                        if (!isTargetCustomer) {
                            lines.add(line);
                        }

                        // Jika mencapai akhir keranjang pelanggan, berhenti menambah baris
                        if (isTargetCustomer && line.trim().isEmpty()) {
                            isTargetCustomer = false;  // Menghentikan penghapusan
                        }
                    }
                }

                // Menulis ulang file tanpa keranjang pelanggan yang dihapus
                try (PrintWriter writer = new PrintWriter(new FileWriter(cartFile))) {
                    for (String line : lines) {
                        writer.println(line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error clearing cart: " + e.getMessage());
            }
        }
    }

    // Method untuk menambah produk ke file produk
    public static void appendProductToFile(Barang barang) {
        try {
            // Cek apakah file kosong atau tidak
            File file = new File(PRODUCTS_FILE);
            boolean needNewLine = file.exists() && file.length() > 0;
            
            // Buka file dalam mode append
            try (PrintWriter out = new PrintWriter(new FileWriter(PRODUCTS_FILE, true))) {
                // Tambahkan newline jika file tidak kosong
                if (needNewLine) {
                    out.println(); // Tambahkan baris baru dulu
                }
                // Tulis data barang tanpa newline di akhir
                out.printf("%s,%s,%d,%d",
                    barang.getId(),
                    barang.getNama(),
                    barang.getHarga(),
                    barang.getStok()
                );
            }
        } catch (IOException e) {
            System.out.println("Error appending product: " + e.getMessage());
        }
    }

    // Method untuk hash password
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return password; // Fallback jika hashing gagal
        }
    }

    // Method untuk memperbarui file produk
    public static void updateProductsFile(List<Barang> barangList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Barang barang : barangList) {
                writer.printf("%s,%s,%d,%d%n",
                    barang.getId(),
                    barang.getNama(),
                    barang.getHarga(),
                    barang.getStok());
            }
        } catch (IOException e) {
            System.out.println("Error updating products file: " + e.getMessage());
        }
    }

    // Method untuk memuat produk dari file
    public static void loadProductsFromFile(ListBarang listBarang) {
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) {
            System.out.println("File products.txt belum ada. Membuat file baru...");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0].trim();
                    String nama = parts[1].trim();
                    int harga = Integer.parseInt(parts[2].trim());
                    int stok = Integer.parseInt(parts[3].trim());
                    listBarang.tambahBarang(new Barang(id, nama, harga, stok));
                }
            }
        } catch (IOException e) {
            System.out.println("Error membaca file products.txt: " + e.getMessage());
        }
    }
}
