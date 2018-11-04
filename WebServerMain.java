import java.io.IOException;
import java.net.*;
import java.io.*;
import java.nio.file.*;

public class WebServerMain {
    public static final byte ackByte = 1;
    public static String directory;
    public static String fileName;

    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Usage: java WebServerMain <document_root> <port>");
            return;
        }

        directory = args[0];

        File directoryPath = new File(directory);
        if(!directoryPath.isDirectory()){
            System.out.println("Please pass a valid directory path as an argument");
            return;
        }

        int port = Integer.parseInt(args[1]);
        if(port > 65535 || port < 0){
            System.out.println("Invalid port number. Please enter a number between 0 and 65535");
            return;
        }

        Server s = new Server(port);

    }
}