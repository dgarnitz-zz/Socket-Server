import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket conn;       // socket representing TCP/IP connection to Client
    private InputStream is;    // get data from client on this input stream
    private OutputStream os;   // can send data back to the client on this output stream
    BufferedReader br;         // use buffered reader to read client data

    public ConnectionHandler(Socket conn) {
        this.conn = conn;
        try{
            is = conn.getInputStream();     // get data from client on this input stream
            os = conn.getOutputStream();  // to send data back to the client on this stream
            br = new BufferedReader(new InputStreamReader(is)); // use buffered reader to read client data
        } catch (IOException ioe){
            System.out.println("ConnectionHandler: " + ioe.getMessage());
        }
    }

    public void handleClientRequest() {
        System.out.println("new ConnectionHandler thread started .... "); //I don't think this actually starts a new thread
        try {
            printClientData();
        } catch (Exception e) {
            System.out.println("ConnectionHandler:run " + e.getMessage());
            cleanup();
        }
    }

    private void checkRequest(String line) {
        System.out.println("ConnectionHandler: " + line);
        String[] requestLine = line.split(" ");

        if(requestLine[0].startsWith("GET")) {
            System.out.println("ConnectionHandler: This contains a GET method: " + line);
            HeadRequest.simpleWriter(os,"HTTP/1.1 200 OK");
        } else if(requestLine[0].startsWith("HEAD")) {
            System.out.println("ConnectionHandler: This contains a HEAD method: " + line);
            HeadRequest.simpleWriter(os,"HTTP/1.1 200 OK");
        }
    }

    private void printClientData() throws DisconnectedException, IOException {
        while(true) {
            String line = br.readLine(); // get data from client over socket
            // if readLine fails we can deduce here that the connection to the client is broken
            // and shut down the connection on this side cleanly by throwing a DisconnectedException
            // which will be passed up the call stack to the nearest handler (catch block)
            // in the run method
            if(line == null || line.equals("null") || line.equals("exit") ){
                throw new DisconnectedException(" ... client has closed the connection ... ");
            }
            // in this simple setup all the server does in response to messages from the client is to send
            // a single ACK byte back to client - the client uses this ACK byte to test whether the
            // connection to this server is still live, if not the client shuts down cleanly
            os.write(WebServerMain.ackByte);
            checkRequest(line);
        }
    }

    private void cleanup(){
        System.out.println("ConnectionHandler: ... cleaning up and exiting ... " );
        try{
            br.close();
            is.close();
            conn.close();
        } catch (IOException ioe){
            System.out.println("ConnectionHandler:cleanup " + ioe.getMessage());
        }
    }

}
