import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHandler {
    private static final String PRODUCTS_FILE = "produk.txt";
    private static final String CUSTOMERS_FILE = "customer.txt";
    private static final String TRANSACTIONS_FILE = "transaksi.txt";
    private static final String CART_DIR = "keranjang/";

    // Method untuk menyimpan customer baru
    public static void saveCustomer(Customer customer) {
        try {
            // Buka file dalam mode append
            try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE, true))) {
                // Tambahkan newline jika file tidak kosong
                if (new File(CUSTOMERS_FILE).length() > 0) {
                    writer.println();
                }
                writer.printf("%s,%s", customer.getId(), customer.getPassword());
            }
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    // Method untuk memuat daftar customer
    public static List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        customers.add(new Customer(parts[0].trim(), parts[1].trim()));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading customers: " + e.getMessage());
            }
        }
        return customers;
    }

    // Method untuk menyimpan keranjang belanja
    public static void saveCart(String customerId, List<Barang> items) {
        try {
            new File(CART_DIR).mkdirs();
            String filename = CART_DIR + customerId + "_keranjang.txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                for (Barang item : items) {
                    writer.printf("%s,%s,%d,%d%n", 
                        item.getId(), 
                        item.getNama(), 
                        item.getHarga(), 
                        item.getStok());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving cart: " + e.getMessage());
        }
    }

    // Method untuk memuat keranjang belanja
    public static List<Barang> loadCart(String customerId) {
        List<Barang> items = new ArrayList<>();
        String filename = CART_DIR + customerId + "_keranjang.txt";
        File cartFile = new File(filename);
        
        if (cartFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cartFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        items.add(new Barang(
                            parts[0], 
                            parts[1], 
                            Integer.parseInt(parts[2]), 
                            Integer.parseInt(parts[3])));
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
        String filename = CART_DIR + customerId + "_keranjang.txt";
        File cartFile = new File(filename);
        if (cartFile.exists()) {
            cartFile.delete();
        }
    }

    // Method untuk menambah produk ke file
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
            System.out.println("File produk.txt belum ada. Membuat file baru...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
            System.out.println("Error membaca file produk.txt: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method untuk menyimpan transaksi
    public static void saveTransaction(Transaksi transaksi) {
        try {
            // Buka file dalam mode append
            try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
                // Tambahkan newline jika file tidak kosong
                if (new File(TRANSACTIONS_FILE).length() > 0) {
                    writer.println();
                }
                writer.printf("%d,%s,%s,%d", 
                    transaksi.IDTransaksi,
                    transaksi.akun.getId(),
                    transaksi.pembayaran.toString(),
                    transaksi.getTotalHarga());
            }
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    // Method untuk memuat transaksi
    public static List<Transaksi> loadTransactions() {
        List<Transaksi> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0].trim());
                        String customerId = parts[1].trim();
                        String paymentMethod = parts[2].trim();
                        int totalAmount = Integer.parseInt(parts[3].trim());
                        
                        // Buat objek transaksi dari data yang dibaca
                        Customer customer = new Customer(customerId, "");
                        Pembayaran payment;
                        
                        // Tentukan jenis pembayaran berdasarkan string
                        if (paymentMethod.contains("QRIS")) {
                            payment = new QRIS();
                        } else if (paymentMethod.contains("COD")) {
                            payment = new COD();
                        } else if (paymentMethod.contains("Bank")) {
                            String bankName = paymentMethod.replace("Pembayaran melalui ", "");
                            payment = new Bank(bankName);
                        } else {
                            payment = new QRIS(); // Default ke QRIS
                        }
                        
                        Transaksi transaksi = new Transaksi(customer, new ArrayList<>(), payment);
                        transaksi.IDTransaksi = id;
                        transaksi.totalHarga = totalAmount;
                        
                        transactions.add(transaksi);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading transactions: " + e.getMessage());
            }
        }
        return transactions;
    }
} 
