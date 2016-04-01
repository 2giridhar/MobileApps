package asu.edu.msse.gpeddabu.moviedescriptions;
/**
 * Created by Giridhar on 3/31/2016.
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
 * @version 3/31/2016
 */
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncLibraryConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {
    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest) {
        try {
            String result = downloadUrl(aRequest[0].urlString);
            aRequest[0].resultAsJson = result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation methodInformation) {
        try {
            if(methodInformation.resultAsJson != "{}"){
                JSONObject movie = new JSONObject(methodInformation.resultAsJson);
                if(movie.getString("Response").equals("True")){
                    methodInformation.searchActivity.fillDataFromSearchResult(movie);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        String response = "{}";
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            br.close();
            response = responseOutput.toString();
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                is.close();
            }
        }
        return response;
    }
}
