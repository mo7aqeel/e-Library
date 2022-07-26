package com.mo7.library.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mo7.library.Adapters.FavoritesAdapter;
import com.mo7.library.BookDes;
import com.mo7.library.BookDetails;
import com.mo7.library.MainActivity;
import com.mo7.library.R;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    public FavoriteFragment(){
        super(R.layout.favorites_fragment);
    }

    ArrayList<BookDes> list;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = MainActivity.favList;
        RecyclerView recyclerView = view.findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new FavoritesAdapter(getContext(), list, new FavoritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookDes book) {
                HomeFragment.mBookDes = book;
                Intent intent = new Intent(getContext(), BookDetails.class);
                startActivity(intent);
            }
        }));
    }
}
