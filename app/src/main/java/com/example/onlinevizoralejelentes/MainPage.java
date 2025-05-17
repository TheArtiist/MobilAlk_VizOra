package com.example.onlinevizoralejelentes;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainPage extends AppCompatActivity {
    private static final String LOG_TAG = MainPage.class.getName();
    private TextView movingText;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        movingText = findViewById(R.id.titleTextView);


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;


        animator = ObjectAnimator.ofFloat(movingText, "translationX", 0, screenWidth - movingText.getWidth());
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.start();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG,"Authenticated user");
        }else{
            Log.d(LOG_TAG,"NOT authenticated user");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        animator.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animator.cancel();
    }



    public void logoutButton(View view){
        Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("SECRET_KEY",123456789);
        startActivity(intent);
    }

    public void waterMeterReportButton(View view){
        Intent intent = new Intent(this, LocationInfoActivity.class);
        startActivity(intent);
    }
}