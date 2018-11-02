import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        /*BasicFileAttributes attribute = Files.readAttributes(path, BasicFileAttributes.class);
        System.out.println(attribute.size());
        https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html
        https://docs.oracle.com/javase/8/docs/api/java/nio/file/attribute/BasicFileAttributes.html
        */
    }


    public static void simpleWriter(OutputStream os, String[] line) {

        //The data written below also need to be written into a file. Will need to create a separate class or method
        //for that
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));

            String protocol = checkProtocol(line[2]);
            if(protocol.equals("Error")){
                return;
            }

            Path path = Paths.get(".", WebServerMain.directory, line[1]);
            String responseCode = responseCode(path);
            WebServerMain.fileName = line[1];
            String ServerResponse = protocol + responseCode;
            System.out.println(ServerResponse);

            byte[] response = ServerResponse.getBytes();
            pw.println(response);



            //Date
            Date d = new Date();
            String formatted = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(d);
            String date = "Date: " + formatted;
            System.out.println(date);
            byte[] dateStamp = date.getBytes();
            pw.println(dateStamp);

            String contentType = "text/html";
            byte[] type = contentType.getBytes();
            pw.println(type);

            //Server Name
            String serverName = "David's Server";
            byte[] serverN = serverName.getBytes();
            pw.println(serverN);

            //NEED a method to get the content length --> use built in methods
            String length = "9000\r\n";
            byte[] contentLength = length.getBytes();
            pw.println(contentLength);

            //End header section
            String endHeader = "\r\n";
            byte[] end = endHeader.getBytes();
            pw.println(end);

        } catch (Exception e) {
            System.out.println("ConnectionHandler: failed response " + e.getMessage());
        }
    }

}


// \r\n is needed to end the header section of the response in the last line then another