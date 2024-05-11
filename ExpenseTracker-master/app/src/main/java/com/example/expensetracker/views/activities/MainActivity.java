package com.example.expensetracker.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensetracker.Adapters.TransactionsAdapter;
import com.example.expensetracker.Model.Transaction;
import com.example.expensetracker.Model.TransactionFactory;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.views.fragments.Add_Transaction_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;
   Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
            calendar.add(Calendar.DATE,1);
            updateDate();
        });

        binding.PreviousDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE,-1);
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c ->{
            new Add_Transaction_Fragment().show(getSupportFragmentManager(),null);
        });

        ArrayList<Transaction>transactions=new ArrayList<>();
        TransactionFactory transactionFactory=new TransactionFactory();
       // transactions.add(transactionFactory.getTransaction(Constants.INCOME,"Bank","Cash","Hello", Helper.formatDate(new Date()),500,2));

//       if(transactions.size()>0){
//           TransactionsAdapter transactionsAdapter =new TransactionsAdapter(this,transactions);
//           binding.trasactionList.setLayoutManager(new LinearLayoutManager(this));
//           binding.trasactionList.setAdapter(transactionsAdapter);
//       }
    }
//    void fetchTransactionsFromFirebase(String fetchinDate) {
//        ArrayList<Transaction> transactions = new ArrayList<>();
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser == null) {
//            // User is not authenticated, return empty list or handle accordingly
//            return ;//transactions;
//        }
//
//        String userID = firebaseUser.getUid();
//        DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference("Users")
//                .child(userID)
//                .child("transactions");
//        transactionsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//              if(task.isSuccessful()){
//                  DataSnapshot snapshot=task.getResult();
//
//               //   ArrayList<Transaction> transactions = new ArrayList<>();
//
//                  for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
//
//                      String type = transactionSnapshot.child("type").getValue(String.class);
//
//                      String category = transactionSnapshot.child("category").getValue(String.class);
//
//                      String account = transactionSnapshot.child("account").getValue(String.class);
//                      String note = transactionSnapshot.child("note").getValue(String.class);
//                      String dateString = transactionSnapshot.child("date").getValue(String.class);
//
//                    double amount = transactionSnapshot.child("amount").getValue(Double.class);
//                   long   id = transactionSnapshot.child("id").getValue(long.class);
//
//
//                      // Parse dateString to Date object (assuming date is stored as a string)
//                      String date = (dateString);
//
//                      // Create Transaction object
//                      TransactionFactory transactionFactory = new TransactionFactory();
//                      Transaction transaction = transactionFactory.getTransaction(type, category, account, note, date, amount, id);
//                      //Toast.makeText(MainActivity.this, transaction.getDate(),Toast.LENGTH_LONG).show();
//                      transactions.add(transaction);
//                      Toast.makeText(MainActivity.this, String.valueOf(transactions.size()),Toast.LENGTH_LONG).show();
//                  }
//              }
//            }
//        });
//        Toast.makeText(MainActivity.this, String.valueOf(transactions.size()),Toast.LENGTH_LONG).show();
////         for(int i=0;i<transactions.size();i++){
////             Transaction transaction=transactions.get(i);
////             Toast.makeText(MainActivity.this, transaction.getDate(),Toast.LENGTH_LONG).show();
////         }
//        TransactionsAdapter transactionsAdapter =new TransactionsAdapter(this,transactions);
//        binding.trasactionList.setLayoutManager(new LinearLayoutManager(this));
//        binding.trasactionList.setAdapter(transactionsAdapter);
//
//        return ;//transactions; // Return the fetched transactions
//    }

    void fetchTransactionsFromFirebase(String fetchinDate) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            // User is not authenticated, handle accordingly (e.g., show login screen)
            return;
        }

        String userID = firebaseUser.getUid();
        DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(userID)
                .child("transactions");

        transactionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    String type = transactionSnapshot.child("type").getValue(String.class);
                    String category = transactionSnapshot.child("category").getValue(String.class);
                    String account = transactionSnapshot.child("account").getValue(String.class);
                    String note = transactionSnapshot.child("note").getValue(String.class);
                    String dateString = transactionSnapshot.child("date").getValue(String.class);
                    double amount = transactionSnapshot.child("amount").getValue(Double.class);
                    long id = transactionSnapshot.child("id").getValue(Long.class);

                    // Create Transaction object
                    TransactionFactory transactionFactory = new TransactionFactory();
                    String date = dateString; // Assuming dateString is a valid date string
                    Transaction transaction = transactionFactory.getTransaction(type, category, account, note, date, amount, id);
                 if(date.equals(fetchinDate))   transactions.add(transaction);
                }

                // Update UI with fetched transactions
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);
                binding.trasactionList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                binding.trasactionList.setAdapter(transactionsAdapter);

                // Display toast to show number of transactions fetched
                //Toast.makeText(MainActivity.this, "Transactions loaded: " + transactions.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle data retrieval cancellation or error
                Log.e("MainActivity", "Error fetching transactions: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void updateDate(){
//        SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM,YYYY");
        binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        fetchTransactionsFromFirebase(Helper.formatDate(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Handle other menu items if needed

        if (id == R.id.profile_section) {
            // Handle profile icon click action
            // For example, navigate to the profile activity
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}