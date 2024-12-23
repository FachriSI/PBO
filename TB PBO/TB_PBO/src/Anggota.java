public  class Anggota extends Member implements AktivitasLabor {
    private String id;
    private String nama;
    private String status;
    private int totalKehadiran;
    private double totalKeaktifan;

    public Anggota(String id, String nama, String status, java.sql.Date tanggalGabung, double totalKeaktifan) {
        super(id, nama);  // Memanggil konstruktor Member
        this.status = status;
        this.totalKehadiran = 0;
    }

     // Metode setter
     public void setTotalKehadiran(int totalKehadiran) {
        this.totalKehadiran = totalKehadiran;
    }

      // Metode getter
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getNama() {
        return nama;
    }

    public String getStatus() {
        return status;
    }

  // Metode getter
    public int getTotalKehadiran() {
        return totalKehadiran;
    }

    @Override
    public void absen() {
        totalKehadiran++;
    }

    public String getFormattedName() {
        return nama.toUpperCase();
    }

    @Override
    public void tampilkanInfo() {
        System.out.println("ID: " + id + ", Nama: " + nama + ", Status: " + status + ", Kehadiran: " + totalKehadiran);
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalKeaktifan() {
        return totalKeaktifan;
    }

    public void setTotalKeaktifan(double totalKeaktifan) {
        this.totalKeaktifan = totalKeaktifan;
    }
}
