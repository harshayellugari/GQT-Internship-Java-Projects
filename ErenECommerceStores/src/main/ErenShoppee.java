package main;

import java.util.Scanner;

public class ErenShoppee {
    static Scanner sc = new Scanner(System.in);
    static Category[] categories = new Category[5];
    static Cart cart = new Cart();
    public static void main(String[] args) {
        loadData();
        while (true) {
            System.out.println(Color.CYAN + "\n--- E COMMERCE APPLICATION ---" + Color.RESET);
            for (int i = 0; i < categories.length; i++) {
                System.out.println(Color.YELLOW + (i + 1) + ". " + categories[i].name + Color.RESET);
            }
            System.out.println(Color.RED + "0. Checkout" + Color.RESET);

            int choice = sc.nextInt();

            if (choice == 0) {
                checkout();
                break;
            }
            showSubCategories(categories[choice - 1]);
        }
    } 
    static void showSubCategories(Category category) {
        while (true) {
            System.out.println(Color.BLUE + "\n" + category.name + Color.RESET);
            for (int i = 0; i < 4; i++) {
                System.out.println(Color.YELLOW + (i + 1) + ". " + category.subCategories[i].name + Color.RESET);
            }
            System.out.println(Color.RED + "0. Back" + Color.RESET);
            int choice = sc.nextInt();
            if (choice == 0) {
                return;
            }
            showProducts(category.subCategories[choice - 1]);
        }
    }
    static void showProducts(SubCategory sub) {
        while (true) {
            System.out.println(Color.PURPLE + "\n" + sub.name + Color.RESET);
            for (int i = 0; i < 10; i++) {
                System.out.print(Color.YELLOW);
                sub.products[i].displayShort();
                System.out.print(Color.RESET);
            }
            System.out.println(Color.RED + "0. Back" + Color.RESET);
            System.out.println(Color.CYAN + "Enter product id:" + Color.RESET);
            int pid = sc.nextInt();

            if (pid == 0) {
                return;
            }
            Product selected = sub.products[pid - 1];
            System.out.println(Color.GREEN);
            selected.displayFull();
            System.out.print(Color.RESET);
            System.out.println(Color.YELLOW + "1. Add to Cart" + Color.RESET);
            System.out.println(Color.YELLOW + "2. Buy Now" + Color.RESET);
            System.out.println(Color.RED + "0. Back" + Color.RESET);
            int option = sc.nextInt();
            if (option == 0) {
                continue;
            }
            cart.add(selected);
            if (option == 2) {
                checkout();
                System.exit(0);
            }
        }
    }
    static void checkout() {
        System.out.println(Color.GREEN + "\n--- CHECKOUT ---" + Color.RESET);
        cart.showCart();
        double total = cart.totalAmount();
        System.out.println(Color.CYAN + "Total Amount: ₹" + total + Color.RESET);
        payment();
    }   
    static void payment() {
        System.out.println(Color.BLUE + "Enter Card Number:" + Color.RESET);
        sc.next();
        System.out.println(Color.BLUE + "Enter CVV:" + Color.RESET);
        sc.next();
        System.out.println(Color.BLUE + "Enter OTP:" + Color.RESET);
        sc.next();
        System.out.println(Color.GREEN + "Payment Successful" + Color.RESET);
        System.out.println(Color.GREEN + "Your order is placed successfully" + Color.RESET);
    }
        static void loadData() {
            // ================= CATEGORY 1 =================
            Category electronics = new Category("Electronics");
            SubCategory mobiles = new SubCategory("Mobiles");
            mobiles.products[0] = new Product(1,"iPhone 15","Apple","128GB, A17 Chip",79999,20);
            mobiles.products[1] = new Product(2,"Galaxy S23","Samsung","5G AMOLED Display",69999,15);
            mobiles.products[2] = new Product(3,"Pixel 8","Google","Pure Android Experience",65999,10);
            mobiles.products[3] = new Product(4,"OnePlus 12","OnePlus","Fast Charging 5G",62999,18);
            mobiles.products[4] = new Product(5,"Nothing Phone 2","Nothing","Glyph Interface",45999,12);
            mobiles.products[5] = new Product(6,"Redmi Note 13","Xiaomi","Budget 5G Phone",18999,30);
            mobiles.products[6] = new Product(7,"Realme GT","Realme","Gaming Smartphone",25999,22);
            mobiles.products[7] = new Product(8,"Moto Edge","Motorola","Curved Display",27999,14);
            mobiles.products[8] = new Product(9,"Vivo X100","Vivo","Camera Focused",54999,9);
            mobiles.products[9] = new Product(10,"iQOO Neo","iQOO","Performance Phone",32999,16);
            
            SubCategory laptops = new SubCategory("Laptops");
            laptops.products[0] = new Product(1,"MacBook Air","Apple","M2 Chip, 13 inch",114999,10);
            laptops.products[1] = new Product(2,"MacBook Pro","Apple","M3 Chip",164999,8);
            laptops.products[2] = new Product(3,"Dell XPS","Dell","Ultra Thin Laptop",134999,6);
            laptops.products[3] = new Product(4,"HP Spectre","HP","Premium Build",124999,7);
            laptops.products[4] = new Product(5,"Lenovo Legion","Lenovo","Gaming Laptop",139999,9);
            laptops.products[5] = new Product(6,"Asus ROG","Asus","High Performance Gaming",149999,5);
            laptops.products[6] = new Product(7,"Acer Predator","Acer","RTX Graphics",129999,11);
            laptops.products[7] = new Product(8,"MSI Katana","MSI","Gaming Beast",119999,6);
            laptops.products[8] = new Product(9,"Samsung Galaxy Book","Samsung","AMOLED Laptop",104999,12);
            laptops.products[9] = new Product(10,"Honor MagicBook","Honor","Lightweight Laptop",74999,20);
            
            SubCategory accessories = new SubCategory("Accessories");
            for (int i = 0; i < 10; i++) {
                accessories.products[i] = new Product(
                    i + 1,
                    "Electronic Accessory " + (i + 1),
                    "Generic",
                    "Useful electronic accessory",
                    999 + (i * 200),
                    50
                );
            }
            SubCategory tablets = new SubCategory("Tablets");
            for (int i = 0; i < 10; i++) {
                tablets.products[i] = new Product(
                    i + 1,
                    "Tablet Model " + (i + 1),
                    "BrandTab",
                    "10 inch display tablet",
                    19999 + (i * 1500),
                    25
                );
            }
            electronics.subCategories[0] = mobiles;
            electronics.subCategories[1] = laptops;
            electronics.subCategories[2] = accessories;
            electronics.subCategories[3] = tablets;
            categories[0] = electronics;
            // ================= CATEGORY 2 =================
            Category fashion = new Category("Fashion");
            for (int i = 0; i < 4; i++) {
                fashion.subCategories[i] = new SubCategory("Fashion Type " + (i + 1));
                for (int j = 0; j < 10; j++) {
                    fashion.subCategories[i].products[j] = new Product(
                        j + 1,
                        "Clothing Item " + (j + 1),
                        "BrandWear",
                        "Comfortable daily wear",
                        999 + j * 300,
                        40
                    );
                }
            }
            categories[1] = fashion;
            // ================= CATEGORY 3 =================
            Category home = new Category("Home & Kitchen");
            for (int i = 0; i < 4; i++) {
                home.subCategories[i] = new SubCategory("Home Item Group " + (i + 1));
                for (int j = 0; j < 10; j++) {
                    home.subCategories[i].products[j] = new Product(
                        j + 1,
                        "Home Product " + (j + 1),
                        "HomeBrand",
                        "Essential home utility",
                        1499 + j * 500,
                        35
                    );
                }
            }
            categories[2] = home;
            // ================= CATEGORY 4 =================
            Category books = new Category("Books");
            for (int i = 0; i < 4; i++) {
                books.subCategories[i] = new SubCategory("Book Genre " + (i + 1));
                for (int j = 0; j < 10; j++) {
                    books.subCategories[i].products[j] = new Product(
                        j + 1,
                        "Book Title " + (j + 1),
                        "Author Name",
                        "Popular bestselling book",
                        399 + j * 100,
                        100
                    );
                }
            }
            categories[3] = books;
            // ================= CATEGORY 5 =================
            Category sports = new Category("Sports");
            for (int i = 0; i < 4; i++) {
                sports.subCategories[i] = new SubCategory("Sports Gear " + (i + 1));
                for (int j = 0; j < 10; j++) {
                    sports.subCategories[i].products[j] = new Product(
                        j + 1,
                        "Sports Item " + (j + 1),
                        "FitBrand",
                        "Professional sports equipment",
                        1999 + j * 700,
                        20
                    );
                }
            }
            categories[4] = sports;
        }
}