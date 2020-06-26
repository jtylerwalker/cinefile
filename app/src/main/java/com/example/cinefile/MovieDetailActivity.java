package com.example.cinefile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements RecyclerViewClickListener {
    private RecyclerView popularListRecycler, topRatedListRecycler, upcomingListRecycler;
    private List<Movie> popularList, topRatedList, upcomingList;
    private RecyclerView.Adapter popularAdapter, topRatedAdapter, upcomingAdapter;
    private String apiKey = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        TextView title = findViewById(R.id.title);
        TextView voteAverage = findViewById(R.id.voteAverage);
        TextView year = findViewById(R.id.year);

        voteAverage.setText(String.valueOf(movie.voteAverage));
        voteAverage.setText(movie.year);
        title.setText(movie.title);

        ImageView poster = findViewById(R.id.posterUrl);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342" + movie.getPosterUrl())
                .into(poster);

        TextView overview = findViewById(R.id.overview);
        overview.setText(movie.overview);

        popularListRecycler = findViewById(R.id.moreRecycler);

        popularList = new ArrayList<>();

        popularAdapter = new MovieAdapter(getApplicationContext(), popularList, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        popularListRecycler.setHasFixedSize(true);
        popularListRecycler.setLayoutManager(layoutManager);
        popularListRecycler.setAdapter(popularAdapter);

        String popularUrl = "https://api.themoviedb.org/3/movie/" + movie.id + "/similar?language=en-US&page=1&api_key=" + apiKey;

        fetchMovies(popularUrl, popularList, popularAdapter);
    }

    public void fetchMovies(String url, final List<Movie> list, final RecyclerView.Adapter adapter) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, url, null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject jsonObject = results.getJSONObject(i);

                                if (jsonObject.getString("poster_path") != "null") {
                                    Log.d("Null image", jsonObject.getString("title"));
                                    Movie movie = new Movie();
                                    movie.setId(jsonObject.getInt("id"));
                                    movie.setTitle(jsonObject.getString("title"));
                                    movie.setVoteAverage(jsonObject.getDouble("vote_average"));
                                    movie.setYear(jsonObject.getString("release_date"));
                                    movie.setOverview(jsonObject.getString("overview"));
                                    movie.setPosterUrl(jsonObject.getString("poster_path"));

                                    list.add(movie);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        );
        // Adds the JSON object request "obreq" to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(obreq);
    }

    @Override
    public void onClick(View view, int position, List<Movie> list) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("Movie", list.get(position));
        startActivity(intent);
    }
}