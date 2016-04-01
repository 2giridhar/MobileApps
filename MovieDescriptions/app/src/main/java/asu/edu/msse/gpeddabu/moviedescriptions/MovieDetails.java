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
import android.widget.TextView;

public class MovieDetails extends AppCompatActivity {
    private String title;
    private Context cont = (Context) this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent i = getIntent();
        title = i.getStringExtra("movieName");
        setTitle(title);
        try{
            MovieDB db = new MovieDB(cont);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select * from movie where title=?;",
                    new String[]{title});
            while (cur.moveToNext()){

                TextView tempText =(TextView)findViewById(R.id.year);
                tempText.setText(cur.getString(1));
                tempText =(TextView)findViewById(R.id.rated);
                tempText.setText(cur.getString(2));
                tempText =(TextView)findViewById(R.id.released);
                tempText.setText(cur.getString(3));
                tempText =(TextView)findViewById(R.id.runtime);
                tempText.setText(cur.getString(4));
                tempText =(TextView)findViewById(R.id.genre);
                tempText.setText(cur.getString(5));
                tempText =(TextView)findViewById(R.id.actors);
                tempText.setText(cur.getString(6));
                tempText =(TextView)findViewById(R.id.plot);
                tempText.setText(cur.getString(7));
                tempText =(TextView)findViewById(R.id.poster);
                tempText.setText(cur.getString(8));
            }
            cur.close();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(), "Exception getting movie info: " +
                    ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.movie_description_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                // User chose the "Back Button" item. Log it
                android.util.Log.d(this.getClass().getSimpleName(),"back button selected");
                finish();
                return true;
            case R.id.action_delete:
                android.util.Log.d(this.getClass().getSimpleName(), "Delete button selected");
                try{
                    MovieDB db = new MovieDB(cont);
                    SQLiteDatabase crsDB = db.openDB();
                    crsDB.execSQL("delete from movie where title=?;",
                            new String[]{title});
                    Cursor cur = crsDB.rawQuery("select title from movie;", null);
                    while (cur.moveToNext()){
                        android.util.Log.w(this.getClass().getSimpleName(),cur.getString(0));
                    }
                    cur.close();
                    crsDB.close();
                    db.close();
                } catch (Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception getting movie info: " +
                            ex.getMessage());
                }
                Intent ni = new Intent(this,MainActivity.class);
                startActivity(ni);
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
