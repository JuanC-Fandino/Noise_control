package com.example.juank.noise_control;


/**
 * Created by Abhi on 22 Apr 2017 022.
 */

public class MovieSpinnerVO {

    int movieId = 0;
    String movieName = "";

    public MovieSpinnerVO(int movieId, String movieName) {
        this.movieId = movieId;
        this.movieName = movieName;
    }
    public String toString()
    {
        return movieName;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }
}
