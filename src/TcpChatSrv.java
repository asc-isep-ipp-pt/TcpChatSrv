


public class TcpChatSrv {
    private static final int TCP_SERVER_PORT=9999;
    private static final int HTTP_SERVER_PORT=2226;
    private static final String WEB_ROOT_FOLDER="www";

    public static void main(String args[]) throws Exception {

        Thread tcpServer=new TcpServer(TCP_SERVER_PORT);
        tcpServer.start();

        Thread httpServer=new HttpServer(HTTP_SERVER_PORT, WEB_ROOT_FOLDER);
        httpServer.start();

        tcpServer.join();
    }
}


