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

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddMovie extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ArrayList<String> genreArr;
    private Context con = (Context) this;
    private ArrayAdapter<String> adapter;
    private SearchView searchView;
    private Spinner spinner;
    private String url = "http://www.omdbapi.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        spinner = (Spinner) findViewById(R.id.genreET);
        getGenreList();
    // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genreArr);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final EditText title = (EditText) findViewById(R.id.titleET);
        final Button addButton = (Button) findViewById(R.id.addButton);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(title.getText().toString().length()>0){
                    addButton.setEnabled(true);
                }else{
                    addButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(title.getText().toString().length()>0){
                    addButton.setEnabled(true);
                }else{
                    addButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(title.getText().toString().length()>0){
                    addButton.setEnabled(true);
                }else{
                    addButton.setEnabled(false);
                }
            }
        });
    }

    public void getGenreList(){
        genreArr = new ArrayList<>();
        try{
            MovieDB db = new MovieDB(con);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select distinct genre from movie;", null);
            while (cur.moveToNext()){
                genreArr.add(cur.getString(0));
            }
            cur.close();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting genre info: "+
                    ex.getMessage());
        }
    }

    public void onAddButtonClick(View view) throws JSONException {

        // Adding movie title
        EditText tempText = (EditText) findViewById(R.id.titleET);
        String title = tempText.getText().toString();

        // Adding movie Year
        tempText = (EditText) findViewById(R.id.yearET);
        String year = tempText.getText().toString();

        // Adding movie Rated
        tempText = (EditText) findViewById(R.id.ratedET);
        String rated = tempText.getText().toString();

        // Adding movie Released On
        tempText = (EditText) findViewById(R.id.releasedOnET);
        String released = tempText.getText().toString();

        // Adding movie Runtime
        tempText = (EditText) findViewById(R.id.runtimeET);
        String runtime = tempText.getText().toString();

        // Adding movie Genre
        String genre = (String) spinner.getSelectedItem();

        // Adding movie Actors
        tempText = (EditText) findViewById(R.id.actorsET);
        String actors = tempText.getText().toString();

        // Adding movie plot
        tempText = (EditText) findViewById(R.id.plotET);
        String plot = tempText.getText().toString();

        // Adding movie poster url
        tempText = (EditText) findViewById(R.id.posterET);
        String poster = tempText.getText().toString();
        try{
            MovieDB db = new MovieDB(con);
            SQLiteDatabase crsDB = db.openDB();
            crsDB.execSQL("insert into movie values(?,?,?,?,?,?,?,?,?);",
                    new String[]{title, year, rated, released, runtime, genre, actors, plot, poster});
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(), "Exception adding movie info: " +
                    ex.getMessage());
        }
        Intent ni = new Intent(this,MainActivity.class);
        startActivity(ni);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.movie_search_menu, menu);
        // setup the search in action bar
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try{
            android.util.Log.d(this.getClass().getSimpleName(), "in onQueryTextChange: " + query);
            String urlStr = "http://www.omdbapi.com/?t="+query+"&y=&plot=short&r=json";
            MethodInformation mi = new MethodInformation(this, urlStr);
            AsyncLibraryConnect ac = (AsyncLibraryConnect) new AsyncLibraryConnect().execute(mi);
            searchView.clearFocus();
        }finally {
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void fillDataFromSearchResult(JSONObject movieJson){
        try {
            // Adding movie title
            EditText tempText = (EditText) findViewById(R.id.titleET);
            tempText.setText(movieJson.getString("Title"));

            // Adding movie Year
            tempText = (EditText) findViewById(R.id.yearET);
            tempText.setText(movieJson.getString("Year"));

            // Adding movie Rated
            tempText = (EditText) findViewById(R.id.ratedET);
            tempText.setText(movieJson.getString("Rated"));

            // Adding movie Released On
            tempText = (EditText) findViewById(R.id.releasedOnET);
            tempText.setText(movieJson.getString("Released"));

            // Adding movie Runtime
            tempText = (EditText) findViewById(R.id.runtimeET);
            tempText.setText(movieJson.getString("Runtime"));;

            // Adding movie Genre
            String genreStr = movieJson.getString("Genre").split(",",2)[0];
            if(genreArr.contains(genreStr)){
                spinner.setSelection(adapter.getPosition(genreStr));
            }else{
                genreArr.add(genreStr);
//                adapter.add(genreStr);
                adapter.notifyDataSetChanged();
                spinner.setSelection(adapter.getPosition(genreStr));
            }

            // Adding movie Actors
            tempText = (EditText) findViewById(R.id.actorsET);
            tempText.setText(movieJson.getString("Actors"));

            // Adding movie plot
            tempText = (EditText) findViewById(R.id.plotET);
            tempText.setText(movieJson.getString("Plot"));

            // Adding movie poster url
            tempText = (EditText) findViewById(R.id.posterET);
            tempText.setText(movieJson.getString("Poster"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
