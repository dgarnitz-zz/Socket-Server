import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.Socket;

public class HeadRequest {

    public static void simpleWriter(OutputStream os, String line) {
        byte[] response = line.getBytes();

        //The data written below also need to be written into a file. Will need to create a separate class or method
        //for that
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
            pw.println(response);
            //os.write(response);

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
            pw.println(dateStamp);
            String length = "9000";
            byte[] contentLength = length.getBytes();
            pw.println(contentLength);
        } catch (Exception e) {
            System.out.println("ConnectionHandler: failed response " + e.getMessage());
        }
    }

}


// \r\n is needed to end the header section of the response in the last line then another