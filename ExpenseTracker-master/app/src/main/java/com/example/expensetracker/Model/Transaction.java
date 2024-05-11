package com.example.expensetracker.Model;

import java.util.Date;

public interface Transaction {
    String getType();
    String getCategory();
    String getAccount();
    String getNote();
    String getDate();
    double getAmount();
    long getId();
}
