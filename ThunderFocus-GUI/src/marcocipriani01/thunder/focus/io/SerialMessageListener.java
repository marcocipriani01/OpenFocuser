package marcocipriani01.thunder.focus.io;

/**
 * Serial message listener interface.
 *
 * @author marcocipriani01
 * @version 1.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public interface SerialMessageListener {

    /**
     * Called when a new message is received from the serial port.
     *
     * @param msg the received message.
     */
    void onPortMessage(final String msg);

    /**
     * Called when an error occurred while communicating with the serial port.
     *
     * @param e the {@code Exception}.
     */
    void onPortError(Exception e);
}