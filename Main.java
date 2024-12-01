import java.util.*;

/**
 * Kelas Main merupakan kelas utama yang menjalankan program EG Shopping System.
 * Program ini menyediakan fungsionalitas untuk login sebagai Admin, login sebagai Customer,
 * membuat akun Customer baru, dan keluar dari program.
 *
 * @author Alief Aulia SAG (2308107010028),
 *	   Muhammad Nazlul Ramadhyan (2308107010036), 
 *         Muhammad Sidqi Alfareza (2308107010040),
 *         Razian Sabri (2308107010050), 
 *	   Naufal Farrel Syafilan (2308107010058),
 *	   Muhammad Syukri (2308107010060),
 *	   Halim Elsa Putra (2308107010062),
 *		 
 * @since 2024-12-1
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ListBarang listBarang = new ListBarang();
        List<Transaksi> listTransaksi = new ArrayList<>();
        List<Customer> customerList = new ArrayList<>();
        Admin admin = new Admin("admin", "adminpass");

        // Baca data barang dari file
        FileHandler.loadProductsFromFile(listBarang);

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

    private static boolean adminLogin(Admin admin, ListBarang productList, List<Transaksi> transactionList) {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.print("Masukkan username: ");
                String idUser = scanner.next();
                System.out.print("Masukkan password: ");
                String password = scanner.next();
    
                if (admin.login(idUser, password)) {
                    System.out.println("Login berhasil sebagai Admin!");
                    showLoadingAnimation("Memuat menu Admin", 100);
    
                    AdminDriver adminDriver = new AdminDriver(productList, transactionList);
                    adminDriver.Menu(productList, transactionList);
                    return true;
                } else {
                    attempts++;
                    System.out.printf("Login gagal. Sisa percobaan: %d%n", MAX_ATTEMPTS - attempts);
                }
    
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan. Silakan coba lagi.");
                scanner.nextLine();
                attempts++;
            }
        }
        
        System.out.println("Terlalu banyak percobaan gagal. Kembali ke menu utama.");
        return false;
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
