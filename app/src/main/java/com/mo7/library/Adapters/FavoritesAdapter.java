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


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {

    private Context context;
    private ArrayList<BookDes> list = new ArrayList<>();
    public OnItemClickListener listener;

    public FavoritesAdapter(Context context, ArrayList<BookDes> list, OnItemClickListener listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorites_list, parent, false);
        return new FavoritesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapterViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(BookDes book);

    }

    public static class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleText;
        TextView mWriterText;
        ImageView mImageView;
        public FavoritesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.download_title);
            mWriterText = itemView.findViewById(R.id.download_writer);
            mImageView = itemView.findViewById(R.id.download_imageView);
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
