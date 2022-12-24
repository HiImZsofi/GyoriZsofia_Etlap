package com.example.gyorizsofia_etlap;

public class Meal {
    private int id;
    private String name;
    private String description;
    private int price;
    private String category;

    public Meal(int id, String name, String description, int price, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Meal(String name, String description, int price, String category) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Etel{" +
                "id=" + id +
                ", nev='" + name + '\'' +
                ", leiras='" + description + '\'' +
                ", ar=" + price +
                ", kategoria='" + category + '\'' +
                '}';
    }
}
