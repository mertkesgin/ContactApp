package com.example.contactsapp.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.contactsapp.Adapters.ViewPagerAdapter;
import com.example.contactsapp.R;
import com.example.contactsapp.Utils.Permissions;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupViewPager();
    }

    public void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                Permissions.PReqCode
        );
    }

    public boolean checkPermissionsArray(String[] permissions) {
        for (int i=0; i<permissions.length;i++){
            String permission = permissions[i];
            if (!checkPermissions(permission)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(MainActivity.this,permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }else {
            return true;
        }
    }

    private void setupViewPager() {

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentCall(),"");
        adapter.addFragment(new FragmentContact(),"");
        adapter.addFragment(new FragmentFavourite(),"");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star);

        tabLayout.setTabIconTintResource(R.color.white);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts");
    }
}
