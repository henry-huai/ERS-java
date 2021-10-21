
fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/pending-request-analyze",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
        var chart = new CanvasJS.Chart("pending-request-chart", {
            title:{
                text: "Pending requests analysis"              
            },
            data: [              
            {
                type: "column",
                dataPoints: [
                    { label: "Business expense",  y: data[0]  },
                    { label: "Travel expense", y: data[1]  },
                    { label: "Food expense", y: data[2]  },
                    { label: "Medical expense",  y: data[3]  },
                ]
            }
            ]
        });
        chart.render();
});


fetch("http://ec2-3-144-234-17.us-east-2.compute.amazonaws.com:8080/project1/resolved-request-analyze",{
    method: 'GET',
    headers:{
        'Authorization': localStorage.getItem('token')       
    },
})
.then(response => response.json())
.then(data=>{
    //https://canvasjs.com/javascript-charts/multiple-axis-column-chart/
        var chart = new CanvasJS.Chart("resolved-request-chart", {
            title:{
                text: "Resolved requests analysis"              
            },
            axisY: {
                title: "approved requests",
                titleFontColor: "#4F81BC",
                lineColor: "#4F81BC",
                labelFontColor: "#4F81BC",
                tickColor: "#4F81BC"
            },
            axisY2: {
                title: "denied requests",
                titleFontColor: "#C0504E",
                lineColor: "#C0504E",
                labelFontColor: "#C0504E",
                tickColor: "#C0504E"
            },	
            toolTip: {
                shared: true
            },
            legend: {
                cursor:"pointer"
            },
            data: [              
            {
                type: "column",
                name: "approved requests",
                legendText: "Approved requests",
                dataPoints: [
                    { label: "business expense",  y: data[0]  },
                    { label: "travel expense", y: data[2]  },
                    { label: "food expense",  y: data[4]  },
                    { label: "medical expense",  y: data[6]  },
                ]
            },
            {
                type: "column",
                name: "denied requests",
                legendText: "Denied requests",
                dataPoints: [
                    { label: "business expense", y: data[1]  },
                    { label: "travel expense",  y: data[3]  },
                    { label: "food expense",  y: data[5]  },
                    { label: "medical expense",  y: data[7]  },
                ]
            }
            ]
        });
        chart.render();
});
