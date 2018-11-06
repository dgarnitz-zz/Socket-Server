/**
 *Class used to created an exception that is thrown when the Server cannot read in the request, thus disrupting the
 * connection.
 * This class is identical to the one in the example code.
 */
public class DisconnectedException extends Exception {
    public DisconnectedException(String message) {
        super(message);
    }
}
