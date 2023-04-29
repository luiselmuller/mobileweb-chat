/**
 * Class that holds codes that notify the server of events that happen in the chatroom.
 * 
 * @author Luisel Muller | Ian Colon
 * @version 0.1
 * @since 04/18/2023
 */
package com.mobileweb.sca;

/**
 * List of codes used by the server as notifications for events that happen
 * Codes in the single digits are client actions while the ones in the hundreds are server actions.
 */
public class ProtocolCodes {
    // Used when a client joins the chatroom and sends their username to the server
    public static final int JOIN = 1;
    // Used when a client leaves the chatroom
    public static final int LEAVE = 2;
    // Used when a client sends a message to the chatroom
    public static final int MESSAGE = 3;
    // Used when a client sends a private message to another client
    public static final int QUIT = 4;
    // Used when a client tries to list the clients in the chatroom
    public static final int LIST = 5;

    // Used when a server broadcasts a message
    public static final int MESSAGE_DELIVERED = 102;
    // Used when the server can't broadcast the message
    public static final int MESSAGE_NOT_DELIVERED = 103;

}
