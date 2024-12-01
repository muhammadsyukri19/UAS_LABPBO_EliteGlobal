import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String PRODUCTS_FILE = "products.txt";  // File produk
    private static final String CUSTOMERS_DIR = "customers/";   // Direktori untuk menyimpan data customer
    private static final String CART_FILE = "keranjang.txt";    // File untuk menyimpan data keranjang
    private static final String TRANSACTION_HISTORY_FILE = "riwayat_transaksi.txt";  // Ubah nama file

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
                    if (line.trim().isEmpty()) {
                        isTargetCustomer = false;
                        continue;
                    }
                    
                    if (line.equals("Customer ID: " + customerId)) {
                        isTargetCustomer = true;
                        continue;
                    }
                    
                    if (isTargetCustomer) {
                        String[] parts = line.split(",");
                        if (parts.length == 4) {
                            try {
                                Barang barang = new Barang(
                                    parts[0].trim(),
                                    parts[1].trim(),
                                    Integer.parseInt(parts[2].trim()),
                                    Integer.parseInt(parts[3].trim())
                                );
                                items.add(barang);
                            } catch (NumberFormatException e) {
                                System.out.println("Warning: Mengabaikan baris dengan format tidak valid");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Info: Gagal membaca file keranjang");
                return new ArrayList<>(); // Return empty list instead of null
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

    // Tambahkan method baru untuk menyimpan histori transaksi
    public static void saveTransactionHistory(Transaksi transaksi) {
        // Pastikan direktori ada
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter fw = new FileWriter("data/" + TRANSACTION_HISTORY_FILE, true);
             PrintWriter writer = new PrintWriter(new BufferedWriter(fw))) {
            
            // Format: customerId,transactionId,total,paymentMethod
            writer.printf("%s,%d,%d,%s%n",
                transaksi.akun.getId(),
                transaksi.IDTransaksi,
                transaksi.totalHarga,
                transaksi.pembayaran.toString().replace(",", ";")); // Hindari konflik dengan delimiter
            writer.flush(); // Pastikan data tertulis
            
        } catch (IOException e) {
            System.out.println("Error menyimpan transaksi: " + e.getMessage());
        }
    }

    // Method untuk membaca histori transaksi
    public static List<Transaksi> loadTransactionHistory(String customerId) {
        List<Transaksi> transactions = new ArrayList<>();
        File file = new File("data/" + TRANSACTION_HISTORY_FILE);
        
        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && parts[0].equals(customerId)) {
                        // Parse data
                        String userId = parts[0].trim();
                        int transId = Integer.parseInt(parts[1].trim());
                        double total = Double.parseDouble(parts[2].trim());
                        String paymentMethod = parts[3].trim().replace(";", ",");

                        // Buat objek pembayaran
                        Pembayaran payment;
                        if (paymentMethod.contains("QRIS")) {
                            payment = new QRIS();
                        } else if (paymentMethod.contains("COD")) {
                            payment = new COD();
                        } else {
                            String bankName = paymentMethod.replace("Pembayaran melalui ", "");
                            payment = new Bank(bankName);
                        }

                        // Buat transaksi baru
                        Customer customer = new Customer(userId, "");
                        Transaksi transaksi = new Transaksi(customer, new ArrayList<>(), payment);
                        transaksi.IDTransaksi = transId;
                        transaksi.totalHarga = (int)total;
                        transactions.add(transaksi);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Warning: Mengabaikan baris tidak valid dalam file histori");
                }
            }
        } catch (IOException e) {
            System.out.println("Error membaca histori: " + e.getMessage());
        }
        
        return transactions;
    }

    // Method baru untuk membaca semua transaksi
    public static void displayAllTransactions() {
        File file = new File("data/" + TRANSACTION_HISTORY_FILE);
        
        if (!file.exists()) {
            System.out.println("Belum ada riwayat transaksi.");
            return;
        }

        System.out.println("\nDaftar Semua Transaksi:");
        System.out.println("----------------------------------------");
        System.out.printf("| %-10s | %-8s | %-12s | %-15s |\n", 
            "User ID", "ID Trans", "Total", "Metode Bayar");
        System.out.println("----------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String userId = parts[0].trim();
                        String transId = parts[1].trim();
                        String total = String.format("Rp%,d", Integer.parseInt(parts[2].trim()));
                        String paymentMethod = parts[3].trim().replace(";", ",");

                        System.out.printf("| %-10s | %-8s | %-12s | %-15s |\n",
                            userId, transId, total, paymentMethod);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Warning: Data transaksi tidak valid");
                }
            }
            System.out.println("----------------------------------------");
        } catch (IOException e) {
            System.out.println("Error membaca riwayat transaksi: " + e.getMessage());
        }
    }
}
