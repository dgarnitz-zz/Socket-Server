import java.io.*;

/**
 * The GetRequest class is responsible for reading in the data from the specified resource and sending it to the client.
 * It accomplishes this through the use of sendResponse method it inherits from Request.
 */
class GetRequest extends Request {

    /**
     *
     * @param os OutputStream associated with the ServerSocket's Socket object
     */
    static void readInResource(OutputStream os) {
        String directory = WebServerMain.directory + WebServerMain.fileName;

        try {

            PrintStream ps = new PrintStream(os);
            File fileToSend = new File(directory);
            int length = (int)fileToSend.length();
            FileInputStream contentFile = new FileInputStream(directory);
            byte[] fileStoredAsBytes = new byte[length];
            contentFile.read(fileStoredAsBytes);
            ps.write(fileStoredAsBytes, 0, length);

        }  catch (FileNotFoundException e) {
            System.out.println("Directory not found : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error with I/O" + e.getMessage());
        }
    }
}
