package com.example.contactsapp.Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.contactsapp.R;
import com.example.contactsapp.Utils.SendData;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private String whichFragment;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getIntentData();
        initFragment();
    }

    private void initFragment() {

        if (whichFragment.equals(getString(R.string.intent_name_addContact))){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,new AddContactFragment());
            transaction.commit();
        }else if (whichFragment.equals(getString(R.string.intent_name_detailContact))){
            sendID();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,new DetailFragment());
            transaction.commit();
        }
    }

    private void sendID() {
        EventBus.getDefault().postSticky(new SendData(id));
    }

    private void getIntentData() {
        whichFragment = getIntent().getStringExtra("forWhat");
        id = getIntent().getIntExtra("id",0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
