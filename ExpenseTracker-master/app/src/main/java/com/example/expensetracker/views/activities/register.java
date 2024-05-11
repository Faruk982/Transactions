package com.example.expensetracker.views.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.expensetracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {

    private EditText editTextRegisterName, editTextRegisterEmail, editTextRegisterDob, editTextRegisterMobile, editTextRegisterPwd, editTextRegisterConfirmPwd;

    private RadioGroup radioGroupGender;

    private RadioButton radioButtonGenderSelected;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);


        Toast.makeText(this, "You can register here!", Toast.LENGTH_SHORT).show();
        editTextRegisterName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDob = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);

        radioGroupGender = findViewById(R.id.radio_group_register_gender);
        radioGroupGender.clearCheck();

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextRegisterName.getText().toString().trim();
                String email = editTextRegisterEmail.getText().toString().trim();
                String dob = editTextRegisterDob.getText().toString().trim();
                String mobile = editTextRegisterMobile.getText().toString().trim();
                String password = editTextRegisterPwd.getText().toString().trim();

                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                radioButtonGenderSelected = findViewById(selectedGenderId);

                String textGender;

                if (TextUtils.isEmpty(name)) {
                    editTextRegisterName.setError("Name is required");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    editTextRegisterEmail.setError("Email is required");
                    return;
                } else if (TextUtils.isEmpty(dob)) {
                    editTextRegisterDob.setError("Date of Birth is required");
                    return;
                } else if (TextUtils.isEmpty(mobile)) {
                    editTextRegisterMobile.setError("Mobile is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    editTextRegisterPwd.setError("Password is required");
                    return;
                } else if (selectedGenderId == -1) {
                    Toast.makeText(register.this, "Please Select Your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    textGender = radioButtonGenderSelected.getText().toString();
                    registerUser(name, email, dob, mobile, password);
                }


            }
        });
    }

    private void registerUser(String name, String email, String dob, String mobile, String password) {
        // Register user

        Toast.makeText(register.this, "Registering User", Toast.LENGTH_LONG).show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(register.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete (@NonNull Task< AuthResult > task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    ReadWriteUserDetails readWriteUserDetails = new ReadWriteUserDetails(name,email,dob,mobile,password);
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                                    databaseReference.child(user.getUid()).setValue(readWriteUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(register.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(register.this, login.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(register.this, "User Registration Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

// Sign in success, update UI with the signed-in user's information


//                                    Intent intent = new Intent(register.this, UserProfile.class);
//                                    startActivity(intent);
                                }
                                else {
// If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                );






    }
}