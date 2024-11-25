import java.io.*;
import java.util.Scanner;

public class RegistrasiAkun {

    static final String FOLDER_PATH = "users/";

    public static void registrasi(Scanner scanner) {
        System.out.println("==== Registrasi ====");
        System.out.print("Masukkan ID: ");
        String id = scanner.nextLine();
        System.out.print("Masukkan Password: ");
        String password = scanner.nextLine();
        System.out.print("Apakah Anda mendaftar sebagai (1-Admin / 2-Customer)? ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        String role = roleChoice == 1 ? "Admin" : "Customer";

        // Buat folder khusus untuk ID pengguna
        String userFolderPath = FOLDER_PATH + id + "/";
        File userFolder = new File(userFolderPath);

        if (userFolder.exists()) {
            System.out.println("ID sudah terdaftar. Silakan gunakan ID lain.");
            return;
        }

        if (userFolder.mkdirs()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFolderPath + "user_data.txt"))) {
                writer.write(id + "\n");
                writer.write(password + "\n");
                writer.write(role);
                System.out.println("Registrasi berhasil! Silakan login.");
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat menyimpan data.");
            }
        } else {
            System.out.println("Terjadi kesalahan saat membuat folder pengguna.");
        }
    }

    public static void login(Scanner scanner) {
        System.out.println("==== Login ====");
        System.out.print("Masukkan ID: ");
        String id = scanner.nextLine();
        System.out.print("Masukkan Password: ");
        String password = scanner.nextLine();

        String userFolderPath = FOLDER_PATH + id + "/";
        File userFolder = new File(userFolderPath);

        if (!userFolder.exists() || !new File(userFolderPath + "user_data.txt").exists()) {
            System.out.println("ID tidak ditemukan. Silakan registrasi terlebih dahulu.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFolderPath + "user_data.txt"))) {
            String storedId = reader.readLine();
            String storedPassword = reader.readLine();
            String role = reader.readLine();

            if (storedId.equals(id) && storedPassword.equals(password)) {
                System.out.println("Login berhasil sebagai " + role + "!");
                System.out.println("Memuat menu, harap tunggu...");

                // Screen loading animasi berbentuk lingkaran dan persentase
                String[] spinner = {"|", "/", "-", "\\"};
                int totalSteps = 100; // Total progress bar steps (100%)
                for (int i = 0; i <= totalSteps; i++) {
                    // Simulasi loading (300ms dibagi rata untuk setiap langkah)
                    System.out.print("\rLoading " + spinner[i % spinner.length] + " " + i + "%");
                    Thread.sleep(15); // Delay 15ms per langkah
                }

                System.out.println("\nMenu " + role + " siap digunakan!");
            } else {
                System.out.println("ID atau Password salah.");
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca data.");
        } catch (InterruptedException e) {
            System.out.println("\nProses terganggu. Coba lagi.");
        }
    }
}
