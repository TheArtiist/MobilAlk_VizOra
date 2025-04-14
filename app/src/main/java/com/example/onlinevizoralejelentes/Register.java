package com.example.onlinevizoralejelentes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
private FirebaseAuth mAuth;
private final String  LOG_TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       // Bundle budle = getIntent().getExtras();
        //int secret_key = budle.getInt("SECRET_KEY");
        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);

        mAuth = FirebaseAuth.getInstance();

    }

    public void firstRegButton(View view){
        EditText emailET = findViewById(R.id.email2EditText);
        EditText passwordET = findViewById(R.id.password2EditText);
        EditText passwordET2 = findViewById(R.id.password22EditText);

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String password2 = passwordET2.getText().toString();

        if(!password.equals(password2)){
            Log.e(LOG_TAG,"Hibás jelszó, nem egyezik meg a kettő jelszó!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"User created successfully");
                    goMain();
                }else{
                    Log.d(LOG_TAG,"User was not created");
                    Toast.makeText(Register.this,"User was not created", Integer.parseInt(task.getException().getMessage())).show();
                }
            }
        });

    }

    public void goMain(){
        Intent intent = new Intent(this, MainPage.class);
       // intent.putExtra("SECRET_KEY",10);
        startActivity(intent);
    }

}