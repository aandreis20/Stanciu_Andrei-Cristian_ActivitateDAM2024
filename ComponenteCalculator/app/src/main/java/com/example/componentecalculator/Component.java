package com.example.componentecalculator;

public class Component {
    private String name;
    private String category;
    private int price;
    private boolean availability;
    private int quantity;

    public Component() {
        this.name = "i5";
        this.category = "Procesoare";
        this.price = 1000;
        this.availability = true;
        this.quantity = 10;
    }


    public Component(String name,
                     String category,
                     int price,
                     boolean availability,
                     int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.availability = availability;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Component{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                ", quantity=" + quantity +
                '}';
    }
}
