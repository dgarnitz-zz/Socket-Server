import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.net.Socket;


public class HeadRequest {


    public static String checkProtocol(String protocol){
        if(protocol.equals("HTTP/1.1")){
            return "HTTP/1.1";
        } else if (protocol.equals("HTTP/1.0")){
            return "HTTP/1.0";
        } else {
            System.out.println("This server does not support this protocol");
            return "Error";
        }
    }

    public static String responseCode(Path path) {
        if(Files.exists(path)) {
            return " 202 OK";
        } else {
            return " 404 Not found";
        }
    }

    public static String contentLength(Path path) {
        try{
            long size = Files.size(path);
            return "Content-Length: " + size;
        } catch (IOException ioe) {
            System.out.println("Error reading content length: " + ioe.getMessage());
            return "Content length: Unknown";
        }

        /*
        BasicFileAttributes attribute = Files.readAttributes(path, BasicFileAttributes.class);
        String size = (String)attribute.size();
        https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html
        https://docs.oracle.com/javase/8/docs/api/java/nio/file/attribute/BasicFileAttributes.html
        */
    }

    public static void convertToBytesAndSend(ArrayList<String> responses, PrintWriter pw){
        for(String r : responses){
            pw.println(r);
            pw.flush();
            System.out.println(r);

            /*
            //Use for binary data
            byte[] response = r.getBytes();
            pw.println(response); */
        }
    }


    public static void simpleWriter(OutputStream os, String[] line) {

        //The data written below also need to be written into a file. Will need to create a separate class or method
        //for that
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));

            String protocol = checkProtocol(line[2]);

            Path path = Paths.get(".", WebServerMain.directory, line[1]);

            WebServerMain.fileName = line[1];

            String responseCode = responseCode(path);
            String ServerResponse;
            if(protocol.equals("Error")){
                ServerResponse = "HTTP/1.1 400 Bad Request";
            } else {
                ServerResponse = protocol + responseCode;
            }

            ArrayList<String> responses = new ArrayList<String>();
            responses.add(ServerResponse);

            //Date
            Date d = new Date();
            String formatted = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(d);
            String date = "Date: " + formatted;
            responses.add(date);

            //NEED conditional handling for this, because it wont be sent if theres a server error <- I THINK, not sure
            String contentType = "Content-Type: text/html; charset=UTF-8";
            responses.add(contentType);

            //Server Name
            String serverName = "Server: David Garnitz Server";
            responses.add(serverName);

            //NEED a method to get the content length --> use built in methods
            String length = contentLength(path)  + "\r\n";
            responses.add(length);

            /*
            //End header section <-- NOT SURE if this is needed for a header or only other responses
            String endHeader = "\r\n";
            responses.add(endHeader);
            */

            String htmlTest = "<head>David Garnitz Website</head>\n<body>\n<h1>HELLO</h1>\n</body>";
            responses.add(htmlTest);

            convertToBytesAndSend(responses, pw);

        } catch (Exception e) {
            System.out.println("ConnectionHandler: failed response " + e.getMessage());
        }
    }

}


// \r\n is needed to end the header section of the response in the last line then another