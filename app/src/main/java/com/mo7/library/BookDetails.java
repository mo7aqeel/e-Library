package com.mo7.library;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mo7.library.Adapters.CommentsAdapter;
import com.mo7.library.Fragments.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookDetails extends AppCompatActivity {

    ImageView mImageView;
    TextView mTitleText, mWriterText, mDescText;
    EditText mCommentEditText;
    Button mReadBtn, mDownloadBtn;
    ImageButton mFavBtn;
    private BookDes bookDes;
    private ArrayList<BookDes> list;
    ArrayList<String> commentList = new ArrayList<>();
    private boolean isFav = false;
    private RecyclerView mRecyclerView;

    private boolean isPayment = false;
    private FirebaseDatabase database;

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        bookDes = HomeFragment.mBookDes;
        SplashActivity.setDefaultLanguage(this, "en");

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users")
                .child(RegisterActivity.getEmail()).child("payment");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot n : snapshot.getChildren()){
                    if (n.getKey().equals(bookDes.getTitle())){
                        ((LinearLayout)findViewById(R.id.linear_layout2)).setVisibility(View.GONE);
                        ((LinearLayout)findViewById(R.id.linear_layout)).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRecyclerView = findViewById(R.id.comments_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(BookDetails.this, RecyclerView.VERTICAL, false));
        mCommentEditText = findViewById(R.id.add_comment_edit_text);
        mImageView = findViewById(R.id.book_img_details);
        mTitleText = findViewById(R.id.title_book_details);
        mWriterText = findViewById(R.id.writer_details);
        mDescText = findViewById(R.id.description_details);
        mReadBtn = findViewById(R.id.read_btn);
        mDownloadBtn = findViewById(R.id.download_btn);
        mFavBtn = findViewById(R.id.favorite_btn);


        list = MainActivity.favList;
        for (int i=0; i<list.size(); i++){
            if (list.get(i).getTitle().equals(bookDes.getTitle())){
                mFavBtn.setImageDrawable(getDrawable(R.drawable.ic_favorite_white));
                isFav = true;

            }
        }

        mTitleText.setText(bookDes.getTitle());
        mDescText.setText(bookDes.getDesc());
        mWriterText.setText(bookDes.getWriter());
        Picasso.get().load(bookDes.getImg()).into(mImageView);

        mReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetails.this, ViewPDF.class);
                intent.putExtra("url", bookDes.getFile());
                startActivity(intent);
            }
        });

        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long file = downloadFile(BookDetails.this, bookDes.getTitle(), ".pdf", "", bookDes.getFile());
                Toast.makeText(BookDetails.this, "يتم الآن التنزيل", Toast.LENGTH_SHORT).show();

            }
        });

        mFavBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                Log.i("Number of fav : ", bookDes.getFavNum());
                long fav = Integer.parseInt(bookDes.getFavNum());
                if (isFav){
                    fav--;
                    bookDes.setFavNum(String.valueOf(fav));
                    mFavBtn.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_24));
                    FirebaseDatabase.getInstance().getReference("users").child(RegisterActivity.getEmail())
                            .child("fav books").child(bookDes.getTitle()).removeValue();
                    for (int i=0; i<list.size(); i++){
                        if (list.get(i).getTitle().equals(bookDes.getTitle())){
                            list.remove(i);
                        }//end if()
                    }//end for()
                }//end if
                else {
                    fav++;
                    bookDes.setFavNum(String.valueOf(fav));
                    FirebaseDatabase.getInstance().getReference(bookDes.getCategory()).child(bookDes.getTitle())
                            .child("fav").setValue(String.valueOf(fav));
                    FirebaseDatabase.getInstance().getReference("users").child(RegisterActivity.getEmail())
                            .child("fav books").child(bookDes.getTitle()).setValue(bookDes);
                    mFavBtn.setImageDrawable(getDrawable(R.drawable.ic_favorite_white));
                    Toast.makeText(BookDetails.this, "تم اضافة الكتاب الى قائمة المفضلات", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i("Size of comments list : ", String.valueOf(commentList.size()));
        mRecyclerView.setAdapter(new CommentsAdapter(BookDetails.this, commentList));

    }

    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        return downloadmanager.enqueue(request);
    }

    public void sendComment(View view) {
        String comm = mCommentEditText.getText().toString();
        if (!comm.isEmpty()){
            FirebaseDatabase.getInstance().getReference(bookDes.getCategory()).child(bookDes.getTitle())
                    .child("comments").child(RegisterActivity.getEmail())
                    .setValue(comm);
            mCommentEditText.setText("");
            Toast.makeText(BookDetails.this, "تم إضافة تعليقك", Toast.LENGTH_SHORT).show();
        }
        else {
            mCommentEditText.setError("Can't upload an empty comment");
            mCommentEditText.requestFocus();
        }
    }

    private void showComments(){
        StringBuilder builder = new StringBuilder();
        FirebaseDatabase.getInstance().getReference(bookDes.getCategory()).child(bookDes.getTitle())
                .child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot n : snapshot.getChildren()){
                    builder.append(n.getKey()).append("\n").append(n.getValue(String.class));
                    commentList.add(builder.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startPaymentActivity(View view) {
        startActivity(new Intent(BookDetails.this, PaymentActivity.class));
    }
}