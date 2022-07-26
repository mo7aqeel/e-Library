package com.mo7.library.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mo7.library.Adapters.CategoriesAdapter;
import com.mo7.library.CategoriesInfo;
import com.mo7.library.CategoryBook;
import com.mo7.library.R;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    public CategoriesFragment(){
        super(R.layout.categories_fragment);
    }

    private RecyclerView mRecyclerView;
    private ArrayList<CategoriesInfo> list = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.categories_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        list.add(new CategoriesInfo("روايات", R.drawable.novels));
        list.add(new CategoriesInfo("علوم", R.drawable.science));
        list.add(new CategoriesInfo("فلسفة", R.drawable.philosophy));
        list.add(new CategoriesInfo("سياسة", R.drawable.politics));
        list.add(new CategoriesInfo("دينية", R.drawable.islamic));
        list.add(new CategoriesInfo("شعر", R.drawable.poetry));
        list.add(new CategoriesInfo("قانون", R.drawable.law));
        list.add(new CategoriesInfo("علم النفس", R.drawable.psychology));

        CategoriesAdapter adapter = new CategoriesAdapter(getContext(), list, new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoriesInfo book) {
                Intent intent = new Intent(getContext(), CategoryBook.class);
                intent.putExtra("category", book.getTitle());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}
