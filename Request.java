import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.net.Socket;


class Request {


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

    private static String responseCode(Path path) {
        if(Files.exists(path)) {
            return " 200 OK";
        } else {
            return " 404 Not Found";
        }
    }

    private static boolean isHeadOrGetRequest(String requestMethod) {
       return (requestMethod.equals("HEAD") || requestMethod.equals("GET"));
    }

    private static String getDate() {
        Date d = new Date();
        String formatted = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(d);
        return formatted;
    }
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

    private static String contentType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException ioe) {
            System.out.println("Error reading content type: " + ioe.getMessage());
            return "Unknown";
        }
    }

    static void convertToBytesAndSend(ArrayList<String> responses, PrintWriter pw){
        for(String r : responses){
            pw.println(r);
            pw.flush();
            System.out.println(r);



            //Use for binary data
            /* byte[] response = r.getBytes();
            pw.println(response);
            pw.flush(); */
        }
    }

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

    static void simpleWriter(OutputStream os, String[] line) {


        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));

            String protocol = checkProtocol(line[2]);

            Path path = Paths.get("/", WebServerMain.directory, line[1]);

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

            convertToBytesAndSend(responses, pw);
            logRequests(responses, line);
        } catch (Exception e) {
            System.out.println("ConnectionHandler: failed response " + e.getMessage());
        }
    }
}