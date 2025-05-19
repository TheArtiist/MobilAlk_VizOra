package com.example.onlinevizoralejelentes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InvoiceHistoryAdapter extends RecyclerView.Adapter<InvoiceHistoryAdapter.ViewHolder> {
    private List<Invoices> invoicesList;


    public InvoiceHistoryAdapter(List<Invoices> invoicesList) {
        this.invoicesList = invoicesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Invoices invoice = invoicesList.get(position);
        holder.email.setText(invoice.getEmail());
        holder.address.setText(invoice.getZipCode() + " " + invoice.getVaros() + ", " + invoice.getUtca() + " " + invoice.getHazNum());
        holder.vizOraAllas.setText(String.valueOf(invoice.getVizOraAllas()));

        /*
        // Törlés gomb művelete
        holder.deleteButton.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("invoices").document(invoice.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(v.getContext(), "Számla törölve!", Toast.LENGTH_SHORT).show();
                        invoicesList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(v.getContext(), "Hiba történt a törlés során!", Toast.LENGTH_SHORT).show());
        });*/


    }

    @Override
    public int getItemCount() {
        return invoicesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView email, address, vizOraAllas;
        //Button deleteButton;


        public ViewHolder(View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.emailTextView);
            address = itemView.findViewById(R.id.addressTextView);
            vizOraAllas = itemView.findViewById(R.id.vizOraTextView);
        }
    }


}


