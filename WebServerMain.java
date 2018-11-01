import java.io.IOException;
import java.net.*;
import java.io.*;

public class WebServerMain {
    public static final byte ackByte = 1;

    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("java WebServerMain");
            System.out.println("Usage java WebServerMain <document_root> <port>");
            return;
        }

        //NEED a try catch statement to make sure directory provided by user is valid

        String directory = args[0];
        int port = Integer.parseInt(args[1]);
        Server s = new Server(port);


        return;
    }
}