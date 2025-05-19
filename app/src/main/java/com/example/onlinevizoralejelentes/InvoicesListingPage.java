package com.example.onlinevizoralejelentes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class InvoicesListingPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InvoiceHistoryAdapter adapter;
    private List<Invoices> invoicesList;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invoices_listing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            fetchUserInvoices(user.getEmail());
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        invoicesList = new ArrayList<>();
        adapter = new InvoiceHistoryAdapter(invoicesList);
        recyclerView.setAdapter(adapter);

        loadInvoices();
    }

    private void loadInvoices() {
        db.collection("meter_readings")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        invoicesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Invoices invoice = document.toObject(Invoices.class);
                            invoicesList.add(invoice);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    public void goBack(View view){
        Intent intent = new Intent(this,MainPage.class);
        startActivity(intent);
    }

    private void fetchUserInvoices(String userEmail) {
        Log.d("Firestore", "Lekérdezés indítva...");
        db.collection("invoices")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        invoicesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Invoices invoice = document.toObject(Invoices.class);
                            invoicesList.add(invoice);
                        }
                        Log.d("Firestore", "Lekérdezett számlák száma: " + invoicesList.size());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }





}