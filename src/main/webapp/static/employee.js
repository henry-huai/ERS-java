//document.getElementById("new-request-b").addEventListener("click", submitRequest);

fetch("http://localhost:8080/project1/resolved",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    console.log('Success:', data);
    let tableBody = document.getElementById("resolved-table-body");
    for(let request of data){
        let tableRow = document.createElement("tr");
        let rowIndex = document.getElementById("resolved-table").rows.length;
        if(request.status > 0){
            tableRow.innerHTML=`<td>${rowIndex}</td><td>${request.description}</td><td>Approved</td><td>${request.request_id}</td>`
        }else{
            tableRow.innerHTML=`<td>${rowIndex}</td><td>${request.description}</td><td>Denied</td><td>${request.request_id}</td>`
        }
        tableBody.appendChild(tableRow);
    }
});


fetch("http://localhost:8080/project1/pending",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    console.log('Success:', data);
    let tableBody = document.getElementById("pending-table-body");
    for(let request of data){
        let tableRow = document.createElement("tr");
        let rowIndex = document.getElementById("pending-table").rows.length;
        
        tableRow.innerHTML=`<td>${rowIndex}</td><td>${request.description}</td><td>Pending</td><td id="pending-request-id">${request.request_id}</td><td>${request.transaction_date}</td><td onClick="openImage()">View</td>`
        tableBody.appendChild(tableRow);
    }
});




fetch("http://localhost:8080/project1/login",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    console.log('Success:', data);
    let h = document.getElementById("employee-header");
    h.innerHTML=`<h5>Welcome employee ${data.firstName}</h5>`    
});



function submitRequest(){  

    const requestDescription = document.getElementById("new-request-description").value;
    const requestBody = new URLSearchParams(`description=${requestDescription}&base64=${base64String}`);

    fetch("http://localhost:8080/project1/request",{
        method: 'POST',
        headers:{
            'Authorization': localStorage.getItem('token')
        },
        body:requestBody
    })
    .then(response=>{   
        console.log(response.status);
        if(response.status === 200){
            
            location.reload();
        }else if(response.status === 403){
            window.alert("empty request here")    
        }else{
            window.alert("no authorization");
        }
    })
    .catch((error) => {
        console.log('Error:', error)
      });
}


function logout(){
    localStorage.clear();
    window.location.href="http://localhost:8080/project1/static/login.html";
}

function openImage() {

    //var request_id = $(this).closest("td").value;
    //window.alert($(this).closest("td").value);
    //const requestBody = new URLSearchParams(`request_id=1`);

    fetch("http://localhost:8080/project1/request",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token'),
        'Request':'1'       
    },
    //body:requestBody
})
.then(response => response.json())
.then(data=>{
    //console.log('Success:', data);
    // var image = new Image();
    // //console.log("data:image/jpg;base64, "+ request.base64encodedString);
    
   
    // console.log(image.src);

    // var w = window.open('about:blank');
    // w.document.write(image.outerHTML);

    var img = document.createElement("img");
// added `width` , `height` properties to `img` attributes
img.width = "250px";
img.height = "250px";
img.src = "data:image/png;base64," + data;
var preview = document.getElementById("image-div");
preview.appendChild(img);
});

    
}

//https://www.geeksforgeeks.org/how-to-convert-image-into-base64-string-using-javascript/
let base64String = "";
function generateBase64(){
    
var file = document.querySelector(
    'input[type=file]')['files'][0];

var reader = new FileReader();
  
reader.onload = function transfer() {
    base64String = reader.result.replace("data:", "")
        .replace(/^.+,/, "");

}
reader.readAsDataURL(file);

}





  


// function submitRequest(){  
//     const requestDescription = document.getElementById("new-request-description").value;
//     const requestBody = new URLSearchParams(`description=${requestDescription}`);

//     fetch("http://localhost:8081/project1/request",{
//         method: 'POST',
//         headers:{
//             'Authorization': localStorage.getItem('token')
//         },
//         body:requestBody
//     })
//     .then(response=>{   
//         console.log(response.status);
//         if(response.status === 200){
//             window.alert("request submitted")
//             location.reload();
//         }else if(response.status === 403){
//             window.alert("empty request here")    
//         }else{
//             window.alert("no authorization");
//         }
//     })
//     .catch((error) => {
//         console.log('Error:', error)
//       });
// }