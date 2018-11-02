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
            HeadRequest.simpleWriter(os,requestLine);
        } else if(requestLine[0].startsWith("HEAD")) {
            HeadRequest.simpleWriter(os,requestLine);
        }
    }

    private void printClientData() throws DisconnectedException, IOException {
        while(true) {
            String line = br.readLine();
            if(line == null || line.equals("null") || line.equals("exit") ){
                throw new DisconnectedException(" ... client has closed the connection ... ");
            }

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
