/**
 * [Short description of the purpose of this file]
 * 
 * [Detailed description of the contents of this file, including what classes, methods, and variables it contains]
 * 
 * @author Luisel Muller
 * @version 0.1
 * @since 04/14/2023
 */
package com.mobileweb.sca;

/**
 * 
 */
public class ProtocolCodes {
    // Used when a client joins the chatroom and sends their username to the server
    public static final int JOIN = 1;
    // Used when a client leaves the chatroom
    public static final int LEAVE = 2;
    // Used when a client sends a message to the chatroom
    public static final int MESSAGE = 3;
    // Used when a client sends a private message to another client
    public static final int WHISPER = 4;
    // Used when a client tries to list the clients in the chatroom
    public static final int LIST = 5;
    
    // Used when a client sends a message to a user that does not exist in the chatroom
    public static final int USER_NOT_IN_ROOM = 22;
    // Used when a client tries to use a username that is already in use
    public static final int USER_ALREADY_EXISTS = 23;
    // Used when a client tries to use a command that does not exist
    public static final int COMMAND_NOT_FOUND = 24;
    // Used when a client tries to join a chatroom that is full
    public static final int ROOM_FULL = 25;

    // Used when a server broadcasts a message
    public static final int MESSAGE_DELIVERED = 102;
    // Used when the server can't broadcast the message
    public static final int MESSAGE_NOT_DELIVERED = 103;

}
