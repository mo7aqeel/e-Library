package com.mo7.library.Adapters;

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


public class CategoryBookAdapter extends RecyclerView.Adapter<CategoryBookAdapter.CategoryBookAdapterViewHolder> {

    private final ArrayList<BookDes> list;
    private final Context context;
    private final OnItemClickListener listener;

    public CategoryBookAdapter( Context context, ArrayList<BookDes> list, OnItemClickListener listener){
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryBookAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_book_list, parent, false);
        return new CategoryBookAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryBookAdapterViewHolder holder, int position) {

        holder.bind(list.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(BookDes book);

    }

    public static class CategoryBookAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitleText;
        private final TextView mWriterText;
        private final ImageView mImageView;

        public CategoryBookAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.title_category_book);
            mWriterText = itemView.findViewById(R.id.writer_category_book);
            mImageView = itemView.findViewById(R.id.imageView_category_book);
        }

        public void bind(final BookDes item, OnItemClickListener listener) {
            mTitleText.setText(item.getTitle());
            mWriterText.setText(item.getWriter());
            Picasso.get().load(item.getImg()).into(mImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }

            });
        }
    }
}
