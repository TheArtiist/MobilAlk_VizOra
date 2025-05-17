package com.example.onlinevizoralejelentes;

import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class AsyncTaskWaterMeter extends android.os.AsyncTask<Void, Void, String> {
    private WeakReference<TextView> textView;
    public AsyncTaskWaterMeter(TextView textView){
        this.textView = new WeakReference<>(textView);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Random random = new Random();
        int number = random.nextInt(11);
        int ms = number * 300;

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ""; //Valami szöveg amire kicserélem az egyik gomb szövegét + ms + "után"
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        textView.get().setText(s);
    }
}
