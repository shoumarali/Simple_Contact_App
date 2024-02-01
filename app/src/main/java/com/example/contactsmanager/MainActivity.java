package com.example.contactsmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsmanager.Adapter.ContactAdapter;
import com.example.contactsmanager.db.DataBaseHelper;
import com.example.contactsmanager.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> contactArrayList;
    private ContactAdapter contactAdapter;
    private RecyclerView recyclerView;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts");

        // recycler view
        recyclerView = findViewById(R.id.recycler_view_contacts);
        db = new DataBaseHelper(getApplicationContext());

        //Contacts
        contactArrayList = new ArrayList<>();
        contactArrayList.addAll(db.getAllContacts());
        contactAdapter = new ContactAdapter(getApplicationContext(), contactArrayList , MainActivity.this);

        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndEditContact(false,null, -1);
            }
        });
    }
    public void addAndEditContact(final boolean isUpdated, final Contact contact,final int position){
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setView(view);

        TextView contactTitle = view.findViewById(R.id.contact_title);
        EditText contactName = view.findViewById(R.id.edit_text_name);
        EditText contactEmail = view.findViewById(R.id.edit_text_email);

        contactTitle.setText(isUpdated ? "Edit Contact" : "New Contact" );

        if(isUpdated && contact != null){
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }
        alertDialog.setCancelable(false)
                .setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isUpdated){
                            deleteContact(contact,position);
                        }else{
                            dialog.cancel();
                        }
                    }
                });

        final AlertDialog createAlertDialog = alertDialog.create();
        createAlertDialog.show();
        createAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(contactName.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter the name", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    createAlertDialog.dismiss();
                }
                if(isUpdated && contact != null){
                    updateContact(contactName.getText().toString(),contactEmail.getText().toString(), position);
                }else {
                    createContact(contactName.getText().toString(),contactEmail.getText().toString());
                }
            }
        });
    }
    private void deleteContact(Contact contact,int position){
        contactArrayList.remove(position);
        db.deleteContact(contact);
        contactAdapter.notifyDataSetChanged();
    }

    private void updateContact(String name,String email, int positon){
        Contact contact = contactArrayList.get(positon);
        contact.setEmail(email);
        contact.setName(name);
        contactArrayList.set(positon, contact);
        contactAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void createContact(String name, String email){
        long id = db.insertContact(name,email);
        Contact contact = db.getContact(id);
        if(contact != null){
            contactArrayList.add(0,contact);
            contactAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(R.id.action_settings == id){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}