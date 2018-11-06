import java.io.*;
import java.util.ArrayList;

/**
 * The GetRequest class is responsible for reading in the data from the specified resource and sending it to the client.
 * It accomplishes this through the use of convertToBytesAndSend method it inherits from Request.
 */
class GetRequest extends Request {

    /**
     * readInResources is the only method of the GetRequest class. It uses a BufferedReader to read in the content
     * line by line, adding each line to an ArrayList. After all the lines have been read in, it then creates
     * a PrintWriter using the OutputStream "os" it is passed as an argument. It then passes that using PrintWriter
     * and the ArrayList "content".
     * @param os OutputStream associated with the ServerSocket's Socket object
     */
    static void readInResource(OutputStream os) {
        String directory = WebServerMain.directory + WebServerMain.fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(directory))) {
            ArrayList<String> content = new ArrayList<>();

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
        } catch (IOException e) {
            System.out.println("Error with I/O" + e.getMessage());
        }
    }

}
