package com.example.mypersonalbudget.entities;

public class Transaction {

    private String category;
    private String title;
    private float amount;

    public Transaction(String category, String title, float amount) {
        this.category = category;
        this.title = title;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public float getAmount() {
        return amount;
    }
}
