import java.io.*;
import java.net.*;
import java.util.*;

public class HttpServerConn extends Thread {
    private Socket cli;
    private String webRootFolder;
    private DataInputStream sIn;
    private DataOutputStream sOut;

    public HttpServerConn(Socket s, String root) { cli=s; webRootFolder=root;}

    @Override
    public void run() {
        try {
            sIn = new DataInputStream(cli.getInputStream());
            sOut = new DataOutputStream(cli.getOutputStream());
            HTTPmessage request = new HTTPmessage(sIn);
            HTTPmessage response = new HTTPmessage();
            response.setResponseStatus("200 OK");

            if(request.getMethod().equals("GET")) {
                if (request.getURI().equals("/messages")) {
                    String content = ChatMessages.getLastHTML();
                    response.setContent(content, "text/html");
                }
                else { // NOT GET /messages , THEN IT MUST BE A FILE
                    String fullname = webRootFolder + "/";
                    if (request.getURI().equals("/")) fullname = fullname + "index.html";
                    else fullname = fullname + request.getURI();
                    if (!response.setContentFromFile(fullname)) {
                        response.setContentFromString(
                                "<html><body><h1>404 File not found</h1></body></html>",
                                "text/html");
                        response.setResponseStatus("404 Not Found");
                    }
                }
            }
            else {
                if (request.getMethod().equals("PUT")) {
                    if (request.getURI().equals("/messages")) {
                        ChatMessages.push(request.getContentAsString());
                        TcpServerConn.sendToAll(request.getContent().length, request.getContent());
                    } else {
                        response.setContentFromString(
                                "<html><body><h1>404 File not found</h1></body></html>",
                                "text/html");
                        response.setResponseStatus("404 Not Found");
                    }
                } else {
                    response.setContentFromString(
                            "<html><body><h1>ERROR: 405 Method Not Allowed</h1></body></html>",
                            "text/html");
                    response.setResponseStatus("405 Method Not Allowed");
                }
            }
            response.send(sOut);
        }
        catch(Exception ex) { System.out.println("HTTP I/O error"); }
        try { cli.close(); } catch(Exception ex) { System.out.println("HTTP I/O error closing client connection"); }
    }
}

