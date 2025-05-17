package com.example.onlinevizoralejelentes;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LocationInfoActivity extends AppCompatActivity {
    private ImageView spinningImage;
    private Animation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinningImage = findViewById(R.id.spinningImage);

        // Load animation
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.spinin_anim);
        spinningImage.startAnimation(rotateAnimation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinningImage.startAnimation(rotateAnimation);  // Starts spinning when user is active
    }

    @Override
    protected void onPause() {
        super.onPause();
        spinningImage.clearAnimation();  // Stops spinning when user leaves the page
    }

    public void saveLocation(View view){

    }

    public void cancelButton(View view){

    }
}