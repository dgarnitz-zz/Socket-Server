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
            os.write(response);

            //Date
            Date d = new Date();
            String formatted = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(d);
            String date = "Date: " + d;
            System.out.println(date);
            byte[] dateStamp = date.getBytes();
            os.write(dateStamp);
        } catch (IOException ioe) {
            System.out.println("ConnectionHandler: failed response " + ioe.getMessage());
        }
    }
}
