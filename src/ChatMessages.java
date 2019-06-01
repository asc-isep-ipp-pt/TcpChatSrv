import java.io.*;

public class ChatMessages {

    private static final int MAX_MESSAGES=25;

    private static String[] lastMessages = new String[MAX_MESSAGES];
    private static int numMessages=0;

    public static synchronized void sendLast(DataOutputStream cOut, boolean lastFirst) throws IOException {
        if(lastFirst) {
            for(int i=0;i<numMessages;i++) {
                cOut.write(lastMessages[i].length());
                cOut.write(lastMessages[i].getBytes(),0,lastMessages[i].length());
            }
        } else {
            for(int i=numMessages-1;i>=0;i--) {
                cOut.write(lastMessages[i].length());
                cOut.write(lastMessages[i].getBytes(),0,lastMessages[i].length());
            }
        }
    }

    public static synchronized String getLastHTML() {
        String res="";
        for(int i=0;i<numMessages;i++) {
            res=res+"<p>" + lastMessages[i] + "</p>";
        }
        return res;
    }

    public static synchronized void push(String msg) {
        for(int i=numMessages;i>0;i--) {
            if(i<MAX_MESSAGES) {
                lastMessages[i]=lastMessages[i-1];
            }
        }
        lastMessages[0]=msg;
        if(numMessages<MAX_MESSAGES) numMessages++;
    }
}
