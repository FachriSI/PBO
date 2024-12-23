public class Member {
    // Atribut dasar yang dimiliki semua anggota
    private String id;
    private String nama;

    // Konstruktor dan metode lainnya
    public Member(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    // Getter dan Setter untuk id dan nama
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}
