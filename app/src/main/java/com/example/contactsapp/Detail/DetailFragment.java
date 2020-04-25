package com.example.contactsapp.Detail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contactsapp.Database.DatabaseHelper;
import com.example.contactsapp.Database.DatabaseMethods;
import com.example.contactsapp.Model.Contact;
import com.example.contactsapp.R;
import com.example.contactsapp.Utils.BitmapByteConverter;
import com.example.contactsapp.Utils.SendData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = "DetailContact";

    private View view;

    private Toolbar toolbar;
    private TextView tvName,tvPhone,tvEmail;
    private CircleImageView img_contact;

    private int id;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);

        setupToolbar();
        initViews();
        retrieveContactData();

        return view;
    }

    private void retrieveContactData() {

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        DatabaseMethods databaseMethods = new DatabaseMethods();
        Contact contact = databaseMethods.getContact(databaseHelper,id);

        tvName.setText(contact.getName());
        tvPhone.setText(contact.getPhone().substring(2,14));
        if (contact.getEmail().equals("")){
            tvEmail.setText(getString(R.string.no_email));
        }else {
            tvEmail.setText(contact.getEmail());
        }

        if (contact.getProfile_photo() != null){
            img_contact.setImageBitmap(BitmapByteConverter.getImage(contact.getProfile_photo()));
        }
    }

    private void setupToolbar() {
        toolbar = view.findViewById(R.id.edit_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contact Detail");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        tvName = view.findViewById(R.id.detail_name);
        tvEmail = view.findViewById(R.id.detail_email);
        tvPhone = view.findViewById(R.id.detail_phone);
        img_contact = view.findViewById(R.id.img_contact_detail);
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_action){
            replaceByEditFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceByEditFragment() {
        FragmentTransaction transaction = ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,new EditFragment());
        transaction.addToBackStack("detailFragment");
        transaction.commit();
    }

    //EventBus
    @Subscribe(sticky = true)
    public void getID(SendData data){
        id = data.getId();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
