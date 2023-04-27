package com.mobileweb.sca;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ChatRoomServer 
{
    static ServerSocket server;
    
    public static void main(String[] args)
    {
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
