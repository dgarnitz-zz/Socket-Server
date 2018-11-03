import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GetRequest extends Request {

    public static void readInResource(OutputStream os) {
        String directory = WebServerMain.directory + WebServerMain.fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(directory))) {
            ArrayList<String> content = new ArrayList<String>();

            while(true) {
                String line = br.readLine();

                if(line == null || line.equals("null") || line.equals("exit") ){
                    break;
                }

                content.add(line);
            }

            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
            convertToBytesAndSend(content, pw);

        }  catch (FileNotFoundException e) {
            System.out.println("Directory not found : " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Error with I/O" + e.getMessage());
            return;
        }
    }

}
