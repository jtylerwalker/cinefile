package com.example.cinefile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener {
    private RecyclerView popularListRecycler, topRatedListRecycler, upcomingListRecycler;
    private List<Movie> popularList, topRatedList, upcomingList;
    private RecyclerView.Adapter popularAdapter, topRatedAdapter, upcomingAdapter;
    private String apiKey = BuildConfig.API_KEY;

    // nav
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView background = findViewById(R.id.backgroundImage);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w780/pCUdYAaarKqY2AAUtV6xXYO8UGY.jpg")
                .into(background);

        popularListRecycler = findViewById(R.id.popularList);
        topRatedListRecycler = findViewById(R.id.topRatedList);
        upcomingListRecycler = findViewById(R.id.upcomingList);

        popularList = new ArrayList<>();
        topRatedList = new ArrayList<>();
        upcomingList = new ArrayList<>();

        popularAdapter = new MovieAdapter(getApplicationContext(), popularList, this);
        topRatedAdapter = new MovieAdapter(getApplicationContext(), topRatedList, this);
        upcomingAdapter = new MovieAdapter(getApplicationContext(), upcomingList, this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager topRatedManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager upcomingManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        popularListRecycler.setHasFixedSize(true);
        popularListRecycler.setLayoutManager(layoutManager);
        popularListRecycler.setAdapter(popularAdapter);

        topRatedListRecycler.setHasFixedSize(true);
        topRatedListRecycler.setLayoutManager(topRatedManager);
        topRatedListRecycler.setAdapter(topRatedAdapter);

        upcomingListRecycler.setHasFixedSize(true);
        upcomingListRecycler.setLayoutManager(upcomingManager);
        upcomingListRecycler.setAdapter(upcomingAdapter);

        String popularUrl = "https://api.themoviedb.org/3/movie/popular?language=en-US&page=1&api_key=" + apiKey;
        String topRatedUrl = "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1&api_key=" + apiKey;
        String upcomingUrl = "https://api.themoviedb.org/3/movie/upcoming?language=en-US&page=1&api_key=" + apiKey;

        fetchMovies(popularUrl, popularList, popularAdapter);
        fetchMovies(topRatedUrl, topRatedList, topRatedAdapter);
        fetchMovies(upcomingUrl, upcomingList, upcomingAdapter);

        createNav();
    }

    public void createNav() {
        final Intent intent = new Intent(this, SearchActivity.class);

        dl = (DrawerLayout)findViewById(R.id.drawer_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_open, R.string.navigation_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.navView);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.home:
                        //Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();break;
                        break;
                    case R.id.search:
                        startActivity(intent);
                        break;
                    case R.id.featured:
                        //Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();break;
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
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
                                    Movie movie = new Movie();
                                    movie.setId(jsonObject.getInt("id"));
                                    movie.setTitle(jsonObject.getString("title"));
                                    movie.setVoteAverage(jsonObject.getDouble("vote_average"));
                                    movie.setYear(jsonObject.getString("release_date"));
                                    movie.setOverview(jsonObject.getString("overview"));
                                    movie.setPosterUrl(jsonObject.getString("poster_path"));

                                    list.add(movie);
                                };
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