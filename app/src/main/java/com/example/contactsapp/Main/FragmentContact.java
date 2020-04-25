package com.example.contactsapp.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Adapters.ContactsAdapter;
import com.example.contactsapp.Database.DatabaseHelper;
import com.example.contactsapp.Database.DatabaseMethods;
import com.example.contactsapp.Detail.DetailActivity;
import com.example.contactsapp.Model.Contact;
import com.example.contactsapp.R;

import java.util.Collections;
import java.util.List;

public class FragmentContact extends Fragment implements SearchView.OnQueryTextListener{

    private static final String TAG = "FragmentContact";
    private View view;

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter adapter;

    public FragmentContact() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_fragment,container,false);

        setupRecyclerView();
        retrieveDataFromDatabase();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView = view.findViewById(R.id.contact_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void retrieveDataFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        contactList = new DatabaseMethods().getAllContacts(databaseHelper);

        Collections.sort(contactList);
        adapter = new ContactsAdapter(getContext(),contactList);
        recyclerView.setAdapter(adapter);
    }

    //Options Menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
            case R.id.action_people_add:
                openFragment();
            default:
                return false;
        }
    }

    private void openFragment() {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("forWhat",getString(R.string.intent_name_addContact));
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        contactList = new DatabaseMethods().searchContact(databaseHelper,newText);
        Collections.sort(contactList);
        adapter = new ContactsAdapter(getContext(),contactList);
        recyclerView.setAdapter(adapter);
        return false;
    }
}
