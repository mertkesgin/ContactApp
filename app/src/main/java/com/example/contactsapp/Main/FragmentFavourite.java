package com.example.contactsapp.Main;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.contactsapp.Adapters.ContactsAdapter;
import com.example.contactsapp.Adapters.FavouritesAdapter;
import com.example.contactsapp.Database.DatabaseHelper;
import com.example.contactsapp.Database.DatabaseMethods;
import com.example.contactsapp.Detail.DetailActivity;
import com.example.contactsapp.Model.Contact;
import com.example.contactsapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentFavourite extends Fragment implements SearchView.OnQueryTextListener {

    private View view;

    private RecyclerView rvFavourite;
    private ArrayList<Contact> favouriteList;
    private FavouritesAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    public FragmentFavourite() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fav_fragment,container,false);

        setupRecyclerView();
        initSwipeRefresh();
        retrieveDataFromDatabase();

        return view;
    }

    private void initSwipeRefresh() {
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveDataFromDatabase();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupRecyclerView() {
        rvFavourite = view.findViewById(R.id.rv_favourite);
        rvFavourite.setHasFixedSize(true);
        rvFavourite.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void retrieveDataFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        favouriteList = new DatabaseMethods().getAllFavourites(databaseHelper);

        Collections.sort(favouriteList);
        adapter = new FavouritesAdapter(getContext(),favouriteList);
        rvFavourite.setAdapter(adapter);
    }

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
        favouriteList = new DatabaseMethods().searchFavourite(databaseHelper,newText);
        Collections.sort(favouriteList);
        adapter = new FavouritesAdapter(getContext(),favouriteList);
        rvFavourite.setAdapter(adapter);
        return false;
    }
}
