package com.example.expensetracker.Adapters;
import com.example.expensetracker.Model.Category;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Model.Transaction;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.RowTransactionsBinding;
import com.example.expensetracker.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

//import com.example.expensetracker.databinding.RowTransactionBinding;
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{
            Context context;
        RealmResults<Transaction> transactions;


        public TransactionsAdapter(Context context, RealmResults<Transaction> transactions) {
            this.context = context;
            this.transactions = transactions;
//            for (Transaction transaction : transactions) {
//              //  Toast.makeText(context, transaction.getNote(), Toast.LENGTH_LONG).show();
//            }
        }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transactions, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
                    Transaction transaction = transactions.get(position);

            holder.binding.amount.setText(String.valueOf(transaction.getAmount()));
            holder.binding.accountLable.setText(transaction.getAccount());
            holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
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
             holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v) {
                     AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                     deleteDialog.setTitle("Delete Transaction");
                     deleteDialog.setMessage("Are you sure to delete this transaction?");
                     deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                         ((MainActivity)context).viewModel.deleteTransaction(transaction);
                     });
                     deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> {
                            deleteDialog.dismiss();
                     });
                     deleteDialog.show();
                     return false;
                 }
             });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        RowTransactionsBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionsBinding.bind(itemView);
        }
    }

}
