package com.example.expensetracker.Model;

import java.util.Date;

public class Expense implements Transaction{
    private String category;
    private String account;
    private String note;
    private String date;
    private double amount;
    private long id;

    public Expense(String category, String account, String note, String date, double amount, long id) {
        this.category = category;
        this.account = account;
        this.note = note;
        this.date = date;
        this.amount = amount;
        this.id = id;
    }

    @Override
    public String getType() {
        return "Expense";
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public long getId() {
        return id;
    }
}
