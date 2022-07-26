package com.mo7.library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddBookActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private EditText mTitleEditText, mWriterEditText, mDescEditText, mPriceEditText;
    private TextView mImageUriTextView, mFileUriTextView;

    private ProgressBar progressBar;
    private DatabaseReference reference;
    private String dep = "";
    private Uri filePath;
    private Uri imagePath;
    private Uri fileUri;
    private Uri imageUri;

    FirebaseStorage storage;
    DatabaseReference database;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        progressBar = findViewById(R.id.progressBar4);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mSpinner = findViewById(R.id.spinner);
        mTitleEditText = findViewById(R.id.title_book_edit_text);
        mWriterEditText = findViewById(R.id.writer_edit_text);
        mDescEditText = findViewById(R.id.desc_edit_text);
        mPriceEditText = findViewById(R.id.price_book_edit_text);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_data, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dep = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void addImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK){
            if (data != null && data.getData() != null){
                imagePath = data.getData();
                mImageUriTextView.setText(imagePath.toString());
            }
        }
        else if (requestCode == 1 && resultCode == RESULT_OK){
            if (data != null) {
                filePath=data.getData();
                mFileUriTextView.setText(filePath.toString());
            } else
                Toast.makeText(this, "NO FILE CHOSEN", Toast.LENGTH_SHORT).show();
        }
    }


    public void addFile(View view) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), 1);
    }

    private void uploadImage() {
        if (imagePath != null) {
            StorageReference sref = storageReference.child(mTitleEditText.getText().toString() + ".jpg");

            sref.putFile(imagePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            imageUri = uriTask.getResult();
                            UploadFile();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddBookActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void UploadFile() {
        if (filePath != null) {
            StorageReference sref = storageReference.child(mTitleEditText.getText().toString() + ".pdf");

            sref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            fileUri = uriTask.getResult();

                            BookDes bookDes = new BookDes(mTitleEditText.getText().toString(), mDescEditText.getText().toString(),
                                    imageUri.toString(), mWriterEditText.getText().toString(), fileUri.toString(),
                                    dep, mPriceEditText.getText().toString(), "0");
                            database = FirebaseDatabase.getInstance().getReference();
                            database.child(dep).child(mTitleEditText.getText().toString()).setValue(bookDes)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(AddBookActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddBookActivity.this, "Uploaded Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddBookActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    public void uploadBook(View view) {
        if (mTitleEditText.getText().toString().isEmpty()){
            mTitleEditText.setError("هذا الحقل مطلوب!");
            mTitleEditText.requestFocus();
            return;
        }
        if (mWriterEditText.getText().toString().isEmpty()){
            mWriterEditText.setError("هذا الحقل مطلوب!");
            mWriterEditText.requestFocus();
        }
        if (mDescEditText.getText().toString().isEmpty()){
            mDescEditText.setError("هذا الحقل مطلوب!");
            mDescEditText.requestFocus();
        }
        if (imagePath.toString().isEmpty()){
            Toast.makeText(AddBookActivity.this, "يجب اختيار صورة للكتاب!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (filePath.toString().isEmpty()){
            Toast.makeText(AddBookActivity.this, "يجب اختيار ملف الكتاب!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        uploadImage();
    }
}