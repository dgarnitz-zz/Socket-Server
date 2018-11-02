import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.Socket;

public class HeadRequest {


    public static void checkProtocol(String protocol){
        if(!protocol.equals("HTTP/1.1") && !protocol.equals("HTTP/1.0")){
            System.out.println("This server does not support this protocol");
            return;
        }
    }

    /*public static int responseCode(String resource){

    } */

    public static void simpleWriter(OutputStream os, String[] line) {

        //The data written below also need to be written into a file. Will need to create a separate class or method
        //for that
        try {
            checkProtocol(line[2]);

            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));


            String r = "HTTP/1.1 200 OK";
            byte[] response = r.getBytes();
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