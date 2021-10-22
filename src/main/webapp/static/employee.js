//document.getElementById("new-request-b").addEventListener("click", submitRequest);
//onclick="generateBase64(); return false;"


fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/resolved",{
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
            tableRow.innerHTML=`<td style="text-align:center">${rowIndex}</td><td>${request.description}</td><td>Approved</td><td style="text-align:center">${request.request_id}</td><td>${request.category}</td><td>${request.transaction_date}</td>`
        }else{
            tableRow.innerHTML=`<td style="text-align:center">${rowIndex}</td><td>${request.description}</td><td>Denied</td><td style="text-align:center">${request.request_id}</td><td>${request.category}</td><td>${request.transaction_date}</td>`
        }
        tableBody.appendChild(tableRow);
    }
});


fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/pending",{
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
        
        tableRow.innerHTML=`<td style="text-align:center">${rowIndex}</td><td>${request.description}</td><td>Pending</td><td id="pending-request-id" style="text-align:center">${request.request_id}</td><td>${request.category}</td><td>${request.transaction_date}</td><td onClick="openImage()">View</td>`
        tableBody.appendChild(tableRow);
    }
});




fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/login",{
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

let category = "";
function updateCategory() {
    var select = document.getElementById('categories');
    category = select.options[select.selectedIndex].value;
}



function submitRequest(){  

    const requestDescription = document.getElementById("new-request-description").value;
    const requestBody = new URLSearchParams(`description=${requestDescription}&base64=${base64String}&category=${category}`);

    fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/request",{
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
    window.location.href="http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/static/login.html";
}

function openImage() {

    // var request_id = $(this).closest("td").value;
    // window.alert(request_id);


    fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/request",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token'),
        'Request':'3'       
    },
    //body:requestBody
})
.then(response => response.json())
.then(data=>{

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

