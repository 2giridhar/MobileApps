package asu.edu.msse.gpeddabu.moviedescriptions;
/**
 * Created by Giridhar on 3/30/2016.
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
 * @version 3/30/2016
 */

public class MethodInformation {

    public AddMovie searchActivity;
    public String urlString;
    public String resultAsJson;

    public MethodInformation(AddMovie addActivity, String urlString){
        this.searchActivity = addActivity;
        this.urlString = urlString;
        this.resultAsJson = "{}";
    }
}
