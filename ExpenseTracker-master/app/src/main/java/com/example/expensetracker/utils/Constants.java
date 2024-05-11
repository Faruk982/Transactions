package com.example.expensetracker.utils;

import com.example.expensetracker.Model.Category;
import com.example.expensetracker.R;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";
    public static ArrayList<Category> categories;
    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int CALENDAR = 2;
    public static int SUMMARY = 3;
    public static int NOTES = 4;
    public static int SELECTED_TAB=0;
    public static void setCatagories(){
        categories=new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.baseline_business_center_24,R.color.category1));
        categories.add(new Category("Bank",R.drawable.baseline_analytics_24,R.color.category2));
        categories.add(new Category("Loan",R.drawable.baseline_attach_money_24,R.color.category3));
        categories.add(new Category("Investment",R.drawable.baseline_checkroom_24,R.color.category4));
        categories.add(new Category("Rent",R.drawable.baseline_chair_24,R.color.category5));
        categories.add(new Category("Other",R.drawable.baseline_bar_chart_24,R.color.category6));

    }
    public static Category getCategoryDetails(String categoryName) {
        for (Category cat :
                categories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }
    public static int getAccountsColor(String accountName) {
        switch (accountName) {
            case "Bank":
                return R.color.greenColor;
            case "Cash":
                return R.color.black;
            case "Card":
                return R.color.orange;
            default:
                return R.color.gray;
        }
    }
}
