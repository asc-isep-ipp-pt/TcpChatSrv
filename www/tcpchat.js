
function refreshMessages() {
    var request = new XMLHttpRequest();
    request.onload= function upDate() {
        if(this.responseText == "") document.getElementById("messages").innerHTML= "<b>Sorry, no messages yet</b>";
        else document.getElementById("messages").innerHTML= this.responseText;
        setTimeout(refreshMessages, 1500);
    }


    function errorResult() {
        document.getElementById("messages").innerHTML= "Server unreachable, still trying ...";
        setTimeout(refreshMessages, 1000);
    }

    request.ontimeout=errorResult;
    request.onerror=errorResult;

    request.open("GET", "/messages", true);
    request.timeout= 5000;
    request.send();
}

function setNick() {
    if(document.getElementById("nickname").value!=""){
        document.getElementById("nickname").disabled=true;
        document.getElementById("setnick").disabled=true;
        document.getElementById("message").disabled=false;
    }
}

function enterKey(kk) {
    if(kk.keyCode != 13) return;
    sendChatMessage();

}

function sendChatMessage() {
    if(document.getElementById("message").value=="") return;
    var request = new XMLHttpRequest();
    request.open("PUT", "/messages", true);
    request.setRequestHeader("Content-type", "text/plain")
    request.send("(" + document.getElementById("nickname").value + ") " + document.getElementById("message").value);
    document.getElementById("message").value="";
}