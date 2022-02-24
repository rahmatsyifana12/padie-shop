import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    Scanner scan = new Scanner(System.in);
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> cart = new ArrayList<>();
    int currUserIdx = -1;
    int structId = 1;

    public Main() {
        int choose = 0;
        do {
            clear();
            System.out.println("Padie Shop ~");
            System.out.println("------------------");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            int err;
            do {
                err = 0;
                try {
                    System.out.printf(">> ");
                    choose = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    err = 1;
                    scan.nextLine();
                }
            } while (err == 1);

            switch (choose) {
                case 1:
                    login();
                    break;
                case 2:
                    registerUser();
                    break;
            }

        } while (choose != 3);
    }

    private void adminMenu() {
        String name;
        int price = 0;

        do {
            System.out.printf("Product name : ");
            name = scan.nextLine();
            if (!isName(name)) {
                continue;
            }
            if (!isProduct(name)) {
                continue;
            }
            break;
        } while (true);

        do {
            int err;
            do {
                err = 0;
                try {
                    System.out.printf("Price : ");
                    price = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    err = 1;
                    scan.nextLine();
                }
            } while (err == 1);

            if (price < 1000) {
                continue;
            }

            break;
        } while (true);

        if (name.endsWith(" [F]")) {
            String expDate;
            expDate = scan.nextLine();
            products.add(new Food(name, price + price/10, "Food", expDate));
        }
        else if (name.endsWith(" [C]")) {
            String size = "";
            do {
                size = scan.nextLine();
                if (size.equals("S") || size.equals("M") || size.equals("L") || size.equals("XL")) {
                    break;
                }
            } while (true);
            products.add(new Food(name, price + price/4, "Cloth", size));
        } else {
            String version;
            version = scan.nextLine();
            products.add(new Food(name, price + price*3/10, "Technology", version));
        }
    }

    private boolean isProduct(String name) {
        return name.endsWith(" [F]") || name.endsWith(" [C]") || name.endsWith(" [T]");
    }

    private void mainMenu() {
        int choose = 0;
        do {
            clear();
            System.out.println("Hello " + users.get(currUserIdx).getFullName());
            System.out.println("------------------");
            System.out.println("1. Buy Product");
            System.out.println("2. Purchase History");
            System.out.println("3. Add Money");
            System.out.println("4. Check Account Balance");
            System.out.println("5. Logout");
            int err;
            do {
                err = 0;
                try {
                    System.out.printf(">> ");
                    choose = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    err = 1;
                    scan.nextLine();
                }
            } while (err == 1);

            switch (choose) {
                case 1:
                    buyProduct();
                    break;
                case 2:
                    displayPurchaseHistory();
                    break;
                case 3:
                    addMoney();
                    break;
                case 4:
                    checkAccBalance();
                    break;
            }

        } while (choose != 5);
        currUserIdx = -1;
        cart.clear();
    }

    private void buyProduct() {
        int choose = 0;
        do {
            System.out.println("1. Pick a Product");
            System.out.println("2. Check Out");
            System.out.println("3. Back");
            int err;
            do {
                err = 0;
                try {
                    System.out.printf(">> ");
                    choose = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    err = 1;
                    scan.nextLine();
                }
            } while (err == 1);

            switch (choose) {
                case 1:
                    pickProduct();
                    break;
                case 2:
                    checkOut();
                    break;
            }
        } while (choose != 3);
    }

    private void pickProduct() {
        System.out.println("--------------------------------------------");
        System.out.printf("| %-3s | %-18s | %-13s |\n", "No.", "Product Name", "Price");
        System.out.println("--------------------------------------------");
        for (int i=0; i<products.size(); i++) {
            System.out.printf("| %-3d | %-18s | Rp %-10s |\n", i, products.get(i).getName(), products.get(i).getPrice());
        }
        System.out.println("--------------------------------------------");

        int chooseIdx = -1;
        do {
            int err;
            do {
                err = 0;
                try {
                    System.out.printf("Pick a product by index [%d - %d]: ", 0, products.size()-1);
                    chooseIdx = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    err = 1;
                    scan.nextLine();
                }
            } while (err == 1);

            if (chooseIdx < 0 || chooseIdx >= products.size()) {
                System.out.println("Product not found or index out of bound!");
                continue;
            } else {
                String conf;
                System.out.printf("Are you sure to pick this product? [Y | N] : ");
                conf = scan.nextLine();
                if (conf.equals("N") || conf.equals("Y") || conf.equals("n") || conf.equals("y")) {
                    if (conf.equals("N") || conf.equals("n")) {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            break;
        } while (true);

        cart.add(products.get(chooseIdx));
        users.get(currUserIdx).setCart(cart);
    }

    private void checkOut() {
        String conf;
        do {
            System.out.printf("Are you sure to check out? [Y | N] : ");
            conf = scan.nextLine();
            if (conf.equals("N") || conf.equals("Y") || conf.equals("n") || conf.equals("y")) {
                if (conf.equals("N") || conf.equals("n")) {
                    return;
                }
            } else {
                continue;
            }
            break;
        } while (true);

        int totalPrice = 0;
        for (int i=0; i<users.get(currUserIdx).getCart().size(); i++) {
            totalPrice += users.get(currUserIdx).getCart().get(i).getPrice();
        }

        Receipt newReceipt = new Receipt(structId, totalPrice);
        newReceipt.setProducts(users.get(currUserIdx).getCart());
        structId++;

        if (users.get(currUserIdx).getReceipts().size() == 0) {
            ArrayList<Receipt> newReceipts = new ArrayList<>();
            newReceipts.add(newReceipt);
            users.get(currUserIdx).setReceipts(newReceipts);
        } else {
            ArrayList<Receipt> newReceipts = users.get(currUserIdx).getReceipts();
            newReceipts.add(newReceipt);
            users.get(currUserIdx).setReceipts(newReceipts);
        }

        displayReceipt(users.get(currUserIdx).getReceipts().size() - 1);
    }

    private void displayPurchaseHistory() {
        int recId = 0;
        int recIdx = -1;
        User currUser = users.get(currUserIdx);
        ArrayList<Receipt> currUserReceipts = currUser.getReceipts();
        do {
            System.out.println("--------------------");
            System.out.println("| No. | Receipt ID |");
            System.out.println("--------------------");
            for (int i=0; i<currUserReceipts.size(); i++) {
                System.out.printf("| %-3d | %-10d |\n", i+1, currUserReceipts.get(i).getId());
            }
            System.out.println("--------------------\n");
            int err;
            do {
                err = 0;
                try {
                    System.out.printf("Enter receipt ID [0 to go back] : ");
                    recId = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    err = 1;
                    scan.nextLine();
                }
            } while (err == 1);

            if (recId == 0) {
                return;
            }

            boolean found = false;
            for (int i=0; i<currUserReceipts.size(); i++) {
                if (recId == currUserReceipts.get(i).getId()) {
                    recIdx = i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Receipt is not found!");
                System.out.println("Press any key to continue . . .");
                scan.nextLine();
                continue;
            }
            break;
        } while (true);

        displayReceipt(recIdx);
    }

    private void displayReceipt(int recIdx) {
        Receipt currUserReceipt = users.get(currUserIdx).getReceipts().get(recIdx);
        ArrayList<Product> currUserReceiptsProducts = currUserReceipt.getProducts();

        clear();
        System.out.println("Padie Shop");
        System.out.println("---------------------------------------");
        System.out.printf("ID: #%d\n\n", currUserReceipt.getId());
        for (int i=0; i<currUserReceiptsProducts.size(); i++) {
            if (currUserReceiptsProducts.get(i) instanceof Food) {
                Food food = (Food)currUserReceiptsProducts.get(i);
                System.out.printf("%d. %s - Rp %d\n", i+1, food.getName(), food.getPrice());
                System.out.printf("    - Expire date: %s\n", food.getExpiredDate());
            }
            else if (currUserReceiptsProducts.get(i) instanceof Cloth) {
                Cloth cloth = (Cloth)currUserReceiptsProducts.get(i);
                System.out.printf("%d. %s - Rp %d\n", i+1, cloth.getName(), cloth.getPrice());
                System.out.printf("    - Size: %s\n", cloth.getSize());
            } else {
                Technology tech = (Technology)currUserReceiptsProducts.get(i);
                System.out.printf("%d. %s - Rp %d\n", i+1, tech.getName(), tech.getPrice());
                System.out.printf("    - Version: %s\n", tech.getVersion());
            }
        }
        System.out.println("---------------------------------------");
        System.out.println("Quantity    : " + currUserReceiptsProducts.size());
        System.out.println("Total price : " + currUserReceipt.getTotalPrice());
        System.out.println("\nPress any key to continue . . .");
        scan.nextLine();
    }

    private void addMoney() {
        clear();
        int nominal = 0;
        do {
            int err;
            do {
                err = 0;
                try {
                    System.out.printf("Enter nominal addition [0 to go back to main menu] : ");
                    nominal = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    err = 1;
                    scan.nextLine();
                }
            } while (err == 1);
            if (nominal == 0) {
                return;
            }
            else if (nominal < 0) {
                continue;
            }
            break;
        } while (true);

        int oldMoney = users.get(currUserIdx).getAccountBalance();
        users.get(currUserIdx).setAccountBalance(oldMoney + nominal);
    }

    private void checkAccBalance() {
        clear();
        System.out.println("Your Account Balance : Rp" + users.get(currUserIdx).getAccountBalance() + ",00");
        System.out.println("Click any key to continue . . .");
        scan.nextLine();
    }

    private void login() {
        String username;
        String password;
        System.out.printf("Username : ");
        username = scan.nextLine();
        System.out.printf("Password : ");
        password = scan.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            adminMenu();
            return;
        }

        for (int i=0; i<users.size(); i++) {
            if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password)) {
                System.out.println("Login successful");
                System.out.println("Press any key to continue . . .");
                currUserIdx = i;
                cart = users.get(currUserIdx).getCart();
                mainMenu();
                return;
            }
        }
        System.out.println("The user is not registered in the application");
        System.out.println("Press any key to continue . . .");
        scan.nextLine();
    }

    private void registerUser() {
        String username;
        String fullName;
        String email;
        String password;

        clear();
        do {
            System.out.printf("Username : ");
            username = scan.nextLine();
            if (!isName(username)) {
                continue;
            }
            break;
        } while (true);

        do {
            System.out.printf("Fullname : ");
            fullName = scan.nextLine();
            if(!isFullName(fullName)) {
                continue;
            }
            break;
        } while (true);

        do {
            System.out.printf("Email : ");
            email = scan.nextLine();
            if (!isEmail(email)) {
                continue;
            }
            break;
        } while (true);

        do {
            System.out.printf("Password : ");
            password = scan.nextLine();
            if (!isPassword(password)) {
                continue;
            }
            break;
        } while (true);

        users.add(new User(username, fullName, email, password));
        System.out.println("New User has been created successfully!");
        System.out.println("Click any button to continue . . .");
        scan.nextLine();
    }

    private boolean isName(String uname) {
        int len = uname.length();
        if (len >= 3 && len <= 16) {
            return true;
        }
        return false;
    }

    private boolean isFullName(String fullName) {
        if (!isName(fullName)) {
            return false;
        }
        for (int i=0; i<fullName.length(); i++) {
            if ((fullName.charAt(i) >= 'A' && fullName.charAt(i) <= 'Z') || (fullName.charAt(i) >= 'a' && fullName.charAt(i) <= 'z') || fullName.charAt(i) == ' ') {
                continue;
            }
            System.out.println("bug");
            return false;
        }
        return true;
    }

    private boolean isEmail(String email) {
        int len = email.length();
        if (len < 5 || len > 16) {
            return false;
        }

        boolean adaAdd = false;
        for (int i=0; i<len; i++) {
            if (email.charAt(i) == '@') {
                adaAdd = true;
            }
        }

        return adaAdd && email.endsWith(".com") || email.endsWith(".net");
    }

    private boolean isPassword(String password) {
        int len = password.length();
        if (len < 3 || len > 40) {
            return false;
        }

        boolean angka = false;
        boolean huruf = false;
        for (int i=0; i<len; i++) {
            if (password.charAt(i) >= 65 && password.charAt(i) <= 90 || password.charAt(i) >= 97 && password.charAt(i) <= 122) {
                huruf = true;
            }
            if (password.charAt(i) >= 48 && password.charAt(i) <= 57) {
                angka = true;
            }
        }
        return angka && huruf;
    }

    public static void main(String[] args) {
        new Main();
    }

    private void clear() {
        for (int i=0; i<50; i++) {
            System.out.println();
        }
    }
}
