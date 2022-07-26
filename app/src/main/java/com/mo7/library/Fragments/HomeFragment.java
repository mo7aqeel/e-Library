package com.mo7.library.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mo7.library.Adapters.BookAdapter;
import com.mo7.library.BookDes;
import com.mo7.library.BookDetails;
import com.mo7.library.R;
import com.mo7.library.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<BookDes> list = new ArrayList<>();

    private TextView mTitleTextView, mDesTextView;
    private ImageView mImageView;
    public static BookDes mBookDes;

    public HomeFragment(ArrayList<BookDes> list) {
        super(R.layout.home_fragment);
        this.list = list;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        mTitleTextView = view.findViewById(R.id.title_book);
        mDesTextView = view.findViewById(R.id.desc_book);
        mImageView = view.findViewById(R.id.main_book_img);


        RelativeLayout searchEditText = view.findViewById(R.id.linear_layouts);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });

        RecyclerView myRecyclerView = view.findViewById(R.id.recycler_view);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));


        BookAdapter myAdapter = new BookAdapter(getContext(), list, new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookDes book) {
                mBookDes = book;
                Button button = view.findViewById(R.id.details_btn);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), BookDetails.class);;
                        startActivity(intent);
                    }
                });

                mTitleTextView.setText(book.getTitle());
                Picasso.get().load(book.getImg()).into(mImageView);
                String des = book.getDesc();
                String s = "";
                if (des.length() > 250) {
                    for (int i = 0; i < 250; i++) {
                        s += des.charAt(i);
                    }
                    mDesTextView.setText(s + "...");
                } else
                    mDesTextView.setText(des);
            }
        });
        myRecyclerView.setAdapter(myAdapter);
    }
}
