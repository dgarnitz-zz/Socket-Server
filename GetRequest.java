import java.io.*;

/**
 * The GetRequest class is responsible for reading in the data from the specified resource and sending it to the client.
 * This class has one method that is responsible its functionality.
 */
class GetRequest {

    /**
     * The readInResource method works by creating a PrintStream using the ServerSocket’s OutputStream, creating a File
     * object using the directory path and the resource name, and creating a FileInputStream with the same directory
     * path and resource name. It then uses the File’s length method to calculate the size in bytes, creates a byte
     * array of that size, and uses the FileInputStream’s read method to read the file into the byte array. It then
     * calls the PrintStream’s write method to send those bytes back to the client
     * @param os OutputStream associated with the ServerSocket's Socket object
     */
    static void readInResource(OutputStream os) {
        String directory = WebServerMain.directory + WebServerMain.fileName;

        try {
            PrintStream ps = new PrintStream(os);
            File fileToSend = new File(directory);
            FileInputStream contentFile = new FileInputStream(directory);
            int length = (int)fileToSend.length();
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
