import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Class untuk membantu menjalankan database
class DatabaseHelper {
    private static final String URL = "jdbc:postgresql://localhost:5432/LaborPBO"; // URL database
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

public class main {
    public static void main(String[] args) {
        List<Anggota> daftarAnggota = new ArrayList<>();
       // Membuat list untuk menyimpan data anggota sementara (opsional, bergantung pada implementasi).
        
       Scanner scanner = new Scanner(System.in);
       // Membuat scanner untuk menerima input dari pengguna.


        try (Connection conn = DatabaseHelper.getConnection()) {
            // Membuka koneksi database menggunakan helper class untuk mempermudah pengelolaan koneksi.
            DatabaseHelper.checkConnection(); 
            // Memeriksa apakah koneksi ke database berhasil.
            
            while (true) {
                // Loop utama untuk menampilkan menu dan menangani input pengguna hingga memilih keluar.
                System.out.println("\n===== Menu Manajemen Anggota Labor =====");
                System.out.println("1. Tambah Anggota (Create)");
                System.out.println("2. Lihat Daftar Anggota (Read)");
                System.out.println("3. Perbarui Data Anggota (Update)");
                System.out.println("4. Hapus Anggota (Delete)");
                System.out.println("5. Menghitung keaktifan anggota");
                System.out.println("6. Keluar");
                System.out.print("Pilih opsi: ");
                
                int pilihan = scanner.nextInt(); //Membaca input pengguna berupa angka untuk memilih opsi.
                scanner.nextLine(); // Membersihkan newline

                switch (pilihan) {
                    // Struktur switch-case untuk menangani opsi yang dipilih pengguna.
                    case 1 -> tambahAnggota(conn, scanner);
                    case 2 -> lihatDaftarAnggota(conn, daftarAnggota);
                    case 3 -> perbaruiAnggota(conn, scanner);
                    case 4 -> hapusAnggota(conn, scanner);
                    case 5 -> nilaiKeaktifanAnggota(conn, scanner);
                    case 6 -> {
                        // Keluar dari program
                        System.out.println("Terima kasih telah menggunakan sistem manajemen anggota labor!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
                            }
        } catch (SQLException e) {
            // Blok untuk menangani kesalahan yang mungkin terjadi saat berinteraksi dengan database.
            System.out.println("Terjadi kesalahan pada database: " + e.getMessage());
        }

        scanner.close();
    }

    private static void tambahAnggota(Connection conn, Scanner scanner) { // Method untuk menambahkan data anggota baru ke dalam database.
        try {
            System.out.print("Masukkan ID: ");
            String id = scanner.nextLine();
    
            // Validasi ID hanya berisi angka
            if (!id.matches("\\d+")) {
                throw new IllegalArgumentException("ID harus hanya terdiri dari angka.");
            }
    
            System.out.print("Masukkan Nama: ");
            String nama = scanner.nextLine();
    
            // Validasi nama tidak mengandung angka
            if (nama.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Nama tidak boleh mengandung angka.");
            }
    
            System.out.print("Masukkan Status (aktif/non-aktif): ");
            String status = scanner.nextLine().toLowerCase();
    
            // Validasi status hanya "aktif" atau "non-aktif"
            if (!status.equals("aktif") && !status.equals("non-aktif")) {
                throw new IllegalArgumentException("Status hanya boleh diisi dengan 'aktif' atau 'non-aktif'.");
            }
    
            Date tanggalGabung = new Date(System.currentTimeMillis());
            double totalKeaktifan = 0.0;
    
            String sql = "INSERT INTO list_Anggota (id, nama, status, tanggal_gabung, total_keaktifan) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                //query SQL untuk memasukkan data ke database
                stmt.setString(1, id);
                stmt.setString(2, nama);
                stmt.setString(3, status);
                stmt.setDate(4, tanggalGabung);
                stmt.setDouble(5, totalKeaktifan);
                stmt.executeUpdate();
                System.out.println("Anggota berhasil ditambahkan!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Input tidak valid: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan anggota: " + e.getMessage());
        }
    }

    private static void lihatDaftarAnggota(Connection conn, List<Anggota> daftarAnggota) { // Method untuk menampilkan daftar anggota dari database dan menyimpannya ke dalam list.
        String sql = "SELECT * FROM list_Anggota";     // Query SQL untuk mengambil semua data dari tabel `list_Anggota`.
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.isBeforeFirst()) { // Mengecek apakah ResultSet kosong
                System.out.println("Belum ada anggota yang terdaftar.");
                return;
            }
    
            System.out.println("\n===== Daftar Anggota =====");
            while (rs.next()) {  // Looping melalui setiap baris hasil query.
                String id = rs.getString("id");
                String nama = rs.getString("nama");
                String status = rs.getString("status");
                Date tanggalGabung = rs.getDate("tanggal_gabung"); // Sesuaikan tipe
                int totalKeaktifan = rs.getInt("total_keaktifan"); // Tetap sebagai int
                Anggota anggota = new Anggota(id, nama, status, tanggalGabung, totalKeaktifan);  // Membuat objek `Anggota` dengan data yang diambil dari database.
                daftarAnggota.add(anggota);   // Menambahkan objek anggota ke dalam list `daftarAnggota`.
    
                System.out.println("ID: " + id + ", Nama: " + nama + ", Status: " + status + ", Tanggal Gabung: " + tanggalGabung + ", Nilai Keaktifan: " + totalKeaktifan);
            }
        } catch (SQLException e) {
            System.out.println("Gagal membaca daftar anggota: " + e.getMessage());
        }
    }
    
    private static void perbaruiAnggota(Connection conn, Scanner scanner) {
        try {
            System.out.print("Masukkan ID anggota yang ingin di-update: ");
            String id = scanner.nextLine();
    
            // Validasi ID hanya berisi angka
            if (!id.matches("\\d+")) {
                throw new IllegalArgumentException("ID harus hanya terdiri dari angka.");
            }
    
            System.out.print("Masukkan Nama Baru: ");
            String namaBaru = scanner.nextLine();
    
            // Validasi nama tidak mengandung angka
            if (namaBaru.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Nama tidak boleh mengandung angka.");
            }
    
            System.out.print("Masukkan Status Baru (aktif/non-aktif): ");
            String statusBaru = scanner.nextLine().toLowerCase();
    
            // Validasi status hanya "aktif" atau "non-aktif"
            if (!statusBaru.equals("aktif") && !statusBaru.equals("non-aktif")) {
                throw new IllegalArgumentException("Status hanya boleh diisi dengan 'aktif' atau 'non-aktif'.");
            }
    
            String sql = "UPDATE list_Anggota SET nama = ?, status = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, namaBaru);
                stmt.setString(2, statusBaru);
                stmt.setString(3, id);
    
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Data anggota berhasil diperbarui!");
                } else {
                    System.out.println("Anggota dengan ID " + id + " tidak ditemukan.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Input tidak valid: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Gagal memperbarui anggota: " + e.getMessage());
        }
    }
    

    private static void hapusAnggota(Connection conn, Scanner scanner) { // Method untuk menghapus daftar anggota dari database
        try {
            System.out.print("Masukkan ID anggota yang ingin dihapus: ");
            String id = scanner.nextLine();

            String sql = "DELETE FROM list_Anggota WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Anggota berhasil dihapus!");
                } else {
                    System.out.println("Anggota dengan ID " + id + " tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghapus anggota: " + e.getMessage());
        }
    }

