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

// function getTranslation(selectedObject) {
//     //set the language code of the selected language
//     document.getElementById('language').value = selectedObject.value;
//     //set variable text to the text from image uploaded by user
//     const text = document.getElementById('textFromImage').innerHTML;
    
//     //set languageCode to the code of the selected language
//     const languageCode = document.getElementById('language').value;

//     //creates new search parameters to be sent with the POST request
//     const params = new URLSearchParams();
//     params.append('text', text);
//     params.append('languageCode', languageCode);

//     //Once language is selected, create a POST request with the text and language code
//     //the doPost method in DataServlet will receive the text and language code and translate the text
//     //and send response with translated text
//     const fetchPromise = fetch('/data', {
//           method: 'POST',
//           body: params
//         });

//     fetchPromise.then(handleResponse);
// }
// //convert the response to raw text
// function handleResponse(response) {
//     const responsePromise = response.text();
//     responsePromise.then(createMessage);
// }
// //build the HTML of the page
// function createMessage(messageText) {
//     const message = document.getElementById('result');
//     message.innerText = messageText.replace(/<br>/g, "\n");
// }

/**
 * Grabs picture from form handler servlet
 */
function getPicture() {
  fetch('/my-form-handler').then((response) => {
        return response.text();
      })
      .then(addToPage);
}

/**
 * Adds picture to the page
 */
function addToPage(response) {
    const picContainer = document.getElementById('picture');
    picContainer.innerHTML = response;
}

/**
 * Shows green check.
 */
function showCheck() {
    document.getElementById("checkmark").style.display = "block";
}

/**
 * Hides green check.
 */
function hideCheck() {
    document.getElementById("checkmark").style.display = "none";
}
