package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensetracker.Adapters.TransactionsAdapter;
import com.example.expensetracker.Model.Transaction;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.viewmodels.MainViewModel;
import com.example.expensetracker.views.fragments.Add_Transaction_Fragment;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;
   Calendar calendar;
    /*
        0 = Daily
        1 = Monthly
        2 = Calendar
        3 = Summary
        4 = Notes
         */

  public MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.materialToolbar2);
        getSupportActionBar().setTitle("Transaction");
        Constants.setCatagories();
        calendar=Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{
            if(Constants.SELECTED_TAB==Constants.DAILY){
                calendar.add(Calendar.DATE,1);
            }
            else if(Constants.SELECTED_TAB==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,1);
            }
            updateDate();
        });

        binding.PreviousDateBtn.setOnClickListener(c->{
            if(Constants.SELECTED_TAB==Constants.DAILY){
                calendar.add(Calendar.DATE,-1);
            }
            else if(Constants.SELECTED_TAB==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,-1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c ->{
            new Add_Transaction_Fragment().show(getSupportFragmentManager(),null);
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB=1;
                    updateDate();
                }
                else if(tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB=0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        ArrayList<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction(Constants.INCOME, "Rent", "Cash", "note1", new Date(), 500, 2));
//      //  transactions.add(new Transaction("Expense", "Investment", "Bank", "note2", new Date(), 900, 4));
//        transactions.add(new Transaction(Constants.INCOME, "Rent", "Bank", "note2", new Date(), -500, 5));
//        transactions.add(new Transaction(Constants.INCOME, "Bank", "Cash", "note3", new Date(), 500, 6));

        binding.trasactionList.setLayoutManager(new LinearLayoutManager(this));
        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);

                binding.trasactionList.setAdapter(transactionsAdapter);
               if(transactions.size()>0){
                   binding.emptyState.setVisibility(View.GONE);

               }
               else{
                   binding.emptyState.setVisibility(View.VISIBLE);
               }
            }
        });
        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                  binding.incomelbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenselbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totallbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);
        // Initialize and set up RecyclerView and adapter


    }
    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }

    void updateDate(){
//        SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM,YYYY");
        //*faruk
       if(Constants.SELECTED_TAB==0){
           binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
       }
       else{
           binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
       }
        viewModel.getTransactions(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}