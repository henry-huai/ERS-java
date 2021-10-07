

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
        if(request.status > 0){
            tableRow.innerHTML=`<td>${request.request_id}</td><td>${request.description}</td><td>Approved</td><td>${request.user_id}</td><td>${request.status}</td>`
        }else{
            tableRow.innerHTML=`<td>${request.request_id}</td><td>${request.description}</td><td>Denied</td><td>${request.user_id}</td><td>${-request.status}</td>`
        }
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
        //let rows = document.querySelectorAll('tr');
        let rowIndex = document.getElementById("pending-table").rows.length;
        tableRow.innerHTML=`<td>${rowIndex}</td><td>${request.description}</td><td>Pending</td><td>${request.user_id}</td>
        <td><div class="dropdown">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Action
        </button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
          <a  class="dropdown-item" onclick="sendAction('Approve');">Approve</a>
          <a  class="dropdown-item" onclick="sendAction('Deny');">Deny</a>
        </div>
      </div></td>`
        tableBody.appendChild(tableRow);
        // let z = document.createElement("td");
        // z.innerHTML = `<td style="visibility:hidden;">${tableRow.rowIndex}</td>`;
        // tableRow.append(z);
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
    let h = document.getElementById("manager-header");
    h.innerHTML=`<h1>Welcome ${data.firstName}</h1>`    
})



// document.getElementById("dropdown-action").addEventListener("click", sendAction);

// function sendAction(action){
//     const request_id = document.getElementById
//     const requestBody = new URLSearchParams(`request_id=${userId}&password=${password}`);


//     fetch("http://localhost:8081/project1/updatingrequest",{

//     })

// }
