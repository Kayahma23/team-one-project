<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html> 
  <head> 
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
   <!-- <meta charset="UTF-8"> this was in index.html do we need it? --> 
   <title>Team One Project</title>
   <link rel="stylesheet" href="style.css">
   <script src="/script.js"></script>
   <!-- temporary styling this is copied from the jsp example so it can be moved to css -->
    <style>
      /* Form is hidden by default. */
      .hidden {
        display: none;
      }
    </style>
   </head>
  <body>

      <!-- picture upload form -->
      <form id="my-form" class="hidden" method="POST" enctype="multipart/form-data">
        <p>Upload an image:</p>
        <input type="file" name="image">
        <br/><br/>
        <button>Submit</button>
        
      </form>
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