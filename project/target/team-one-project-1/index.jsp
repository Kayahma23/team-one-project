<%--
Copyright 2019 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>

<%-- The Java code in this JSP file runs on the server when the user navigates
     to the homepage. This allows us to insert the Blobstore upload URL into the
     form without building the HTML using print statements in a servlet. --%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
   String uploadUrl = blobstoreService.createUploadUrl("/my-form-handler"); %>

<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- <link href="https://fonts.googleapis.com/css2?family=Carter+One&family=Montserrat:wght@400;600;800&display=swap" rel="stylesheet"> -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script type="text/javascript" src="js/bootstrap-filestyle.min.js"> </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/object-fit-images/3.2.4/ofi.min.js"></script> 
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
  </head>
  <body onload="getPicture()">
      <div class="jumbotron">
        <div class="jumbotron-bg"></div>
            <div class="container">
                <center>  
                <h1 class="app-title">PicReader</h1>
                <!-- <hr class="my-4"> -->
                <p>Google Software Product Sprint Summer 2020</p>
                </center>
            </div>
        </div> 
      </div>
    <center><p>Upload your image. Choose your language. Translate its text.</p></center>
    <!-- form for picture upload -->
    <form method="POST" enctype="multipart/form-data" action="<%= uploadUrl %>">
    <center>
    <div class="buttonRow">
      <div class="buttonCol">
        <div class="fileButton">
            <!-- <center> -->
            <label for="file-input">Upload image</label><input type="file" name="image" id="file-input" onchange="showCheck()">
        </div>
      </div>
      <div class="buttonCol"> 
          <img id="checkmark" src="images/lightGreenCheck.png" width="25" height="25">
      </div>
    <!-- <select class="button1" id="language" onchange="getTranslation(this);"> -->
      <div class="buttonCol">
        <select class="selectButton" name="language" id="language">
        <option value="en" selected>Choose language</option>
        <option value="en">English</option>
        <option value="es">Spanish</option>
        <option value="hi">Hindi</option>
        </select>
      </div>
      <div class="buttonCol">
        <button class="mainButtons">Submit</button>
      </div>
    </div>
    </center>
    </form>
    <div id="picture"></div>
    <div id="translation"></div>
  </body>
</html> 