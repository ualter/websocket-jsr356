var ws;
var wsXp;
var lat = 41.5497669;
var lng = 2.0989116;

function connect() {
    var username = document.getElementById("username").value;
    
    var host = document.location.host;
    var pathname = document.location.pathname;
    
    ws = new WebSocket("ws://" + host  + pathname + "chat/" + username);

    ws.onmessage = function(event) {
    	var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}

function connectXPlane() {
    var host = document.location.host;
    var pathname = document.location.pathname;
    
    wsXp = new WebSocket("ws://" + host  + pathname + "xplane/");

    wsXp.onmessage = function(event) {
    	var log = document.getElementById("logXPlane");
        console.log(event.data);
        var message = event.data;
        log.innerHTML += message + "\n";
    };
}

function sendXPlane() {
    var content = document.getElementById("xplaneMessage").value;
    wsXp.send(content);
}

function info1() {
	var str = "{\n" +
              "   \"messageId\":\"0\",\n" +
              "   \"lat\":\"41.5497669\",\n" +
              "   \"lng\":\"2.0989116\"\n" +
              "}\n";
	var log = document.getElementById("xplaneMessage");
	log.innerHTML = str + "\n";
}

function sendLocation() {
	var str = "{\n" +
    "   \"messageId\":\"0\",\n" +
    "   \"lat\":\""+ lat + "\",\n" +
    "   \"lng\":\""+ lng + "\"\n" +
    "}\n";
	wsXp.send(str);
}

function goNorth() {
	lat += 0.001;
	sendLocation();
}
function goNorthwest() {
	lat += 0.001;
	lng -= 0.001;
	sendLocation();
}
function goNortheast() {
	lat += 0.001;
	lng += 0.001;
	sendLocation();
}
function goSouth() {
	lat -= 0.001;
	sendLocation();
}
function goSouthwest() {
	lat -= 0.001;
	lng -= 0.001;
	sendLocation();
}
function goSoutheast() {
	lat -= 0.001;
	lng += 0.001;
	sendLocation();
}

function goEast() {
	lng += 0.001;
	sendLocation();
}
function goWest() {
	lng -= 0.001;
	sendLocation();
}