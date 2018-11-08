import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;


/**
 * The Request class is responsible for analyzing a client request, and formulating and the sending the server's
 * response headers. It is heavily deconstructed into many different methods that are each responsible for one piece
 * of the class' overall functionality. All these different methods are used by the simpleWriter class, which is called
 * by the ConnectionHandler object.
 */
class Request {
    static Path path;

    /**
     * The checkProtocol method takes a string parsed from the HTTP response containg the protocol
     * and checks to make sure that this server supports it.
     * @param protocol A string containing the protocol piece of the HTTP request
     * @return A string containing the protocol portion of the status line in the HTTP response
     */
    private static String checkProtocol(String protocol){
        switch (protocol) {
            case "HTTP/1.1":
                return "HTTP/1.1";
            case "HTTP/1.0":
                return "HTTP/1.0";
            default:
                System.out.println("This server does not support this protocol");
                return "Error";
        }
    }

    /**
     * The responseCode function checks to make sure the resource requested is available by using Java's Path object
     * constructed from the resource piece of the HTTP request and the directory argument passed to WebServerMain
     * @param path A Path object containing the path to the requested resource
     * @return A string containing the response code portion of the status line in the HTTP response
     */
    private static String responseCode(Path path) {
        if(Files.exists(path)) {
            return " 200 OK";
        } else {
            return " 404 Not Found";
        }
    }

    /**
     * The isHeadOrGetRequest method checks that the request method is one of the two supported by the server.
     * @param requestMethod A string parsed from the HTTP request containing the request method
     * @return a Boolean used to decide how to formulate the HTTP response
     */
    private static boolean isHeadOrGetRequest(String requestMethod) {
       return (requestMethod.equals("HEAD") || requestMethod.equals("GET"));
    }

    /**
     * The getDate method uses Java's Date object to grab the date at the time the function processes, representing the
     * time the HTTP response is formulated, then it uses the SimpleDateFormat object's format method to format it.
     * @return A string containing the current date and time
     */
    private static String getDate() {
        Date d = new Date();
        String formatted = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(d);
        return formatted;
    }

    /**
     * The contentLength method uses a Path object to get the size of a resource in bytes. It then returns a string
     * containing that size, which will be used as part of the HTTP response
     * @param path A Path object containing the path to the requested resource
     * @return A string containing the content length in bytes
     */
    private static String contentLength(Path path) {
        try{
            System.out.println("Checking: " + path);
            long size = Files.size(path);
            return "Content-Length: " + size;
        } catch (IOException ioe) {
            System.out.println("Error reading content length: " + ioe.getMessage());
            return "Content length: Unknown";
        }
    }

    /**
     * The contentLength method uses a Path object to get the type of a resource. It then returns a string
     * containing that type, which will be used as part of the HTTP response
     * @param path A Path object containing the path to the requested resource
     * @return A string containing the content type
     */
    static String contentType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException ioe) {
            System.out.println("Error reading content type: " + ioe.getMessage());
            return "Unknown";
        }
    }

    /**
     * The sendResponse method takes a String ArrayList containing each line of the HTTP response, and a PrintWriter
     * that will send those response lines, and it iterates through the ArrayList, sending each response using the
     * PrinterWriter's println and flush methods.
     * @param responses ArrayList containing the HTTP response lines
     * @param pw PrintWriter used to send HTTP response
     */
    static void sendResponse(ArrayList<String> responses, PrintWriter pw){
        for(String r : responses){
            pw.println(r);
            pw.flush();
            System.out.println(r);
        }
    }

    /**
     * The logRequests method takes the HTTP response headers and logs them to a locally stored file using a
     * PrintWriter println and flush methods
     * @param responses A String ArrayList containing the HTTP responses
     * @param line A String Array containing the first line of the HTTP request
     */
    private static void logRequests(ArrayList<String> responses, String[] line) {
        try {
            String fileName = "Web Server Request Log File.txt";
            String firstLine = "Client Request Listed Below:";
            String secondLine = line[0] + " " + line[1] + " " + line[2];
            String thirdLine = "Server Response Listed Below:";

            PrintWriter logFile = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true))));
            logFile.println(firstLine);
            logFile.println(secondLine);
            logFile.println(thirdLine);
            logFile.flush();

            for(String r : responses) {
                logFile.println(r);
                logFile.flush();
            }

        } catch (IOException ioe) {
            System.out.print("Error logging requests in a file: " + ioe.getMessage());
        }
    }

    /**
     * The principal method of the Request class, simpleWriter takes a string Array containing a line from the HTTP
     * request and the OutputStream associated with the Socket being used for the connection, and calls the above helper
     * methods to analyze the request and formulate responses, which are stored in a string ArrayList. It then sends
     * the response back to the client and logs it in a locally stored file. It should be noted that this method only
     * creates a response header, and that any content requested will actually be send by the GetRequest class.
     * @param os
     * @param line
     */
    static void simpleWriter(OutputStream os, String[] line) {


        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));

            String protocol = checkProtocol(line[2]);

            path = Paths.get("/", WebServerMain.directory, line[1]);

            WebServerMain.fileName = line[1];

            String responseCode = responseCode(path);
            String ServerResponse;
            if(protocol.equals("Error")){
                ServerResponse = "HTTP/1.1 400 Bad Request";
            } else if (!isHeadOrGetRequest(line[0])) {
                ServerResponse = "HTTP/1.1 501 Not Implemented";
            } else {
                ServerResponse = protocol + responseCode;
            }

            ArrayList<String> responses = new ArrayList<String>();
            responses.add(ServerResponse);


            String date = "Date: " + getDate();
            responses.add(date);

            String serverName = "Server: David Garnitz Server";
            responses.add(serverName);

            if(isHeadOrGetRequest(line[0])){
                String contentType = "Content-Type: " + contentType(path);
                responses.add(contentType);

                String length = contentLength(path)  + "\r\n";
                responses.add(length);
            }

            sendResponse(responses, pw);
            logRequests(responses, line);
        } catch (Exception e) {
            System.out.println("ConnectionHandler: failed response " + e.getMessage());
        }
    }
}