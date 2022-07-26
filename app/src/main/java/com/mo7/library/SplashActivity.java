package com.mo7.library;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    public static ArrayList<DatabaseReference> list = new ArrayList<>();
    public static ArrayList<BookDes> arrayList = new ArrayList<>();
    public static ArrayList<String> searchList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        list.add(database.getReference("روايات"));
        list.add(database.getReference("علوم"));
        list.add(database.getReference("فلسفة"));
        list.add(database.getReference("سياسة"));
        list.add(database.getReference("دينية"));
        list.add(database.getReference("شعر"));
        list.add(database.getReference("قانون"));
        list.add(database.getReference("علم النفس"));

        for (int i=0; i<list.size(); i++){
            valueEventListener(list.get(i));
        }
        setDefaultLanguage(this, "en");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("my pref", MODE_PRIVATE);

        ImageView book1 =  findViewById(R.id.book4);
        ImageView book2 =  findViewById(R.id.book1);
        ImageView book3 =  findViewById(R.id.book2);
        ImageView book4 =  findViewById(R.id.book3);
        TextView title = findViewById(R.id.library_splash);
        TextView welcome = findViewById(R.id.welcome_splash);
        progressBar = findViewById(R.id.progressBar3);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.book_anim);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.book2);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.book3);
        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.book4);
        Animation text_anim = AnimationUtils.loadAnimation(this, R.anim.texts_anim);
        book1.startAnimation(animation);
        book2.startAnimation(animation2);
        book3.startAnimation(animation3);
        book4.startAnimation(animation4);
        title.startAnimation(text_anim);
        welcome.startAnimation(text_anim);

        new CountDownTimer(5000, 1000){

            @Override
            public void onTick(long l) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFinish() {
                if (!isNetworkConnected()) {
                    showAlertMessage();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    String email = sharedPreferences.getString("email", "");
                    String pass = sharedPreferences.getString("password", "");
                    if (!email.isEmpty() && !pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        }.start();
    }

    public static void setDefaultLanguage(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public void valueEventListener(DatabaseReference myRef){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    DataSnapshot n = child.child("title");
                    DataSnapshot n2 = child.child("desc");
                    DataSnapshot n3 = child.child("image");
                    DataSnapshot n4 = child.child("writer");
                    DataSnapshot n5 = child.child("file");
                    DataSnapshot n6 = child.child("price");
                    DataSnapshot n7 = child.child("fav");

                    arrayList.add(new BookDes(n.getValue(String.class), n2.getValue(String.class),
                            n3.getValue(String.class), n4.getValue(String.class), n5.getValue(String.class),
                            dataSnapshot.getKey(), n6.getValue(String.class), n7.getValue(String.class)));
                    searchList.add(n.getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetwork() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void showAlertMessage(){
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Field")
                .setMessage(getString(R.string.no_internet))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                }).show();
    }
}