package com.example.onlinevizoralejelentes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

public class MainActivity extends AppCompatActivity {
    private final String  LOG_TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;
    private static final int SECRET_KEY= 123456789;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void loginButton(View view) {
        EditText emailET = findViewById(R.id.emailEditText);
        EditText passwordET = findViewById(R.id.passwordEditText);

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        //Log.i(LOG_TAG, "Bejelentkezett: " + email +" Jelszó: "+password);

        /*
        Próba account:
        Email: teszter@gmail.com
        Jelszó: jelszo123
        */
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"User login successeded");
                    goMain();
                }else{
                    Log.d(LOG_TAG,"User login failed");
                    Toast.makeText(MainActivity.this,"User was not loggedd in", Integer.parseInt(task.getException().getMessage())).show();
                }
            }

        });

    }

    public void goMain(){
        Intent intent = new Intent(this, MainPage.class);
        // intent.putExtra("SECRET_KEY",10);
        startActivity(intent);
    }

    public void registerButton(View view){
        Intent intent = new Intent(this,Register.class);
        //intent.putExtra("SECRET_KEY",123456789);
        startActivity(intent);

    }

}