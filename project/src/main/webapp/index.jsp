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
    <title>Team one project</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
  </head>
  <body onload="getPicture()">

    <!-- form for picture upload -->
    <form method="POST" enctype="multipart/form-data" action="<%= uploadUrl %>">
      <p>Type some text:</p>
      <textarea name="message"></textarea>
      <br/>
      <p>Upload an image:</p>
      <p>test text here!!!</p>
      <input type="file" name="image">
      <br/><br/>
      <button>Submit</button>
    </form>
    <div id="picture"></div>

    <!-- translation selector  -->
    <p id="textFromImage"><%=request.getAttribute("imageText")%></p>
      <p>Select language to translate text</p>
      <select id="language" onchange="getTranslation(this);">
        <option value="en">English</option>
        <option value="es">Spanish</option>
        <option value="hi">Hindi</option>
        <option value="pt">Portuguese</option>
        <option value="it">Italian</option>
        <option value="fr">French</option>
        <option value="ht">Creole</option>
        <option value="he">Hebrew</option>
        <option value="ja">Japanese</option>
      </select>
      <br></br>
      <div id="result"></div>
  </body>
</html>