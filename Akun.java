class Akun implements Autentikasi {
    protected String id;
    protected String password;

    Akun(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean login(String id, String password) {
        return this.id.equals(id) && this.password.equals(password);
    }
}
