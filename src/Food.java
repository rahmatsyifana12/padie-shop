public class Food extends Product {

    private String expiredDate;

    public Food(String name, int price, String type, String expiredDate) {
        super(name, price, type);
        this.expiredDate = expiredDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
}
