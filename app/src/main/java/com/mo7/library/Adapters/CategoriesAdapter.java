package com.mo7.library.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mo7.library.CategoriesInfo;
import com.mo7.library.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder> {

    private Context context;
    private ArrayList<CategoriesInfo> list = new ArrayList<>();
    public OnItemClickListener listener;
    public CategoriesAdapter(Context context, ArrayList<CategoriesInfo> list, OnItemClickListener listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_table, parent, false);
        return new CategoriesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapterViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(CategoriesInfo book);

    }

    public static class CategoriesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView mImage;
        public CategoriesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.category_title);
            mImage = itemView.findViewById(R.id.category_image);
        }

        public void bind(final CategoriesInfo item, OnItemClickListener listener) {
            mTitle.setText(item.getTitle());
            mImage.setImageResource(item.getImage());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }

            });
        }

    }
}
