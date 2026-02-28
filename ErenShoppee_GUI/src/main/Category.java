package main;

public class Category {
    public String name;
    public SubCategory[] subCategories = new SubCategory[4];

    public Category(String name) {
        this.name = name;
    }
}
