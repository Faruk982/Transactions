package com.example.expensetracker.Model;

import java.util.Date;

public class TransactionFactory {
    public Transaction getTransaction(String transactionType, String category, String account, String note, String date, double amount, long id) {
        if (transactionType == null) {
            return null;
        }
        if (transactionType.equalsIgnoreCase("INCOME")) {
            return new Income(category, account, note, date, amount, id);
        } else if (transactionType.equalsIgnoreCase("EXPENSE")) {
            return new Expense(category, account, note, date, amount, id);
        }
        return null;
    }
}
