<html>
  <head>
    <title>Team one project</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="https://fonts.googleapis.com/css2?family=Carter+One&family=Montserrat:wght@400;600;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/object-fit-images/3.2.4/ofi.min.js"></script> 
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
  </head>
  <body onload="getPicture()">
      <div class="jumbotron">
        <div class="jumbotron-background">
            <img src="images/languagesgood.png" class="blur">
        </div>

        <div class="container">
            <center>  
            <h1 class="display-4">Translate Your Image Text</h1>
            <h2><p>Upload your image. Choose your language. Translate its text.</p></h2>
            <hr class="my-4">
            <p>Google Software Product Sprint Summer 2020</p>
            </center>

        </div> 
      </div>
      <center>
    <!-- form for picture upload -->
    <form method="POST" enctype="multipart/form-data" action="<%= uploadUrl %>">
      <p>Upload an image:</p>
      <input type="file" name="image">
      <br/><br/>
      <button class="button1">Submit</button>
    </form>
    <div id="picture"></div>

    <!-- translation selector  -->
    <p>Select language to translate text</p>
    <select class="button1" id="language" onchange="getTranslation(this);">
     <option value="en">English</option>
     <option value="es">Spanish</option>
     <option value="hi">Hindi</option>
    </select>
    <br></br>
    <div id="result"></div>
    </center>
  </body>
</html> 