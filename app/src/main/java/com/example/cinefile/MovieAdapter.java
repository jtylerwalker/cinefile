package com.example.cinefile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<Movie> list;
    private RecyclerViewClickListener mOnClickListener;

    public MovieAdapter(Context context, List<Movie> list, RecyclerViewClickListener onListener) {
        this.context = context;
        this.list = list;
        this.mOnClickListener = onListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new ViewHolder(v, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = list.get(position);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342" + movie.getPosterUrl())
                .into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mOnClickListener;
        public ImageView posterImage;

        public ViewHolder(View itemView, RecyclerViewClickListener onListener) {
            super(itemView);

            posterImage = itemView.findViewById(R.id.poster_image);

            this.mOnClickListener = onListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onClick(view, getAdapterPosition(), list);
        }

    }

}
