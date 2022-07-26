package com.mo7.library;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText mFullName, mEmail, mPassword;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mFullName = findViewById(R.id.name_edit_text_register);
        mEmail = findViewById(R.id.email_login);
        mPassword = findViewById(R.id.password_login);
        mProgressBar = findViewById(R.id.progressBar);

    }

    private void checkInfo(String fullName, String email, String password){

        if (fullName.isEmpty()){
            mFullName.setError("This field is required");
            mFullName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            mEmail.setError("This field is required");
            mEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            mPassword.setError("This field is required");
            mPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Pleas provide valid email");
            mEmail.requestFocus();
            return;
        }

    }//end checkInfo()

    private void verifyEmail(){
        String email = mEmail.getText().toString();
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://com.mo7.library/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();


    }

    public void register(View view) {
        String fullName = mFullName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        checkInfo(fullName, email, password);
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(getEmail()).child("name")
                            .setValue(fullName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }//end if
                            else {
                                Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, getEmail(), Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }//end onComplete()
                    });
                }//end if()
            }//end onComplete()
        });
    }//end register(View view)

    public static String getEmail(){
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        int i = email.indexOf('@');
        return email.substring(0, i);
    }
}