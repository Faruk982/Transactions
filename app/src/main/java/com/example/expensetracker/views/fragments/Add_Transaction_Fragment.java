package com.example.expensetracker.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensetracker.Adapters.AccountsAdapter;
import com.example.expensetracker.Adapters.CategoryAdapter;
import com.example.expensetracker.Model.Account;
import com.example.expensetracker.Model.Category;
import com.example.expensetracker.Model.Transaction;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.FragmentAddTransactionBinding;
import com.example.expensetracker.databinding.ListDialogueBinding;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class Add_Transaction_Fragment extends BottomSheetDialogFragment {


    public Add_Transaction_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddTransactionBinding.inflate(inflater);
         transaction = new Transaction();
        binding.income.setOnClickListener(view -> {
            binding.income.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expense.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expense.setTextColor(getContext().getColor(R.color.textColor));
            binding.income.setTextColor(getContext().getColor(R.color.greenColor));
           transaction.setType(Constants.INCOME);
           // transaction.setType(Constants.INCOME);
        });

        binding.expense.setOnClickListener(view -> {
            binding.income.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expense.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.income.setTextColor(getContext().getColor(R.color.textColor));
            binding.expense.setTextColor(getContext().getColor(R.color.redColor));
            transaction.setType(Constants.EXPENSE);
         //   transaction.setType(Constants.EXPENSE);
        });


        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

//                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                    //*faruk
                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);
                    transaction.setDate(calendar.getTime());
                    transaction.setId(calendar.getTime().getTime());
                //    transaction.setDate(calendar.getTime());
                  //  transaction.setId(calendar.getTime().getTime());
                });
                datePickerDialog.show();
            }
        });


        binding.category.setOnClickListener(c-> {
            ListDialogueBinding dialogBinding = ListDialogueBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());
//*faruk sent it to Contants
//            ArrayList<Category> categories = new ArrayList<>();
//            categories.add(new Category("Salary",R.drawable.baseline_business_center_24,R.color.category1));
//            categories.add(new Category("Bank",R.drawable.baseline_analytics_24,R.color.category2));
//            categories.add(new Category("Loan",R.drawable.baseline_attach_money_24,R.color.category3));
//            categories.add(new Category("Investment",R.drawable.baseline_checkroom_24,R.color.category4));
//            categories.add(new Category("Rent",R.drawable.baseline_chair_24,R.color.category5));
//            categories.add(new Category("Other",R.drawable.baseline_bar_chart_24,R.color.category6));

            CategoryAdapter categoryAdapter=new CategoryAdapter(getContext(),Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });

        binding.account.setOnClickListener(c-> {
            ListDialogueBinding dialogBinding = ListDialogueBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(0, "Cash"));
            accounts.add(new Account(0, "Bank"));
            accounts.add(new Account(0, "PayTM"));
            accounts.add(new Account(0, "EasyPaisa"));
            accounts.add(new Account(0, "Other"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountsDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);

            accountsDialog.show();

        });
        binding.saveTransactionBtn.setOnClickListener(c->{
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.note.getText().toString();
            if(transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(amount*-1);
            } else {
                transaction.setAmount(amount);
            }

            transaction.setNote(note);
            ((MainActivity)getActivity()).viewModel.addTransactions(transaction);
            ((MainActivity)getActivity()).getTransactions();
            dismiss();
        });


        return binding.getRoot();
    }
}