package com.mo7.library.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mo7.library.BookDes;
import com.mo7.library.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookAdapterViewHolder>{

    private final Context mContext;
    private final ArrayList<BookDes> list;
    public OnItemClickListener listener;

    public BookAdapter(Context mContext, ArrayList<BookDes> arrayList, OnItemClickListener listener){
        this.list = arrayList;
        this.mContext = mContext;
        this.listener = listener;
    }//end BookAdapter()

    @NonNull
    @Override
    public BookAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.books_list, parent, false);
        return new BookAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.bind(list.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(BookDes book);

    }


    public static class BookAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mFavoritesTextView;
        TextView mBookNameTextView;
        public BookAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.book_image_view);
            mFavoritesTextView = itemView.findViewById(R.id.favorites_number_text_view);
            mBookNameTextView = itemView.findViewById(R.id.book_name_text_view);

        }

        public void bind(final BookDes item, OnItemClickListener listener) {
            Picasso.get().load(item.getImg()).into(mImageView);
            mBookNameTextView.setText(item.getTitle());
            mFavoritesTextView.setText(item.getFavNum());
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }

            });
        }
    }
}

