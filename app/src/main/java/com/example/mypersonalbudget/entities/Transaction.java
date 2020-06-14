package com.example.mypersonalbudget.entities;

public class Transaction {

    private String category;
    private String title;
    private float amount;
    private String date;

    public Transaction(String category, String title, float amount, String date) {
        this.category = category;
        this.title = title;
        this.amount = amount;
        this.date = date;
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

    public String getDate() {
        return date;
    }
}
