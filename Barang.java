class Barang {
    protected String id;
    protected String nama;
    protected int harga;
    protected int stok;

    public Barang(String id, String nama, int harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public int getHarga() { return harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}