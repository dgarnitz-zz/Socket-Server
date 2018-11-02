import java.io.IOException;
import java.net.*;
import java.io.*;

public class WebServerMain {
    public static final byte ackByte = 1;

    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("java WebServerMain");
            System.out.println("Usage java WebServerMain <document_root> <port>");
            return;
        }

        //DECOMPOSE this into a method
        //Right now it looks for a file, but you first need to check if its a directory
        //Then you need to actually need to read in each individual file from that directory
        //Plus any subdirectories listed
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            System.out.println("Successfully loaded directory");
        }  catch (FileNotFoundException e) {
            System.out.println("Directory not found : " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Error with I/O" + e.getMessage());
            return;
        }

        // DECOMPOSE this into a method
        int port = Integer.parseInt(args[1]);
        if(port > 65535 || port < 0){
            System.out.println("Invalid port number. Please enter a number between 0 and 65535");
            return;
        }
        Server s = new Server(port);

        return;
    }
}