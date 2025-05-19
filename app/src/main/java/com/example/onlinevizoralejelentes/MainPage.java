package com.example.onlinevizoralejelentes;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import androidx.credentials.provider.PendingIntentHandler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainPage extends AppCompatActivity {
    private static final String LOG_TAG = MainPage.class.getName();
    private TextView movingText;
    private ObjectAnimator animator;

    private Notifications mNotifications;
    private AlarmManager mAlarmManager;

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

        mNotifications = new Notifications(this);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

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
        setAlarmManager();
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

    public void invoiceMenu(View view){
        Intent intent = new Intent(this, InvoicesListingPage.class);
        startActivity(intent);
    }



    public void logoutButton(View view){
        Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("SECRET_KEY",123456789);
        startActivity(intent);
    }

    public void waterMeterReportButton(View view){
        Intent intent = new Intent(this, LocationInfoActivity.class);
        startActivity(intent);
        mNotifications.send("Vízóra feltöltés elindítva");
    }

    private void setAlarmManager(){
        long repeatInterval = 60*1000;//AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this,AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(this,0,intent,FLAG_IMMUTABLE);
        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingintent
        );

        mAlarmManager.cancel(pendingintent);

    }

}