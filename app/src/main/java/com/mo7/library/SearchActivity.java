package com.mo7.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.mo7.library.Adapters.ListViewAdapter;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<String> searchList;
    private ListView searchListView;
    private SearchView editsearch;
    private ListViewAdapter adapter;

    @Override
    protected void onStop() {
        super.onStop();
        adapter.mainList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchListView = findViewById(R.id.searchlistview);
        editsearch = findViewById(R.id.searchView);

        searchList = SplashActivity.searchList;
        adapter = new ListViewAdapter(this, searchList);
        searchListView.setAdapter(adapter);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);
                return false;
            }
        });
    }
}