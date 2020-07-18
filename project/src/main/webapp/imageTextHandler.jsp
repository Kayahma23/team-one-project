<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html> 
  <head> 
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
   <title>Team One Project</title>
   <script src="/script.js"></script>
   </head>
  <body>
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