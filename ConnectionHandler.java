import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket conn;
    private InputStream is;
    private OutputStream os;
    BufferedReader br;

    public ConnectionHandler(Socket conn) {
        this.conn = conn;
        try{
            is = conn.getInputStream();
            os = conn.getOutputStream();
            br = new BufferedReader(new InputStreamReader(is));
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

    //NEED to rework this function to call the correct functions for certain request types and handle errors
    private void checkRequest(String line) {
        System.out.println("ConnectionHandler: " + line);
        String[] requestLine = line.split(" ");

        if(requestLine[0].startsWith("GET")) {
            Request.simpleWriter(os,requestLine);
            GetRequest.readInResource(os);
        } else {
            Request.simpleWriter(os,requestLine);
        }
    }

    private void printClientData() throws DisconnectedException, IOException {
        while(true) {
            String line = br.readLine();
            if(line == null || line.equals("null") || line.equals("exit") ){
                throw new DisconnectedException(" ... client has closed the connection ... ");
                //SHOULD this function end here. or should it allow the SERVER to send an error message back to CLIENT
            }

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
