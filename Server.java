import java.io.IOException;
import java.net.*;

/**
 * The Server class handles the creation of a Java ServerSocket-based server.
 * Please note that this class is virtually identical to the example Server class found on Studres.
 * For more information, please see:
 * https://studres.cs.st-andrews.ac.uk/2018_2019/CS5001/Examples/L07-10_IO_and_Networking/CS5001_BasicClientServerExample/src/Server.java
 */
public class Server {
    private ServerSocket ss;

    /**
     * The constructor method for the Server class, it takes an int as an argument, containing a port number, enters a
     * while-true loop that creates a Socket object using the ServerSocket's accept method, which will create a
     * Socket object once a successful connection with a client is made. It then creates a ConnectionHandler
     * object using the Socket object, and calls the ConnectionHandler's handleClientRequest method.
     * @param port an int containing the port number that will be used to create a ServerSocket object
     */
    public Server(int port){
        try {
            ss = new ServerSocket(port);
            System.out.println("Server started ... listening on port " + port + " ...");
            while(true){
                Socket conn = ss.accept();
                System.out.println("Server got new connection request from " + conn.getInetAddress());
                ConnectionHandler ch = new ConnectionHandler(conn);
                ch.handleClientRequest();

            }
        } catch (IOException ioe){
            System.out.println("Ooops " + ioe.getMessage());
        }
    }
}
