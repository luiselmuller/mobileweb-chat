/**
 * This file contains the ChatRoomServer class.
 * 
 * The ChatRoomServer class is a server that accepts client connections and spawns 
 * a new thread to handle each connected client. The main method of the class accepts
 * a port number as a command-line argument, creates a new server socket on that port,
 * and then enters a loop where it waits for incoming connections.
 * 
 * @author Luisel Muller | Ian Colon
 * @version 0.1
 * @since 04/18/2023
 */

package com.mobileweb.sca;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoomServer 
{
    static ServerSocket server;
    
    /**
     * This is the main method of the ChatRoomServer class. It accepts a port number as a command-line argument,
     * creates a new server socket on that port, and enters a loop where it waits for incoming connections. For each
     * new connection, it creates a new instance of the ClientHandler class to handle all communication with the
     * client, and spawns a new thread to run the ClientHandler instance in.
     * 
     * @param args the command-line arguments to the program. This should contain a single string representing the
     *             port number on which the server should listen for incoming connections.
     */
    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.out.println("Please provide port number");
            return;
        }

        int port = Integer.parseInt(args[0]);
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            System.out.println("Waiting for connections");

            while(!server.isClosed())
            {
                Socket client = server.accept();
                System.out.println("New client connected");

                ClientHandler handler = new ClientHandler(client);

                Thread clientThread = new Thread(handler);

                clientThread.start();
            }
        }
        catch (IOException e)
        {
            try
            {
                if(server != null)
                {
                    server.close();
                }
            }
            catch (IOException ex)
            {
                e.printStackTrace();
            }
        }
    }   

}
