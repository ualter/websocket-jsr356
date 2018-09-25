var ws;
var wsXp;

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
              "   \"lat\":\"41.549833\",\n" +
              "   \"lng\":\"2.0989322\"\n" +
              "}\n";
	var log = document.getElementById("xplaneMessage");
	log.innerHTML = str + "\n";
}