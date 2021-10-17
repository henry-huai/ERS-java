fetch("http://localhost:8080/project1/allemployees",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    console.log('Success:', data);
    let tableBody = document.getElementById("employees-table-body");
    for(let employee of data){
        let tableRow = document.createElement("tr");
        let rowIndex = document.getElementById("employees-table").rows.length;
        tableRow.innerHTML=`<td>${rowIndex}</td><td>${employee.firstName}</td><td>${employee.lastName}</td><td>${employee.email}</td><td>${employee.user_id}</td>`
        tableBody.appendChild(tableRow);
    }
})

document.getElementById("add-employee-b").addEventListener("click", addNewEmployee);

function addNewEmployee(e){
  e.preventDefault();
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const requestBody = new URLSearchParams(`firstName=${firstName}&lastName=${lastName}&email=${email}`);
    
    fetch("http://localhost:8080/project1/addemployee",{
        method: 'POST',
        headers:{
            'Authorization': localStorage.getItem('token'), 
            'Content-Type': 'application/x-www-form-urlencoded'  
        },
        body:requestBody
    })
    //.then(response=>console.log(response.status))
    .then(response=>{
      if(response.status==200){
        window.alert("New Employee added")
        location.reload();
      }else{
        window.alert("Invalid user credential");
        location.reload();
      }
    })
      .catch((error) => {
        console.error('Error:', error);
      });
}