    private static void nilaiKeaktifanAnggota(Connection conn, Scanner scanner) { // Meminta nilai keaktifan anggota dan menghitung rata-ratanya. Apabila nilai 7 keatas, maka anggota dianggap aktif.
        try {
            System.out.println("\n===== Menilai Keaktifan Anggota =====");
    
            // Meminta ID anggota yang ingin dinilai
            System.out.print("Masukkan ID anggota yang ingin dinilai: ");
            String id = scanner.nextLine();
    
            // Mengambil data anggota berdasarkan ID
            String selectQuery = "SELECT id, nama, status, total_keaktifan FROM list_Anggota WHERE id = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, id);
                ResultSet rs = selectStmt.executeQuery();
    
                if (rs.next()) {
                    String nama = rs.getString("nama");
                    String status = rs.getString("status");
                    double totalKeaktifan = rs.getDouble("total_keaktifan");
    
                    System.out.println("\nMenilai anggota dengan ID: " + id);
                    System.out.println("Nama: " + nama);
                    System.out.println("Status saat ini: " + status);
                    System.out.println("Total keaktifan sebelumnya: " + totalKeaktifan);
    
                    // Meminta input nilai absen, tugas, dan proyek
                    int nilaiAbsen = getValidNilai(scanner, "Masukkan nilai absen (1-10): ");
                    int nilaiTugas = getValidNilai(scanner, "Masukkan nilai tugas (1-10): ");
                    int nilaiProyek = getValidNilai(scanner, "Masukkan nilai proyek (1-10): ");
    
                    // Menghitung rata-rata nilai
                    double rataRata = (nilaiAbsen + nilaiTugas + nilaiProyek) / 3.0;
                    System.out.println("Rata-rata nilai: " + rataRata);
    
                    // Menentukan status berdasarkan rata-rata nilai
                    String newStatus = rataRata >= 7 ? "aktif" : "non-aktif";
    
                    // Memperbarui status dan total keaktifan anggota di database
                    String updateQuery = "UPDATE list_Anggota SET status = ?, total_keaktifan = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, newStatus);
                        updateStmt.setDouble(2, rataRata);  // Update dengan rata-rata nilai
                        updateStmt.setString(3, id);
                        int rowsAffected = updateStmt.executeUpdate();
    
                        if (rowsAffected > 0) {
                            System.out.println("Status anggota telah diperbarui.");
                        } else {
                            System.out.println("Anggota dengan ID " + id + " tidak ditemukan.");
                        }
                    }
                } else {
                    System.out.println("Anggota dengan ID " + id + " tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saat menilai keaktifan anggota: " + e.getMessage());
        }
    }
    
    // Fungsi untuk validasi nilai antara 1 dan 10
    private static int getValidNilai(Scanner scanner, String prompt) {
        int nilai = -1;
        while (nilai < 1 || nilai > 10) {
            System.out.print(prompt);
            try {
                nilai = scanner.nextInt();
                scanner.nextLine(); // membersihkan newline setelah input angka
    
                if (nilai < 1 || nilai > 10) {
                    System.out.println("Error: Nilai harus antara 1 dan 10!");
                }
            } catch (Exception e) {
                System.out.println("Error: Input harus berupa angka!");
                scanner.nextLine(); // membersihkan input yang salah
            }
        }
        return nilai;
    }
}