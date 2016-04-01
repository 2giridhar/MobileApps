package asu.edu.msse.gpeddabu.moviedescriptions;
/**
 * Created by Giridhar on 2/10/2016.
 * Copyright 2016 Giridhar Reddy Peddabuttaiahgari,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * However this can be used for course evaluation by the instructor,
 * TAs, graders and University.
 * @author Giridhar Reddy Peddabuttaiahgari gpeddabu@asu.edu
 * @version 2/10/2016
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Giridhar on 2/10/2016.
 */
public class MovieLibrary{
    private static LinkedHashMap<String,LinkedHashMap<String,JSONObject>> movieMap;

    MovieLibrary(){

    }
    MovieLibrary(String jsonString){
        if (movieMap == null){
            movieMap = new LinkedHashMap<>();
            JSONObject jsonObj;
            LinkedHashMap<String,JSONObject> movieDescr;
            try {
                jsonObj = new JSONObject(jsonString);
                Iterator<String> iter = jsonObj.keys();
                while (iter.hasNext()){
                    String name = iter.next();
                    JSONObject movie = (JSONObject) jsonObj.get(name);
                    if(movieMap.containsKey(movie.get("Genre").toString())){
                        movieDescr = movieMap.get(movie.get("Genre").toString());
                        movieDescr.put(name, movie);
                    }else {
                        movieDescr = new LinkedHashMap<String,JSONObject>();
                        movieDescr.put(name, movie);
                        movieMap.put(movie.get("Genre").toString(),movieDescr);
                    }

                }
            }catch (Exception e){
                android.util.Log.e(e.toString(), "Error in converting JSON String to JSON Object");
            }
        }
    }

    public LinkedHashMap<String,LinkedHashMap<String,JSONObject>> getMovieMap(){
        return movieMap;
    }

    public void addMovie(JSONObject jsonMovie) throws JSONException {
        String genre = (String) jsonMovie.get("Genre");
        String title = (String) jsonMovie.get("Title");
        movieMap.get(genre).put(title,jsonMovie);
    }

    public void deleteMovie(String genre, String title) throws JSONException {
        HashMap<String,JSONObject> childHash = movieMap.get(genre);
        childHash.remove(title);
    }

    public String getMovie(String movie, String genre) throws JSONException {
        HashMap<String,JSONObject> childHash = movieMap.get(genre);
        JSONObject mov = childHash.get(movie);
        return mov.toString();
    }
}
