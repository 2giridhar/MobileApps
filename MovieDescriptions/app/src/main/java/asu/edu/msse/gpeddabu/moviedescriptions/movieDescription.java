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

import org.json.JSONObject;

public class movieDescription {
    public String title;
    public String year;
    public String rated;
    public String released;
    public String runtime;
    public String genre;
    public String actors;
    public String plot;
    public String poster;

    movieDescription(String JSONstr){
        try{
            JSONObject movieJson = new JSONObject(JSONstr);
            title = movieJson.getString("Title");
            year = movieJson.getString("Year");
            rated = movieJson.getString("Rated");
            released = movieJson.getString("Released");
            runtime = movieJson.getString("Runtime");
            genre = movieJson.getString("Genre");
            actors = movieJson.getString("Actors");
            plot = movieJson.getString("Plot");
            poster = movieJson.getString("Plot");

        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }

    movieDescription(String title, String year, String rated, String released, String runtime,
            String genre, String actors, String plot, String poster){
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
        this.poster = poster;
    }
    public String toJsonString(){
        String movieStr = "";
        try{
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("Title",title);
            jsonObj.put("Year",year);
            jsonObj.put("Rated",rated);
            jsonObj.put("Released",released);
            jsonObj.put("Runtime",runtime);
            jsonObj.put("Genre",genre);
            jsonObj.put("Actors",actors);
            jsonObj.put("Plot",plot);
            jsonObj.put("Poster",poster);
            movieStr = jsonObj.toString();
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return movieStr;
    }

}

