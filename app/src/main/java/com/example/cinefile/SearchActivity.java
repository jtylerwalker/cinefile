package com.example.cinefile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements RecyclerViewClickListener {
    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private List<Movie> movieList;
    private RecyclerView.Adapter adapter;
    private String apiKey = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        populateMovieList();
    }

    protected void populateMovieList() {
        mList = findViewById(R.id.searchList);

        movieList = new ArrayList<>();
        adapter = new SearchAdapter(getApplicationContext(), movieList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d("Search", s);
                        searchMovies(s);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                }
        );
        return super.onCreateOptionsMenu(menu);
    }

    public void searchMovies(String query) {
        movieList.clear();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/search/movie?language=en-US&page=1&api_key=" + apiKey + "&query=" + query;

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
                                Log.println(Log.WARN,"Volley", jsonObject.getString("poster_path"));

                                Movie movie = new Movie();
                                movie.setTitle(jsonObject.getString("title"));
                                movie.setVoteAverage(jsonObject.getDouble("vote_average"));
                                movie.setYear(jsonObject.getString("release_date"));
                                movie.setPosterUrl(jsonObject.getString("poster_path"));

                                movieList.add(movie);
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
        Log.d("Clicked:", movieList.get(position).title);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("Movie", list.get(position));
        startActivity(intent);
    }
}