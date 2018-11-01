import java.io.IOException;
import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket ss; // listen for client connection requests on this server socket

    public Server(int port){
        try {
            ss = new ServerSocket(port);
            System.out.println("Server started ... listening on port " + port + " ...");
            while(true){
                Socket conn = ss.accept(); // will wait until client requests a connection, then returns connection (socket)
                System.out.println("Server got new connection request from " + conn.getInetAddress());
                ConnectionHandler ch = new ConnectionHandler(conn); // create new handler for this connection
                ch.handleClientRequest();                                         // start handler thread
            }
        } catch (IOException ioe){
            System.out.println("Ooops " + ioe.getMessage());
        }
    }
}
