package com.mo7.library.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mo7.library.R;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsAdapterViewHolder> {

    Context context;
    ArrayList<String> list;

    public CommentsAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_list, parent, false);
        return new CommentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapterViewHolder holder, int position) {
        holder.mUserName.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CommentsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName;
        public CommentsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.username_comment);
        }
    }
}
