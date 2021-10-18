document.getElementById("login-b").addEventListener("click", attemptLogin);

function attemptLogin(){
    const userId = document.getElementById("user_id").value;
    const password = document.getElementById("password").value;  
    const requestBody = new URLSearchParams(`user_id=${userId}&password=${password}`);
    
    fetch("http://localhost:8080/project1/login",{
        method: 'POST',
        header:{
            'Content-Type': 'application/x-www-form-urlencoded' 
        },
        body:requestBody
    })
    //.then(response=>console.log(response.status))
    .then(response=>{
      if(response.status===200){
        localStorage.setItem("token", response.headers.get('Authorization'));
        window.location.href=response.headers.get("Location");
      }else{
        window.alert("Invalid user credential");
        location.reload();
      }
    })
      .catch((error) => {
        console.error('Error:', error);
      });
}

// function attemptLogin(){
//     //const errorDiv = document.getElementById("error-div");
//     //errorDiv.hidden = true;


//     // get input values from input fields
//     const user_id = document.getElementById("user_id").value; 
//     const password = document.getElementById("password").value;

//     // we're using const here, rather than var or let

//     console.log(`user_id: ${user_id}, password: ${password}`); // this is a template literal

//     // use XMLHttpRequest to create an http request - POST to http://localhost:8082/auth-demo/login
//                                                     // + request body with credentials
//     const xhr = new XMLHttpRequest();
//     xhr.open("POST", "http://localhost:8080/project1/login");

//     // define onreadystatechange callback for the xhr object (check for readystate 4)
//     xhr.onreadystatechange = function(){

//         if(xhr.readyState===4){
//             // look at status code (either 401 or 200)
//             // if the status code is 401 - indicate to the user that their credentials are invalid
//             if(xhr.status===401){
//                 //errorDiv.hidden = false; 
//                 //errorDiv.innerText = "invalid admin credentials";
//             } else if (xhr.status===200){
//                 // if the status code is 200 - get auth token from response and store it in browser, navigate to another page
//                 const token = xhr.getResponseHeader("Authorization");
//                 console.log(token); //"2:ADMIN"
//                 localStorage.setItem("token", token);
//                 window.location.href="http://localhost:8085/bankapp/static/user.html";
//             } else {
//                 console.log(xhr.status);
//                 //errorDiv.hidden = false; 
//                 //errorDiv.innerText = "unknown error";
//             }
//         }

//     }
    
//     xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//     // send request, with the username and password in the request body
//     const requestBody = `user_id=${user_id}&password=${password}`;
//     xhr.send(requestBody);
// }
