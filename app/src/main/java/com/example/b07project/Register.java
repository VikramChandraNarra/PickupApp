package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    Switch mCheckadmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = findViewById(R.id.Email);
        mRegisterBtn = (Button) findViewById(R.id.RegisterButton);
        mLoginBtn = findViewById(R.id.createText);
        mPassword = findViewById(R.id.Password);
        mCheckadmin = (Switch)findViewById(R.id.AdminCheck);
        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password has to be >= 6 characters");
                }
                // register user
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this,"User Created", Toast.LENGTH_SHORT).show();
                            Admin a = new Admin(email,null);
                          DB_Write.createAdmin(a);
                            if (mCheckadmin.isChecked()){
                                SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                                editor.putBoolean("value",true);
                                editor.apply();
//

                                startActivity(new Intent(getApplicationContext(),AdminMain.class));
                            }else {
                                SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                                editor.putBoolean("value",false);
                                editor.apply();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }
                        else{
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }




        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


    }}