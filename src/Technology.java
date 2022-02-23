public class Technology extends Product{

    private String version;

    public Technology(String name, int price, String version) {
        super(name, price);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
