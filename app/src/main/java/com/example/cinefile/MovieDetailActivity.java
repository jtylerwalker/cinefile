package com.example.cinefile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        TextView title = findViewById(R.id.title);
        title.setText(movie.title);

        ImageView poster = findViewById(R.id.posterUrl);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342" + movie.getPosterUrl())
                .into(poster);

        TextView overview = findViewById(R.id.overview);
        overview.setText(movie.overview);
    }
}