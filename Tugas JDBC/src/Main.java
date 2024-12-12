import java.sql.*;
import java.util.Scanner;


class DatabaseHelper {
    private static final String URL = "jdbc:postgresql://localhost:5432/Smarket"; // URL database
    private static final String USER = "postgres"; // Username PostgreSQL
    private static final String PASSWORD = "admin"; // Password PostgreSQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void checkConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Koneksi berhasil ke database.");
            } else {
                System.out.println("Koneksi gagal.");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi ke database gagal: " + e.getMessage());
        }
    }
}

class Transaksi {
    private String noFaktur;
    private String kodeBarang;
    private String namaBarang;
    private double hargaBarang;
    private int jumlahBeli;
    private double total;

    public Transaksi(String noFaktur, String kodeBarang, String namaBarang, double hargaBarang, int jumlahBeli) {
        this.noFaktur = noFaktur;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
        this.jumlahBeli = jumlahBeli;
        this.total = hargaBarang * jumlahBeli;
    }

    public static void createTransaksi(Scanner scanner) {
        System.out.print("Masukkan Nama Barang: ");
        String namaBarang = scanner.nextLine().trim();

        System.out.print("Masukkan Kode Barang: ");
        String kodeBarang = scanner.nextLine().trim();

        System.out.print("Masukkan Harga Barang: ");
        double hargaBarang = scanner.nextDouble();

        System.out.print("Masukkan Jumlah Beli: ");
        int jumlahBeli = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        String noFaktur = "FTR" + System.currentTimeMillis();

        String sql = "INSERT INTO transaksi (no_faktur, kode_barang, nama_barang, harga_barang, jumlah_beli, total) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, noFaktur);
            pstmt.setString(2, kodeBarang);
            pstmt.setString(3, namaBarang);
            pstmt.setDouble(4, hargaBarang);
            pstmt.setInt(5, jumlahBeli);
            pstmt.setDouble(6, hargaBarang * jumlahBeli);
            pstmt.executeUpdate();

            System.out.println("Transaksi berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan transaksi: " + e.getMessage());
        }
    }

    public static void readTransaksi() {
        String sql = "SELECT * FROM transaksi";

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("No Faktur: " + rs.getString("no_faktur"));
                System.out.println("Kode Barang: " + rs.getString("kode_barang"));
                System.out.println("Nama Barang: " + rs.getString("nama_barang"));
                System.out.println("Harga Barang: " + rs.getDouble("harga_barang"));
                System.out.println("Jumlah Beli: " + rs.getInt("jumlah_beli"));
                System.out.println("Total: " + rs.getDouble("total"));
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Gagal membaca transaksi: " + e.getMessage());
        }
    }

    public static void updateTransaksi(Scanner scanner) {
        System.out.print("Masukkan No Faktur yang ingin diubah: ");
        String noFaktur = scanner.nextLine().trim();

        System.out.print("Masukkan Kode Barang yang baru: ");
        String kodeBarang = scanner.nextLine().trim();

        System.out.print("Masukkan Nama Barang Baru: ");
        String namaBarang = scanner.nextLine().trim();

        System.out.print("Masukkan Harga Barang Baru: ");
        double hargaBarang = scanner.nextDouble();

        System.out.print("Masukkan Jumlah Beli Baru: ");
        int jumlahBeli = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        String sql = "UPDATE transaksi SET kode_barang = ?, nama_barang = ?, harga_barang = ?, jumlah_beli = ?, total = ? WHERE no_faktur = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kodeBarang);
            pstmt.setString(2, namaBarang);
            pstmt.setDouble(3, hargaBarang);
            pstmt.setInt(4, jumlahBeli);
            pstmt.setDouble(5, hargaBarang * jumlahBeli);
            pstmt.setString(6, noFaktur);
            pstmt.executeUpdate();

            System.out.println("Transaksi berhasil diubah.");
        } catch (SQLException e) {
            System.out.println("Gagal mengubah transaksi: " + e.getMessage());
        }
    }

    public static void deleteTransaksi(Scanner scanner) {
        System.out.print("Masukkan No Faktur yang ingin dihapus: ");
        String noFaktur = scanner.nextLine().trim();

        String sql = "DELETE FROM transaksi WHERE no_faktur = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, noFaktur);
            pstmt.executeUpdate();

            System.out.println("Transaksi berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("Gagal menghapus transaksi: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!login(scanner)) {
            System.out.println("Login gagal. Program dihentikan.");
            return;
        }

        int pilihan;
        do {
            System.out.println("\nMenu CRUD:");
            System.out.println("1. Tambah Transaksi");
            System.out.println("2. Lihat Transaksi");
            System.out.println("3. Ubah Transaksi");
            System.out.println("4. Hapus Transaksi");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (pilihan) {
                case 1:
                    Transaksi.createTransaksi(scanner);
                    break;
                case 2:
                    Transaksi.readTransaksi();
                    break;
                case 3:
                    Transaksi.updateTransaksi(scanner);
                    break;
                case 4:
                    Transaksi.deleteTransaksi(scanner);
                    break;
                case 5:
                    System.out.println("Terima kasih telah menggunakan program.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 5);

        scanner.close();
    }

    private static boolean login(Scanner scanner) {
        String username = "postgres";
        String password = "admin";

        System.out.print("Username: ");
        String inputUsername = scanner.nextLine().trim();

        System.out.print("Password: ");
        String inputPassword = scanner.nextLine().trim();

        return inputUsername.equals(username) && inputPassword.equals(password);
    }
}
