package main;
class Product {
    int id;
    String name;
    String brand;
    String description;
    double price;
    int stock;
    Product(int id, String name, String brand,
            String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }
    void displayShort() {
        System.out.println(id + ". " + name + " - ₹" + price);
    }
    void displayFull() {
        System.out.println("ID          : " + id);
        System.out.println("Name        : " + name);
        System.out.println("Brand       : " + brand);
        System.out.println("Description : " + description);
        System.out.println("Price       : ₹" + price);
        System.out.println("Stock       : " + stock);
    }
}



