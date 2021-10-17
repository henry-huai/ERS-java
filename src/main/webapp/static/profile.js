fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/login",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    console.log('Success:', data);
    let f = document.getElementById("fname");
    f.innerHTML=`<p >First Name: ${data.firstName}</p>`
    let l = document.getElementById("lname");
    l.innerHTML=`<p >Last Name: ${data.lastName}</p>`
    let e = document.getElementById("email");
    e.innerHTML=`<p >Email Address: ${data.email}</p>`
    let p = document.getElementById("position");
    p.innerHTML=`<p >Position: ${data.userRole}</p>`    
});

 