package com.example.cinefile;

import java.io.Serializable;

public class Movie implements Serializable {
    public int id;
    public String title;
    public double voteAverage;
    public String year;
    public String posterUrl;
    public String overview;

    public Movie() {

    }

    public Movie(int id, String title, double voteAverage, String year, String posterUrl, String overview) {
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
        this.year = year;
        this.posterUrl = posterUrl;
        this.overview = overview;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
