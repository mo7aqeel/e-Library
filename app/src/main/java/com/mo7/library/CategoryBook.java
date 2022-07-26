package com.mo7.library;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mo7.library.Adapters.CategoryBookAdapter;
import com.mo7.library.Fragments.HomeFragment;

import java.util.ArrayList;

public class CategoryBook extends AppCompatActivity {

    private ArrayList<BookDes> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_book);

        Intent intent = getIntent();
        String key = intent.getStringExtra("category");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference(key);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    DataSnapshot n = child.child("title");
                    DataSnapshot n2 = child.child("desc");
                    DataSnapshot n3 = child.child("image");
                    DataSnapshot n4 = child.child("writer");
                    DataSnapshot n5 = child.child("file");
                    DataSnapshot n6 = child.child("price");
                    DataSnapshot n7 = child.child("fav");

                    list.add(new BookDes(n.getValue(String.class), n2.getValue(String.class),
                            n3.getValue(String.class), n4.getValue(String.class), n5.getValue(String.class),
                            snapshot.getKey(), n6.getValue(String.class), n7.getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        RecyclerView recyclerView = findViewById(R.id.category_book_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        CategoryBookAdapter adapter = new CategoryBookAdapter(this, list, new CategoryBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookDes book) {
                HomeFragment.mBookDes = book;
                Intent my_intent = new Intent(CategoryBook.this, BookDetails.class);
                startActivity(my_intent);
            }
        });

        recyclerView.setAdapter(adapter);

    }
}