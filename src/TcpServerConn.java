import java.io.*;
import java.net.*;
import java.util.*;

public class TcpServerConn extends Thread {

    // Class stuff (static)

    private final static String tcpServerConnIOError = "TCP server: I/O Error. Client connection aborted";

    // List of connected TCP clients
    private static HashMap<Socket,DataOutputStream> cliList = new HashMap<>();

    public static synchronized void sendToAll(int len, byte[] data) throws Exception {
        for(DataOutputStream cOut: cliList.values()) {
            cOut.write(len);
            cOut.write(data,0,len);
        }
    }

    public static synchronized void sendLastMessagesToCli(Socket s) throws Exception {
        ChatMessages.sendLast(cliList.get(s),false);
    }


    public static synchronized void addCli(Socket s) throws Exception {
        cliList.put(s,new DataOutputStream(s.getOutputStream()));
    }

    public static synchronized void remCli(Socket s) throws Exception {
        DataOutputStream cOut = cliList.get(s);
        cliList.remove(s);
        cOut.write(0);
        s.close();
    }


    // Instance stuff

    Socket cli;
    private DataInputStream sIn;

    public TcpServerConn(Socket s) { cli=s; }

    @Override
    public void run() {
        int nChars;
        byte[] data = new byte[300];

        try {
            sIn = new DataInputStream(cli.getInputStream());
            addCli(cli);
            sendLastMessagesToCli(cli);
            while(true) {
                nChars=sIn.read();
                if(nChars==0) break; // an empty line means the client wants to exit
                sIn.read(data,0,nChars);
                sendToAll(nChars,data);
                ChatMessages.push(new String(data,0,nChars));
            }
        }
        catch(Exception ex) { System.out.println(tcpServerConnIOError); }
        System.out.println("TCP server: client " + cli.getInetAddress().getHostAddress() +
                ", port number " + cli.getPort()+ " disconnected");
        try { TcpServerConn.remCli(cli); } catch(Exception ex) { System.out.println(tcpServerConnIOError); }

    }
}
