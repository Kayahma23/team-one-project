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
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import java.io.*;  
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*; 

/**
 * When the user submits the form, Blobstore processes the file upload and then forwards the request
 * to this servlet. This servlet can then process the request using the file URL we get from
 * Blobstore.
 */
@WebServlet("/my-form-handler")
public class FormHandlerServlet extends HttpServlet {

  // Stores the image's url and image's text to use between get and post methods
  BlobKey blobKey = null;
  String langCode = "en";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Get the BlobKey that points to the image uploaded by the user
    blobKey = getBlobKey(request, "image");

    // User didn't upload a file, so render an error message.
    if (blobKey == null) {
      out.println("Please upload an image file.");
      return;
    }

    // Get the request parameters.
    // error check this
    langCode = request.getParameter("language");

    // Redirect back to main page
    response.sendRedirect("/index.jsp");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (blobKey != null) {
        // Set content type and character encoding and instantiate output stream
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();


        // Print out image uploaded and link its own url (upon clicking it, the image will open up)        
        out.println("<p>Here's the image you uploaded:</p>");
        String imageUrl = getUploadedFileUrl(blobKey);
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        out.println("<a class=\"imageContent\" href=\"" + imageUrl + "\">");
        out.println("<img src=\"" + imageUrl + "\" /></a>");

        // // Get and print the text in the image
        byte[] blobBytes = getBlobBytes(blobKey);
        String imageText = getImageText(blobBytes, out);
        if (imageText == null) {
            // If an error arises while parsing text from image
            out.println("Error grabbing text from image.");
            return;
        }

        // Do the translation.
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation = 
            translate.translate(imageText, Translate.TranslateOption.targetLanguage(langCode));
        String translatedText = translation.getTranslatedText();

        // Output the translation.
        out.println("<div id=\"translation\">");
        out.println(translatedText);
        out.println("</div>");
    }
  }

  /** Returns a URL that points to the uploaded file. */
  private String getUploadedFileUrl(BlobKey blobKey) {

    // Instantiate Image Service Factory instance
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    String url = imagesService.getServingUrl(options);

    // GCS's localhost preview is not actually on localhost, so make the URL relative to the current domain.
    if(url.startsWith("http://localhost:8080/")){
      url = url.replace("http://localhost:8080/", "/");
    }
    return url;
  }

   /**
   * Returns the BlobKey that points to the file uploaded by the user, or null if the user didn't
   * upload a file.
   */
  private BlobKey getBlobKey(HttpServletRequest request, String formInputElementName) {
    // Instantiate Blobstore Service Factory Instance and get the blobkey of the image uploaded
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    // User submitted form without selecting a file, so we can't get a BlobKey. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so the BlobKey is empty. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    return blobKey;
  }

  /**
   * Blobstore stores files as binary data. This function retrieves the binary data stored at the
   * BlobKey parameter.
   */
  private byte[] getBlobBytes(BlobKey blobKey) throws IOException {
    // Instantiate Blobstore Service Factory Instance and get byte array output stream
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();

    int fetchSize = BlobstoreService.MAX_BLOB_FETCH_SIZE;
    long currentByteIndex = 0;
    boolean continueReading = true;
    while (continueReading) {
      // end index is inclusive, so we have to subtract 1 to get fetchSize bytes
      byte[] b = blobstoreService.fetchData(blobKey, currentByteIndex, currentByteIndex + fetchSize - 1);
      outputBytes.write(b);

      // if we read fewer bytes than we requested, then we reached the end
      if (b.length < fetchSize) {
        continueReading = false;
      }

      currentByteIndex += fetchSize;
    }
    return outputBytes.toByteArray();
  }

  /**
   * Uses the Google Cloud Vision API to generate the text in the image
   * represented by the binary data stored in imgBytes.
   */
  private String getImageText(byte[] imgBytes, PrintWriter out) throws IOException {
    // Get byte string and make the image, feature and request instances
    ByteString byteString = ByteString.copyFrom(imgBytes);
    Image image = Image.newBuilder().setContent(byteString).build();
    Feature feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
    List<AnnotateImageRequest> requests = new ArrayList<>();
    requests.add(request);

    // Create client, annotate the image and close client
    ImageAnnotatorClient client = ImageAnnotatorClient.create();
    BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
    client.close();

    List<AnnotateImageResponse> imageResponses = batchResponse.getResponsesList();

    System.out.println("imageResponses size" + imageResponses.size())
    for (AnnotateImageResponse res : imageResponses) {
      if (res.hasError()) {
        // If any response has error
        return null;
      } 
    }  

    // Print out text in image
    TextAnnotation annotation = imageResponses.get(0).getFullTextAnnotation();
    out.println("<br/><br/><div charset=\"UTF-8\" id=\"original\">");
    out.println("<br/>" + annotation.getText().replace("\n", "<br/>"));
    out.println("</div>");
    return annotation.getText().replace("\n", "<br/>").replaceAll("&#39;","'");
  }

}