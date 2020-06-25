package com.example.cinefile;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<Movie> list;
    private RecyclerViewClickListener mOnClickListener;

    public SearchAdapter(Context context, List<Movie> list, RecyclerViewClickListener mOnClickListener) {
        this.context = context;
        this.list = list;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(v, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie item = list.get(position);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342" + item.getPosterUrl())
                .into(holder.searchImage);
        holder.textTitle.setText(item.title);
    }

    @Override
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textTitle;
        private ImageView searchImage;
        private RecyclerViewClickListener mOnClickListener;

        public ViewHolder(View itemView, RecyclerViewClickListener mOnClickListener) {
            super(itemView);

            searchImage = itemView.findViewById(R.id.searchImage);
            textTitle = itemView.findViewById(R.id.main_title);

            this.mOnClickListener = mOnClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onClick(view, getAdapterPosition(), list);
        }

    }
}
