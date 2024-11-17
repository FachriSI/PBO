import java.util.Scanner;

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
        return super.toString() + "\n" + 
               "Jumlah Beli: " + jumlahBeli + "\n" + 
               "Total: " + total + "\n" + 
               "No Faktur: " + noFaktur; 
    } 
} 
 
class Tes {     
    private static int fakturCounter = 1; // Counter nomor faktor
public static void main(String[] args) { 
    Scanner scanner = new Scanner(System.in);         
    String lanjut; 

    do { 
        // Penggunaan Exception             
        try { 
            System.out.print("Masukkan Nama Barang: ");                 
            String namaBarang = scanner.nextLine(); 

            System.out.print("Masukkan Kode Barang: "); 
            String kodeBarang = scanner.nextLine();                 
            
            System.out.print("Masukkan Harga Barang: ");                 
            double hargaBarang = scanner.nextDouble(); 
             
            // Validasi harga barang                 
            if (hargaBarang < 1) { 
                throw new IllegalArgumentException("Harga barang harus lebih dari atau sama dengan 1 Rupiah."); 
            } 

            System.out.print("input Jumlah Beli: ");                
             int jumlahBeli = scanner.nextInt(); 

            // Validasi jumlah beli                 
            if (jumlahBeli <= 0) {                     
                throw new IllegalArgumentException("Jumlah beli harus lebih dari 0."); 
            } 

            // Membuat nomor faktur otomatis 
            String noFaktur = "FTR" + String.format("%03d", fakturCounter++); // Format dari nomor faktur 

            // Membuat objek Transaksi 
            Transaksi transaksi = new Transaksi(noFaktur, kodeBarang, namaBarang, hargaBarang, jumlahBeli); 

            // Menampilkan detail transaksi 
            System.out.println("\nDetail Transaksi:");                 
            System.out.println(transaksi); 

        } catch (Exception e) { 
            System.out.println("Terjadi kesalahan: " + e.getMessage());                 
            scanner.nextLine(); // Clear buffer 
        } 
        // menanyakan kepada user apakah ingin melanjutkan
        System.out.print("\nApakah Anda ingin memasukkan transaksi lain? (ya/tidak): ");             
        lanjut = scanner.next(); 
        scanner.nextLine(); // Clear buffer 
    } while (lanjut.equalsIgnoreCase("ya"));         scanner.close(); 
    System.out.println("Terima kasih telah menggunakan program ini."); 
} 
}
