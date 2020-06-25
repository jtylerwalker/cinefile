package com.example.cinefile;

import android.view.View;

import java.util.List;

public interface RecyclerViewClickListener {
    void onClick(View view, int position, List<Movie> list);
}
