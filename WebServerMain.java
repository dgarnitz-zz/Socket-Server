import java.io.*;

/**
 * The WebServerMain class runs is the principal class for using this project to run a ServerSocket based server that
 * can receive GET and HEAD requests, and respond
 * @author CS5001 Student (dag8@st-andrews.ac.uk)
 * @version 1
 * @since 1
 */
public class WebServerMain {
    public static String directory;
    public static String fileName;

    /**
     * The main method takes in two string arguments, the first of which is the full, true (not relative) path to a
     * particular directory that will serve request resources, and the second of which is a port number. This method
     * checks to ensure the arguments passed are valid, then instantiates an instance of the server class.
     * @param args a String array containing the arguments passed to the main function
     */
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Usage: java WebServerMain <document_root> <port>");
            return;
        }

        directory = args[0];

        File directoryPath = new File(directory);
        if(!directoryPath.isDirectory()){
            System.out.println("Usage: java WebServerMain <document_root> <port>");
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