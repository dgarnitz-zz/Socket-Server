import java.io.*;
import java.net.Socket;

/**
 * The ConnectionHandler class is responsible for actually handling requests and responses. It has attributes containing
 * one each of Socket, InputStream, OutputStream, and BufferedReader objects, which it uses to read in requests
 * and send responses.
 * While this class has the same name as the as the example class posted on StudRes, slight alterations have been made
 * to the original run and printClientData. Important, the checkRequest method, which is responsible for
 * beginning the actual processing of the request and sending of the response, has been added, enabling the
 * required functionality.
 */
class ConnectionHandler extends Thread {
    private Socket conn;
    private InputStream is;
    private OutputStream os;
    private BufferedReader br;

    /**
     * Constructor method for ConnectionHandler class. Takes a Socket object as an argument, which is uses to set the
     * values of its "is" (InputStream), "os" (OutputStream), and "br" (BufferedReader) attributes.
     * This constructor is identical to the one in the sample code.
     * @param conn the Socket object associated with the ServerSocket object upon which this server is built
     */
    ConnectionHandler(Socket conn) {
        this.conn = conn;
        try{
            is = conn.getInputStream();
            os = conn.getOutputStream();
            br = new BufferedReader(new InputStreamReader(is));
        } catch (IOException ioe){
            System.out.println("ConnectionHandler: " + ioe.getMessage());
        }
    }

    /**
     * run is a void method that runs a try-catch on the printClientData method, which attempts to read
     * in the request sent by the client. This method will call the cleanup method if it cannot successfully read the
     * request, closing all connections.
     */
    @Override
    public void run() {
        System.out.println("new ConnectionHandler thread started .... "); //I don't think this actually starts a new thread
        try {
            printClientData();
        } catch (Exception e) {
            System.out.println("ConnectionHandler:run " + e.getMessage());
            cleanup();
        }
    }

    /**
     * printClientData attempts to read in the request from the client using the buffered reader inside a while-true
     * loop. When the BufferedReader gets to the end of the file, it throws a DisconnectedException,
     * exiting the while-true loop. After it reads in each line of the request, it calls the checkRequest method to
     * start the processing of the request and the sending of a response.
     * @throws DisconnectedException a custom exception thrown when the initial client InputStream reading fails
     * @throws IOException a standard exception thrown when there is an error with input or ouput
     */
    private void printClientData() throws DisconnectedException, IOException {
        while(true) {
            String line = br.readLine();
            if(line == null || line.equals("null") || line.equals("exit") ){
                throw new DisconnectedException(" ... response has ended and/or client has closed the connection ... ");
            }

            checkRequest(line);
        }
    }

    /**
     * checkRequest takes a line from the request, checks to ensure the line is the first line of the request, then
     * uses it, along with the OutputStream os that is connected to the client, to call the Request
     * class' simplerWriter method. Also checks if its a get request, and if it is it also calls the
     * GetRequest class' readInResource method. These methods are responsing for parsing and analyzing the
     * request, then formulating the response with assistance from helper methods.
     * @param line String containing a line from the request
     */
    private void checkRequest(String line) {
        System.out.println("ConnectionHandler: " + line);
        String[] requestLine = line.split(" ");

        if(requestLine.length == 3 && requestLine[2].startsWith("HTTP")) {
            if(requestLine[0].startsWith("GET")) {
                Request.simpleWriter(os,requestLine);
                GetRequest.readInResource(os);
            } else {
                Request.simpleWriter(os,requestLine);
            }
        }
    }

    /**
     * cleanUp invokes the close method on the BufferedReader, InputStream, and Socket, to ensure the connection is
     * safely and properly ended.
     * This method is identical to the one in the sample code.
     */
    public void cleanup(){
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
