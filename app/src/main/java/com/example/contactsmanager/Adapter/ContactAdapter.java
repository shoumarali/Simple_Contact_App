package com.example.contactsmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsmanager.MainActivity;
import com.example.contactsmanager.R;
import com.example.contactsmanager.db.entity.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Contact> contactArrayList;
    private MainActivity mainActivity;
    public ContactAdapter(Context context, ArrayList<Contact> contactArrayList, MainActivity mainActivity){
        this.context=context;
        this.contactArrayList = contactArrayList;
        this.mainActivity=mainActivity;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView email;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email= itemView.findViewById(R.id.email);
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_main_item,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Contact contact = contactArrayList.get(position);
        holder.name.setText(contact.getName());
        holder.email.setText(contact.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addAndEditContact(true,contact,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }
}
