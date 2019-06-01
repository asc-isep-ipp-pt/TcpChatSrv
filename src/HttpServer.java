import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer extends Thread {

    private int serverPort;
    private String webRootFolder;

    public HttpServer(int port, String root) {serverPort=port; webRootFolder=root;}

    @Override
    public void run() {
        ServerSocket sock;
        Socket cliSock;
        try { sock = new ServerSocket(serverPort); }
        catch(IOException ex) {
            System.out.println("HTTP server: local port number " + serverPort + " not available - server aborted");
            return;
        }
        System.out.println("HTTP server: ready, listening on local port number " + serverPort);
        while(true) {

            try { cliSock=sock.accept(); }
            catch(IOException ex) {
                System.out.println("HTTP server: failed to accept client connection");
                cliSock=null;
            }
            if(cliSock!=null) {
                Thread cliThread = new HttpServerConn(cliSock,webRootFolder);
                cliThread.start();
                System.out.println("HTTP server: new client connection from " + cliSock.getInetAddress().getHostAddress() +
                        ", port number " + cliSock.getPort());

            }
        }
    }
}
