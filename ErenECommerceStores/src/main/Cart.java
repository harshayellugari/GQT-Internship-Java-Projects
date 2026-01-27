package main;

class Cart {
    Product[] items = new Product[50];
    int count = 0;

    void add(Product p) {
        items[count++] = p;
        System.out.println("Product added to cart");
    }

    double totalAmount() {
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum += items[i].price;
        }
        return sum;
    }

    void showCart() {
        for (int i = 0; i < count; i++) {
            System.out.println(items[i].name + " - ₹" + items[i].price);
        }
    }
}
