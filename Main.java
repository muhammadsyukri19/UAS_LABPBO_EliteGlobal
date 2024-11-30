import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ListBarang listBarang = new ListBarang();
        List<Transaksi> listTransaksi = new ArrayList<>();
        List<Customer> customerList = new ArrayList<>();
        Admin admin = new Admin("admin", "adminpass");

        // Baca data barang dari file
        loadProductsFromFile(listBarang);

        boolean isLoggedIn = true;

        while (isLoggedIn) {
            try {
                System.out.println("+=================================================================+");
                System.out.println("|                                                                 |");
                System.out.println("|                   Selamat datang di EG Distro                   |");
                System.out.println("|                                                                 |");
                System.out.println("+=================================================================+");
                System.out.println("| 1. Register Akun Customer                                       |");
                System.out.println("| 2. Login sebagai Admin                                          |");
                System.out.println("| 3. Login sebagai Customer                                       |");
                System.out.println("| 4. Keluar dari program                                          |");
                System.out.println("+=================================================================+");
                System.out.print("Pilih opsi: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        createAccount(customerList, listBarang, listTransaksi);  // Opsi buat akun pertama
                        break;
                    case 2:
                        isLoggedIn = adminLogin(admin, listBarang, listTransaksi);  // Login Admin
                        break;
                    case 3:
                        isLoggedIn = customerLogin(customerList, listBarang, listTransaksi);  // Login Customer
                        break;
                    case 4:
                        isLoggedIn = false;
                        System.out.println("======================================================");
                        System.out.println("       <<< terima kasih telah berkunjung >>>");
                        System.out.println("======================================================");
                        break;
                    default:
                        System.out.println("Opsi tidak valid.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Mohon masukkan angka.");
                scanner.nextLine();
            }
        }
    }

    private static void loadProductsFromFile(ListBarang listBarang) {
        // Method untuk load barang dari file
        // Misalnya, membaca dari file dan mengisi listBarang
    }

    private static boolean adminLogin(Admin admin, ListBarang productList, List<Transaksi> transactionList) {
        boolean isLoggedIn = false;
    
        do {
            try {
                System.out.print("Masukkan username: ");
                String idUser = scanner.next();
                System.out.print("Masukkan password: ");
                String password = scanner.next();
    
                if (admin.login(idUser, password)) {
                    // Menampilkan animasi loading
                    System.out.println("Login berhasil sebagai Admin!");
                    showLoadingAnimation("Memuat menu Admin", 100);
    
                    AdminDriver adminDriver = new AdminDriver(productList, transactionList);
                    adminDriver.Menu(productList, transactionList);
                    isLoggedIn = true;
                } else {
                    System.out.println("Login gagal. Username atau password tidak valid.");
                }
    
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan. Silakan coba lagi.");
                scanner.nextLine(); // Mengonsumsi karakter newline
            }
        } while (!isLoggedIn);
    
        return isLoggedIn;
    }
    
    private static boolean customerLogin(List<Customer> customerList, ListBarang listBarang, List<Transaksi> transactionList) {
        boolean isLoggedIn = false;
    
        do {
            try {
                System.out.print("Masukkan username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Masukkan password: ");
                String password = scanner.nextLine().trim();
    
                // Cek customer dari file terlebih dahulu
                List<Customer> savedCustomers = FileHandler.loadCustomers();  // Load customer dari file
                for (Customer savedCustomer : savedCustomers) {
                    if (savedCustomer.login(username, password)) {
                        // Menampilkan animasi loading
                        System.out.println("Login berhasil sebagai Customer!");
                        showLoadingAnimation("Memuat menu Customer", 100);
    
                        CustomerDriver customerDriver = new CustomerDriver(savedCustomer, 
                            new Transaksi(savedCustomer, new ArrayList<>(), new QRIS()), 
                            listBarang);
                        customerDriver.Menu(listBarang, transactionList);
                        isLoggedIn = true;
                        return true;
                    }
                }
    
                System.out.println("Login gagal. Username atau password salah.");
    
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
                scanner.nextLine();
            }
        } while (!isLoggedIn);
    
        return false;
    }
    
    private static void showLoadingAnimation(String message, int totalSteps) {
        System.out.println(message + ", harap tunggu...");
    
        String[] spinner = {"|", "/", "-", "\\"};
        try {
            for (int i = 0; i <= totalSteps; i++) {
                // Simulasi loading (300ms dibagi rata untuk setiap langkah)
                System.out.print("\r" + message + " " + spinner[i % spinner.length] + " " + i + "%");
                Thread.sleep(15); // Delay 15ms per langkah
            }
            System.out.println();  // Pindah baris setelah loading selesai
        } catch (InterruptedException e) {
            System.out.println("Proses loading terganggu.");
        }
    }
    

    private static void createAccount(List<Customer> customerList, ListBarang listBarang, List<Transaksi> transactionList) {
        System.out.println("Membuat Akun Baru:");
    
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
    
        // Membuat customer baru
        Customer newCustomer = new Customer(username, password);
        customerList.add(newCustomer);
    
        // Simpan customer ke file
        FileHandler.saveCustomer(newCustomer);
    
        System.out.println("Akun berhasil dibuat dan disimpan. Silakan login untuk melanjutkan.");
    }
}
