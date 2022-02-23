public class Technology extends Product{

    private String version;

    public Technology(String name, int price, String type, String version) {
        super(name, price, type);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
