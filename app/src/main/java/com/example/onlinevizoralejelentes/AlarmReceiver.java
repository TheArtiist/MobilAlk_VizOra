package com.example.onlinevizoralejelentes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new Notifications(context).send("It's time to upload the WaterMeter");
    }
}
