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
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    public String selectedStuff;
    public MovieListAdapter mlAdapter;
    public ExpandableListView elview;
    private Context cont = (Context) this;
    private static LinkedHashMap<String,ArrayList<String>> movieMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        elview = (ExpandableListView)findViewById(R.id.movieList);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
        mlAdapter = new MovieListAdapter(this, movieMap);
        elview.setAdapter(mlAdapter);
    }

    public void setSelectedStuff(String aStuff){
        this.selectedStuff = aStuff;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.movie_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                android.util.Log.d(this.getClass().getSimpleName(), "Add button selected");
                // create intent to move to create screen
                Intent addMovie = new Intent(this, AddMovie.class);
                startActivity(addMovie);
                return true;

            default:
                // If we get here, the user's action is not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void loadData(){
        try{
            MovieDB db = new MovieDB(cont);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select title, genre from movie;", null);
            String title;
            String genre;
            movieMap = new LinkedHashMap<>();
            ArrayList<String> movieList;
            while (cur.moveToNext()){
                title = cur.getString(0);
                genre = cur.getString(1);
                if(movieMap.containsKey(genre)){
                    movieList = movieMap.get(genre);
                    movieList.add(title);
                }else {
                    movieList = new ArrayList<>();
                    movieList.add(title);
                    movieMap.put(genre,movieList);
                }
            }
            cur.close();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting movie info: "+
                    ex.getMessage());
        }
    }
}
