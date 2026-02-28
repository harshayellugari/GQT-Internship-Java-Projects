package main;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    public List<Product> items = new ArrayList<>();

    public void add(Product p) {
        items.add(p);
    }

    public void remove(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public double totalAmount() {
        return items.stream().mapToDouble(p -> p.price).sum();
    }

    public int count() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }
}
