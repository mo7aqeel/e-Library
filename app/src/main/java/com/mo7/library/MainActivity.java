package com.mo7.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mo7.library.Fragments.CategoriesFragment;
import com.mo7.library.Fragments.ChoicesFragment;
import com.mo7.library.Fragments.FavoriteFragment;
import com.mo7.library.Fragments.HomeFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView mCategoriesImage, mHomeImage, mFavoriteImage, mChoicesImage;
    private ArrayList<BookDes> arrayList;
    public static ArrayList<BookDes> favList=  new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = SplashActivity.arrayList;

        mCategoriesImage = (ImageView) findViewById(R.id.category_img);
        mHomeImage = findViewById(R.id.home_img);
        mFavoriteImage = findViewById(R.id.favorite_img);
        mChoicesImage = findViewById(R.id.choices_img);

        startHomeFragment(savedInstanceState);

        mCategoriesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoriesImage.setImageResource(R.drawable.ic_category_clicked);
                mHomeImage.setImageResource(R.drawable.ic_home);
                mChoicesImage.setImageResource(R.drawable.ic_choices);
                mFavoriteImage.setImageResource(R.drawable.ic_favorite_border_24);
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left)
                            .replace(R.id.fragment_container_view, CategoriesFragment.class, null)
                            .commit();
                }
            }
        });

        mFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoriesImage.setImageResource(R.drawable.ic_category);
                mHomeImage.setImageResource(R.drawable.ic_home);
                mChoicesImage.setImageResource(R.drawable.ic_choices);
                mFavoriteImage.setImageResource(R.drawable.ic_favorite_24);
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left)
                            .replace(R.id.fragment_container_view, FavoriteFragment.class, null)
                            .commit();
                }
            }
        });

        mChoicesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoriesImage.setImageResource(R.drawable.ic_category);
                mHomeImage.setImageResource(R.drawable.ic_home);
                mChoicesImage.setImageResource(R.drawable.ic_choices_clicked);
                mFavoriteImage.setImageResource(R.drawable.ic_favorite_border_24);
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left)
                            .replace(R.id.fragment_container_view, ChoicesFragment.class, null)
                            .commit();
                }
            }
        });

        mHomeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHomeFragment(savedInstanceState);
            }
        });

        getFavBooks();
    }

    private void startHomeFragment(Bundle savedInstanceState){
        mCategoriesImage.setImageResource(R.drawable.ic_category);
        mHomeImage.setImageResource(R.drawable.ic_home_clicked);
        mChoicesImage.setImageResource(R.drawable.ic_choices);
        mFavoriteImage.setImageResource(R.drawable.ic_favorite_border_24);
        HomeFragment homeFragment = new HomeFragment(arrayList);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left)
                    .replace(R.id.fragment_container_view, homeFragment, null)
                    .commit();
        }
    }

    private void getFavBooks(){
        FirebaseDatabase.getInstance().getReference("users").child(RegisterActivity.getEmail())
                .child("fav books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favList.clear();
                for (DataSnapshot n : snapshot.getChildren()){
                    BookDes bookInfo = new BookDes();
                    for (DataSnapshot ch : n.getChildren()){
                        switch (ch.getKey()){
                            case "title":
                                bookInfo.setTitle(ch.getValue(String.class));
                                break;
                            case "writer":
                                bookInfo.setWriter(ch.getValue(String.class));
                                break;
                            case "img":
                                bookInfo.setImg(ch.getValue(String.class));
                                break;
                            case "des":
                                bookInfo.setDesc(ch.getValue(String.class));
                                break;
                            case "file":
                                bookInfo.setFile(ch.getValue(String.class));
                                break;
                        }//end switch
                    }//end for
                    favList.add(bookInfo);
                }//end for
            }//end onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}