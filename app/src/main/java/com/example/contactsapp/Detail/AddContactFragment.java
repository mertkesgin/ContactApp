package com.example.contactsapp.Detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactsapp.Database.DatabaseHelper;
import com.example.contactsapp.Database.DatabaseMethods;
import com.example.contactsapp.Main.MainActivity;
import com.example.contactsapp.Model.Contact;
import com.example.contactsapp.R;


public class AddContactFragment extends Fragment {

    private View view;

    private Toolbar toolbar;

    private Button btnAdd;
    private EditText contactName,contactEmail,contactPhone;

    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_contact, container, false);

        setupToolbar();
        initViews();
        addContact();

        return view;
    }

    private void addContact() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()){
                    saveUser();
                }else {
                    Toast.makeText(getContext(), "Name and Phone can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUser() {
        String name = contactName.getText().toString();
        String phone = contactPhone.getText().toString();
        String email = contactEmail.getText().toString();

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        DatabaseMethods databaseMethods = new DatabaseMethods();

        Contact contact = new Contact(name,"0 "+phone,email,"not favourite");
        
        if (databaseMethods.addContact(databaseHelper,contact)){
            Toast.makeText(getContext(), "Contact Saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private boolean checkFields() {
        if (!contactPhone.getText().toString().equals("") && !contactName.getText().toString().equals("")){
            return true;
        }else return false;
    }

    private void initViews() {
        contactEmail = view.findViewById(R.id.edit_contact_email);
        contactName = view.findViewById(R.id.edit_contact_name);
        contactPhone = view.findViewById(R.id.edit_contact_phone);
        contactPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        btnAdd = view.findViewById(R.id.btn_add);
    }

    private void setupToolbar() {
        toolbar = view.findViewById(R.id.edit_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Contact");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
