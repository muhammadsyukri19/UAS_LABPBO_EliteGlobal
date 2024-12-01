class Admin extends Akun {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "adminpass";

    public Admin(String id, String password) {
        super(id, password);
    }

    public static String getDefaultUsername() {
        return ADMIN_USERNAME;
    }

    public static String getDefaultPassword() {
        return ADMIN_PASSWORD;
    }

    @Override
    public String toString() {
        return String.format("Admin [ID: %s, Role: Administrator]", getId());
    }
}
