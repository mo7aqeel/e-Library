package com.mo7.library.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mo7.library.AddBookActivity;
import com.mo7.library.ForumActivity;
import com.mo7.library.LoginActivity;
import com.mo7.library.R;
import com.mo7.library.RegisterActivity;

public class ChoicesFragment extends Fragment {
    public ChoicesFragment(){
        super(R.layout.choices_fragment);
    }

    private TextView mUserNameText, mForumText, mLogOutText;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private Button mAddBookBtn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("my pref", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        mUserNameText = view.findViewById(R.id.user_name);
        mForumText = view.findViewById(R.id.forum_text);
        mLogOutText = view.findViewById(R.id.logout_text);
        mAddBookBtn = view.findViewById(R.id.add_book_btn);
        mForumText = view.findViewById(R.id.forum_text);

        mForumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ForumActivity.class));
            }
        });

        mAddBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddBookActivity.class));
            }
        });

        if (mAuth.getCurrentUser().getEmail().equals("mo7@gmail.com"))
            mAddBookBtn.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference("users").child(RegisterActivity.getEmail())
                .child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUserNameText.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to get name of user", Toast.LENGTH_SHORT).show();
                    }
                });

        mLogOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                sharedPreferences.edit().clear().apply();
            }
        });
    }
}
