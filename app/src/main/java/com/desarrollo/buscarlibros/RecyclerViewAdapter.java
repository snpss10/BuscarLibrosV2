package com.desarrollo.buscarlibros;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Book> mData;

    public RecyclerViewAdapter(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.book_raw_item,parent,false);


        /* return new LibrosViewHolder(view);*/
        MyViewHolder pvh = new MyViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Book actual = mData.get(i);
        String urlImg = actual.getThumbnail();
       /* String title = actual.getTitle();
        String author = actual.getAuthors();
        String price = actual.getPrice();
        String category = actual.getCategories();*/

        holder.tvAuthor.setText(mData.get(i).getAuthors());
        holder.tvTitle.setText(mData.get(i).getTitle());
        holder.tvPrice.setText(mData.get(i).getPrice());
        holder.tvCategory.setText(mData.get(i).getCategories());
        Picasso.with(mContext).load(urlImg).fit().centerInside().into(holder.ivThumbnail);


       /* Book book = mData.get(i);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthors());
        holder.tvPrice.setText(book.getPrice());
        holder.tvCategory.setText(book.getCategories());


        //load image from internet and set it into imageView using Glide
        Picasso.with(mContext).load(urlImg).fit().centerInside().into(holder.ivThumbnail);*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail ;
        TextView tvTitle , tvCategory , tvPrice , tvAuthor;
        LinearLayout container ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.thumbnail);
            tvTitle = itemView.findViewById(R.id.title);
            tvAuthor = itemView.findViewById(R.id.author);
            tvCategory = itemView.findViewById(R.id.category);
            tvPrice = itemView.findViewById(R.id.price);
            container = itemView.findViewById(R.id.container);
        }
    }
}
