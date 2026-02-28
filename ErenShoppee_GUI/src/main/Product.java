package main;

public class Product {
    public int id;
    public String name;
    public String brand;
    public String description;
    public double price;
    public int stock;

    public Product(int id, String name, String brand, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }
}
