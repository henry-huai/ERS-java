

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
            tableRow.innerHTML=`<td>${rowIndex}</td><td>${request.description}</td><td>Approved</td><td>${request.request_id}</td><td>${request.user_id}</td><td>${request.status}</td>`
        }else{
            tableRow.innerHTML=`<td>${rowIndex}</td><td>${request.description}</td><td>Denied</td><td>${request.request_id}</td><td>${request.user_id}</td><td>${-request.status}</td>`
        }
        tableBody.appendChild(tableRow);
    }
})


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
        //let rows = document.querySelectorAll('tr');
        let rowIndex = document.getElementById("pending-table").rows.length;
        tableRow.innerHTML=`<td>${rowIndex}</td><td>${request.description}</td><td>Pending</td><td>${request.request_id}</td><td>${request.user_id}</td>
        <td><div class="dropdown">
        <button id="action-b" class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Action
        </button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
          <a  class="dropdown-item" onclick="sendAction('request_id=${request.request_id}&action_type=approve');">Approve</a>
          <a  class="dropdown-item" onclick="sendAction('request_id=${request.request_id}&action_type=deny');">Deny</a>
        </div>
      </div></td>`
        tableBody.appendChild(tableRow);
        // let z = document.createElement("td");
        // z.innerHTML = `<td style="visibility:hidden;">${tableRow.rowIndex}</td>`;
        // tableRow.append(z);
    }
})




fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/login",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    console.log('Success:', data);
    let h = document.getElementById("manager-header");
    h.innerHTML=`<h5>Welcome ${data.firstName}</h5>`    
})



//document.getElementById("action-b").addEventListener("click", sendAction);

function sendAction(data){

    const requestBody = new URLSearchParams(data);


    fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/updatingrequest",{
        method:'POST',
        headers:{
            'Authorization': localStorage.getItem('token'), 
            'Content-Type': 'application/x-www-form-urlencoded' 
        },
        body:requestBody
    })

    location.reload();
}

function logout(){
    localStorage.clear();
    window.location.href="http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/static/login.html";
}


