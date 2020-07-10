// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;


import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

@Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the request parameters.
    String originalText = request.getParameter("text");
    String languageCode = request.getParameter("languageCode");
    
    String text = getParameter(request, "text-input", "");
    String fname = getParameter(request, "fname", "Anonymous");
    System.out.println(fname);
    String lname = getParameter(request, "lname", "Anonymous");
    System.out.println(lname);
    String message = getParameter(request, "message", "");
    System.out.println(message);
    String image = getParameter(request, "image", "");
    System.out.println(image);
    String fileUrl = getParameter(request, "imageUrl", "");
    System.out.println(fileUrl);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity messageEntity = new Entity("Message");
    messageEntity.setProperty("text", text);
    messageEntity.setProperty("lname", lname);
    messageEntity.setProperty("fname", fname);
    messageEntity.setProperty("message", message);
    messageEntity.setProperty("image", image);
  //messageEntity.setProperty("dropdown", dropdown);
    messageEntity.setProperty("timestamp", System.currentTimeMillis());

    datastore.put(messageEntity);

    // Do the translation.
    Translate translate = TranslateOptions.getDefaultInstance().getService();
    Translation translation =
        translate.translate(originalText, Translate.TranslateOption.targetLanguage(languageCode));
    String translatedText = translation.getTranslatedText();

    response.sendRedirect("/data");
    response.sendRedirect("/index.html");

    // Output the translation.
    response.setContentType("text/html; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(translatedText);

    // Get the message entered by the user.
    //String message = request.getParameter("message"); 
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<String> comment = new ArrayList<String>();
    for (Entity entity : results.asIterable()) {
      String messageq = (String) entity.getProperty("text");
      response.getOutputStream().println(messageq);
      String fileUrl = (String) entity.getProperty("imageUrl");
      response.getOutputStream().println(fileUrl);
    }   
    String json = new Gson().toJson(comment);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  } 
  
  private String getParameter(HttpServletRequest request, String text, String defaultValue){
    String value = request.getParameter(text);
    if (value == null) {
        System.out.println(defaultValue);
        return defaultValue;
    }
    return value;
  }

}



