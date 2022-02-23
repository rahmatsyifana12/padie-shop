public class Cloth extends Product {

    private String size;

    public Cloth(String name, int price, String type, String size) {
        super(name, price, type);
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
