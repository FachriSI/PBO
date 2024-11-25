import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

class Barang {  
    protected String kodeBarang;
    protected String namaBarang;
    protected double hargaBarang;

    public Barang(String kodeBarang, String namaBarang, double hargaBarang) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
    }

    public double hitungTotal(int jumlahBeli) {
        return hargaBarang * jumlahBeli;
    }

    @Override
    public String toString() {
        return "Nama Barang: " + namaBarang + "\n" +
               "Kode Barang: " + kodeBarang + "\n" +
               "Harga Barang: " + hargaBarang;
    }
}

class Transaksi extends Barang { 
    private String noFaktur;
    private int jumlahBeli;
    private double total;

    public Transaksi(String noFaktur, String kodeBarang, String namaBarang, double hargaBarang, int jumlahBeli) {
        super(kodeBarang, namaBarang, hargaBarang);
        this.noFaktur = noFaktur;
        this.jumlahBeli = jumlahBeli;
        this.total = hitungTotal(jumlahBeli);
    }

    @Override
    public String toString() {
        return "No. Faktur: " + noFaktur + "\n" +
               "Kode Barang: " + kodeBarang + "\n" +
               "Nama Barang: " + namaBarang + "\n" +
               "Harga Barang: " + hargaBarang + "\n" +
               "Jumlah Beli: " + jumlahBeli + "\n" +
               "Total: " + total;
    }
}

class Tes {
    private static int fakturCounter = 1; 
    private static Random random = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String lanjut;

        
        if (!login(scanner)) {
            System.out.println("Login gagal. Program dihentikan.");
            return;
        }

        
        System.out.println("\nSelamat Datang di Supermarket Smarket");
        
        Date currentDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'pada' hh:mm:ss a zzz");
        System.out.println("Tanggal dan Waktu: " + ft.format(currentDate));

        do {
            // Penggunaan Exception
            try {
                System.out.print("\nMasukkan Nama Barang: ");
                String namaBarang = scanner.nextLine().trim().toUpperCase(); // Menggunakan String trim dan toUpperCase

                System.out.print("Masukkan Kode Barang: ");
                String kodeBarang = scanner.nextLine().trim(); // Menggunakan String trim

                System.out.print("Masukkan Harga Barang: ");
                double hargaBarang = scanner.nextDouble();
                
                // Validasi harga barang
                if (hargaBarang < 1) {
                    throw new IllegalArgumentException("Harga barang harus lebih dari atau sama dengan 1 Rupiah.");
                }

                System.out.print("Masukkan Jumlah Beli: ");
                int jumlahBeli = scanner.nextInt();

                // Validasi jumlah beli
                if (jumlahBeli <= 0) {
                    throw new IllegalArgumentException("Jumlah beli harus lebih dari 0.");
                }

                // Membuat nomor faktur otomatis
                String noFaktur = "FTR" + String.format("%03d", fakturCounter++); // Format nomor faktur

                // Membuat objek Transaksi
                Transaksi transaksi = new Transaksi(noFaktur, kodeBarang, namaBarang, hargaBarang, jumlahBeli);

                // Menampilkan detail transaksi
                System.out.println("\n+----------------------------------------------------+");
                System.out.println(transaksi);
                System.out.println("+----------------------------------------------------+");
                System.out.println("Kasir: Fachri Akbar");
                System.out.println("+----------------------------------------------------+");

            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }

            // Menanyakan apakah pengguna ingin melanjutkan
            System.out.print("\nApakah Anda ingin memasukkan transaksi lain? (YA/TIDAK): ");
            lanjut = scanner.next();
            scanner.nextLine(); // Clear buffer

        } while (lanjut.equalsIgnoreCase("ya")); // Menggunakan String equalsIgnoreCase

        scanner.close();
        System.out.println("Terima kasih telah menggunakan program ini.");
    }

    private static boolean login(Scanner scanner) {
        String username = "admin";
        String password = "admin";
        String captcha = generateCaptcha(); // Menghasilkan captcha acak

        System.out.println("+-----------------------------------------------------+");
        System.out.print("Username: ");
        String inputUsername = scanner.nextLine().trim(); // Menggunakan trim

        System.out.print("Password: ");
        String inputPassword = scanner.nextLine().trim(); // Menggunakan trim

        System.out.println("Captcha: " + captcha); // Menampilkan captcha
        String inputCaptcha = scanner.nextLine().trim(); // Menggunakan trim

        // Menggunakan String equals dan equalsIgnoreCase
        if (inputUsername.equalsIgnoreCase(username) && inputPassword.equals(password) && inputCaptcha.equalsIgnoreCase(captcha)) {
            System.out.println("Login berhasil.");
            return true;
        } else {
            System.out.println("Login gagal. Silakan coba lagi.");
            return false;
        }
    }

    private static String generateCaptcha() {
        int length = 6; // Panjang captcha
        StringBuilder captcha = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            captcha.append(characters.charAt(index));
        }
        return captcha.toString();
    }
}