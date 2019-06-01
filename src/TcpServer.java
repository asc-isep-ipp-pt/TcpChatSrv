import java.net.*;
import java.io.*;
import java.util.*;

public class TcpServer extends Thread {

    private int serverPort;

    public TcpServer(int port) {serverPort=port;}

    @Override
    public void run() {
        ServerSocket sock;
        Socket cliSock;
        try { sock = new ServerSocket(serverPort); }
        catch(IOException ex) {
            System.out.println("TCP server: local port number " + serverPort + " not available - server aborted");
            return;
        }
        System.out.println("TCP server: ready, listening on local port number " + serverPort);
        while(true) {

            try { cliSock=sock.accept(); }
            catch(IOException ex) {
                System.out.println("TCP server: failed to accept client connection");
                cliSock=null;
            }
            if(cliSock!=null) {
                Thread cliThread = new TcpServerConn(cliSock);
                cliThread.start();
                System.out.println("TCP server: new client connection from " + cliSock.getInetAddress().getHostAddress() +
                        ", port number " + cliSock.getPort());

            }
        }
    }
}
