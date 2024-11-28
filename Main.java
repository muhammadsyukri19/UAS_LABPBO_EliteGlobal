import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ListBarang listBarang = new ListBarang();
        List<Transaksi> listTransaksi = new ArrayList<>();
        List<Customer> customerList = new ArrayList<>();
        Admin admin = new Admin("admin", "adminpass");
        Customer customer = new Customer("user", "userpass");

        // Baca data barang dan transaksi dari file
        FileHandler.loadProductsFromFile(listBarang);
        listTransaksi.addAll(FileHandler.loadTransactions());

        boolean isLoggedIn = true;

        while (isLoggedIn) {
            try {
                System.out.println("+=================================================================+");
                System.out.println("|                                                                 |");
                System.out.println("|                   Welcome To EG Distro                          |");
                System.out.println("|                                                                 |");
                System.out.println("+=================================================================+");
                System.out.println("| 1. Login sebagai Admin                                          |");
                System.out.println("| 2. Login sebagai Customer                                       |");
                System.out.println("| 3. Registrasi akun Customer                                     |");
                System.out.println("| 4. EXIT                                                         |");
                System.out.println("+=================================================================+");
                System.out.print("Pilih opsi (1/2/3/4): ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        isLoggedIn = adminLogin(admin, listBarang, listTransaksi);
                        break;
                    case 2:
                        isLoggedIn = customerLogin(customer, listBarang, listTransaksi);
                        break;
                    case 3:
                        createAccount(customerList, listBarang, listTransaksi);
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
        boolean isLoggedIn = false;
    
        do {
            try {
                System.out.print("Masukkan username: ");
                String idUser = scanner.next();
                System.out.print("Masukkan password: ");
                String password = scanner.next();
    
                if (admin.login(idUser, password)) {
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

    private static boolean customerLogin(Customer customer, ListBarang listBarang, List<Transaksi> transactionList) {
        boolean isLoggedIn = false;

        do {
            try {
                System.out.print("Masukkan username: ");
                String username = scanner.next().trim();
                System.out.print("Masukkan password: ");
                String password = scanner.next().trim();

                // Cek customer dari file terlebih dahulu
                List<Customer> savedCustomers = FileHandler.loadCustomers();
                for (Customer savedCustomer : savedCustomers) {
                    if (savedCustomer.login(username, password)) {
                        CustomerDriver customerDriver = new CustomerDriver(savedCustomer, 
                            new Transaksi(savedCustomer, new ArrayList<>(), new QRIS()), 
                            listBarang);
                        customerDriver.Menu(listBarang, transactionList);
                        isLoggedIn = true;
                        return true;
                    }
                }

                // Jika tidak ditemukan di file, cek default customer
                if (customer.login(username, password)) {
                    CustomerDriver customerDriver = new CustomerDriver(customer, 
                        new Transaksi(customer, new ArrayList<>(), new QRIS()), 
                        listBarang);
                    customerDriver.Menu(listBarang, transactionList);
                    isLoggedIn = true;
                    return true;
                }

                System.out.println("Login gagal. Username atau password salah.");

            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
                scanner.nextLine();
            }
        } while (!isLoggedIn);

        return false;
    }

    private static void createAccount(List<Customer> customerList, ListBarang listBarang, List<Transaksi> transactionList) {
        System.out.println("Membuat Akun Baru:");
        
        System.out.print("Enter username: ");
        String username = scanner.next().trim();

        // Cek apakah username sudah terdaftar
        List<Customer> savedCustomers = FileHandler.loadCustomers();
        for (Customer existingCustomer : savedCustomers) {
            if (existingCustomer.getId().equals(username)) {
                System.out.println("Maaf akun anda telah terdaftar");
                return; // Keluar dari method jika username sudah ada
            }
        }
        
        System.out.print("Enter password: ");
        String password = scanner.next().trim();
    
        // Hash password sebelum membuat customer baru
        String hashedPassword = FileHandler.hashPassword(password);
        Customer newCustomer = new Customer(username, hashedPassword);
        customerList.add(newCustomer);
    
        // Simpan ke file
        FileHandler.saveCustomer(newCustomer);
    
        System.out.println("Akun berhasil dibuat dan disimpan. Silakan login untuk melanjutkan.");
        customerLogin(newCustomer, listBarang, transactionList);
    }
}
