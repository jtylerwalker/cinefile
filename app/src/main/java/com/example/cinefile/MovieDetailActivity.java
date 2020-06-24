package com.example.cinefile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        TextView textView = findViewById(R.id.textView);
        textView.setText(movie.title);
    }
}