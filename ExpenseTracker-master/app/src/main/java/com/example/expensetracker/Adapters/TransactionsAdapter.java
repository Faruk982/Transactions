package com.example.expensetracker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Model.Category;
import com.example.expensetracker.Model.Transaction;
import com.example.expensetracker.Model.TransactionFactory;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.RowTransactionsBinding;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.views.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{
    @NonNull
    Context context;
    ArrayList<Transaction>transactions;
    public TransactionsAdapter(Context context,ArrayList<Transaction>transactions){
        this.context=context;
        this.transactions=transactions;
    }
    @Override
    public TransactionsAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transactions, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.binding.amount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.accountLable.setText(transaction.getAccount());
        holder.binding.transactionDate.setText((transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());
        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());
        holder.binding.transactionIcon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.transactionIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));
        holder.binding.accountLable.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));

        if(transaction.getType().equals(Constants.INCOME)){
            holder.binding.amount.setTextColor(R.color.greenColor);
        }
        else if(transaction.getType().equals(Constants.EXPENSE)){
            holder.binding.amount.setTextColor(R.color.redColor);
        }
        // Bind other data accordingly
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
    public void removeItem(int position) {
        transactions.remove(position); // Remove item from the list

        // Notify the adapter that item has been removed
        notifyItemRemoved(position);
    }
    public void removeFromFirebase(Transaction transaction){
        long idd=transaction.getId();
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
                    if(id==idd) {
                        transactionSnapshot.getRef().removeValue().addOnSuccessListener(aVoid->{

                        }).addOnFailureListener(e->{

                        });
                    }
                }

//                // Update UI with fetched transactions
//                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);
//                binding.trasactionList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                binding.trasactionList.setAdapter(transactionsAdapter);

                // Display toast to show number of transactions fetched
                //Toast.makeText(MainActivity.this, "Transactions loaded: " + transactions.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle data retrieval cancellation or error
                Log.e("MainActivity", "Error fetching transactions: " + error.getMessage());
               // Toast.makeText(MainActivity.this, "Failed to fetch transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        RowTransactionsBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionsBinding.bind(itemView);
            itemView.setOnLongClickListener(v -> {
                // Handle long click here
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Transaction clickedTransaction = transactions.get(position);
                    removeFromFirebase(clickedTransaction);
                    // Example: Perform an action on the clicked transaction
                    Toast.makeText(itemView.getContext(), "Long click on transaction: " + clickedTransaction.getType(), Toast.LENGTH_SHORT).show();
                    removeItem(position);
                }
                return true; // Consume the long click event
            });
        }
    }
}
