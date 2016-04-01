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
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class MovieListAdapter extends BaseExpandableListAdapter
            implements View.OnTouchListener,
            ExpandableListView.OnGroupExpandListener,
            ExpandableListView.OnGroupCollapseListener{

    private TextView currSelection = null;
    private MainActivity parent;
    //linked hash map ensures consistent order for iteration and toarray.
    private LinkedHashMap<String,ArrayList<String>> model;

    //Constructor
    MovieListAdapter(MainActivity parent, LinkedHashMap<String,ArrayList<String>> model){
        // logging class calls for tracing back the methods stack.
        android.util.Log.d(this.getClass().getSimpleName(),"in constructor so creating new model");
        this.parent = parent;
        this.model = model;
    }

    @Override
    public int getGroupCount() {
        return model.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Set<String> keySet = model.keySet();
        Iterator<String> iter = keySet.iterator();
        for (int i = 0; i < groupPosition; i++)
            iter.next();
        String key = iter.next().toString();
        return model.get(key).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        Set<String> keySet = model.keySet();
        Iterator<String> iter = keySet.iterator();
        for (int i = 0; i < groupPosition; i++)
            iter.next();
        return iter.next().toString();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Set<String> keySet = model.keySet();
        Iterator<String> iter = keySet.iterator();
        for (int i = 0; i < groupPosition; i++)
            iter.next();
        String key = iter.next().toString();
        ArrayList<String> list = model.get(key);
        return list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if (convertView == null) {
            android.util.Log.d(this.getClass().getSimpleName(),"in getGroupView null so creating new view");
            LayoutInflater inflater = (LayoutInflater) this.parent
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_group, null);
        }

        TextView genreName = (TextView) convertView.findViewById(R.id.genreName);
        //android.util.Log.d(this.getClass().getSimpleName(),"in getGroupView text is: "+lblListHeader.getText());
        genreName.setTypeface(null, Typeface.BOLD);
        genreName.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            android.util.Log.d(this.getClass().getSimpleName(),"in getChildView null so creating new view");
            LayoutInflater inflater = (LayoutInflater) this.parent
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_list, null);
        }
        TextView txtListChild = (TextView)convertView.findViewById(R.id.movieTitle);
        convertView.setOnTouchListener(this);
        convertView.setBackgroundResource(R.color.light_blue);
        txtListChild.setText(childText);
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        android.util.Log.d(this.getClass().getSimpleName(),"in onGroupCollapse called for: "+
                model.keySet().toArray(new String[] {})[groupPosition]);
        if (currSelection != null){
            currSelection.setBackgroundColor(parent.getResources().getColor(R.color.light_blue));
            currSelection = null;
        }
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        android.util.Log.d(this.getClass().getSimpleName(),"in onGroupExpand called for: "+
                model.keySet().toArray(new String[] {})[groupPosition]);
        if (currSelection != null){
            currSelection.setBackgroundColor(parent.getResources().getColor(R.color.light_blue));
            currSelection = null;
        }
        for (int i=0; i< this.getGroupCount(); i++) {
            if(i != groupPosition){
                //parent.elview.collapseGroup(i);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // when the user touches an item, onTouch is called for action down and again for action up
        // we only want to do something on one of those actions. event tells us which action.
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            // onTouch is passed the textview's parent view, a linearlayout - what we set the
            // event on. Look at its children to find the textview
            if(v instanceof android.widget.LinearLayout){
                android.widget.LinearLayout layView = (android.widget.LinearLayout)v;
                // the layout (from list_item.xml should only have one child, but, here's how
                // you find the children of a layout or other view group.
                for(int i=0; i<=layView.getChildCount(); i++){
                    if(layView.getChildAt(i) instanceof TextView){
                        // keep track of TV stuff was most recently touched to un-highlighted
                        if (currSelection != null){
                            currSelection.setBackgroundColor(
                                    parent.getResources().getColor(R.color.light_blue));
                        }
                        TextView tmp = ((TextView)layView.getChildAt(i));
                        currSelection = tmp;
                        parent.setSelectedStuff(tmp.getText().toString());
                        // create an intent (in the name of the parent activity) to start the WebViewActivity
                        // pass the web view activity two strings: the url and the name of the selected item.
                        // expect the WebViewActivity to return a result, which will be picked up in the
                        // requesting activity -- MainActivity.
                        Intent movieDetails = new Intent(parent, MovieDetails.class);
//                        movieDetails.putExtra("MovieLibrary",(parent.movieLibrary));
                        movieDetails.putExtra("movieName",((TextView) layView.getChildAt(i)).getText().toString());
//                        try {
//                            movieDetails.putExtra("genreName", getGenre(((TextView) layView.getChildAt(i)).getText().toString()));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        };
                        parent.startActivity(movieDetails);
                    }
                }
            }
            // code below should never executes. onTouch is called for textview's linearlayout parent
            if(v instanceof TextView){
                android.util.Log.d(this.getClass().getSimpleName(),"in onTouch called for: " +
                        ((TextView)v).getText());
            }
        }
        return true;
    }

    private String getGenre(String movie) throws JSONException {
        Set<String> keySet = model.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()){
            String gen = iter.next();
            ArrayList<String> list = model.get(gen);
            if(list.contains(movie)){
                return gen;
            }
        }
        return "";
    }

    public ArrayList<CharSequence> getGenreList(){
        Set<String> keySet = model.keySet();
        return new ArrayList<CharSequence>(model.keySet());
    }

}
