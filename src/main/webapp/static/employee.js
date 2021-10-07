document.getElementById("new-request-b").addEventListener("click", submitRequest);


function submitRequest(){
    const requestDescription = document.getElementById("new-request-description").value;
 
    const requestBody = new URLSearchParams(`description=${requestDescription}`);

    fetch("http://localhost:8081/project1/request",{
        method: 'POST',
        headers:{
            'Authorization': localStorage.getItem('token'),
            'Content-Type': 'application/x-www-form-urlencoded'           
        },
        body:requestBody
    }) 
    .then(reponse=>{
        //localStorage.setItem("token", reponse.headers.get('Authorization'));
        window.location.href="http://localhost:8081/project1/static/employee.html";
        })
      .catch((error) => {
        console.error('Error:', error);
      });
}

fetch("http://localhost:8081/project1/resolved",{
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
        tableRow.innerHTML=`<td>${request.description}</td><td>${request.status}</td><td style="visibility:hidden;">${request.request_id}</td>`
        tableBody.appendChild(tableRow);
    }
})


fetch("http://localhost:8081/project1/pending",{
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
        tableRow.innerHTML=`<td>${request.description}</td><td>${request.status}</td><td style="visibility:hidden;">${request.request_id}</td>`
        tableBody.appendChild(tableRow);
    }
})




fetch("http://localhost:8081/project1/login",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    console.log('Success:', data);
    let h = document.getElementById("employee-header");
    h.innerHTML=`<h1>Welcome ${data.firstName}</h1>`    
})
