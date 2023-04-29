/**
 * This file contains the Client class that handles the client-side communication in the Chat Room application.
 * 
 * It includes methods for sending and receiving messages, as well as for closing the connection.
 * This class has the following attributes:
 *     - client: a Socket object that represents the client's connection to the server.
 *     - br: a BufferedReader object that reads messages from the server.
 *     - bw: a BufferedWriter object that sends messages to the server.
 *     - scn: a Scanner object that reads input from the console.
 *     - name: a String object that represents the client's username.
 *     - clients: an ArrayList object that contains all of the ClientHandler objects that are connected to the server.
 * This class has the following methods:
 *     - run(): a method that runs the ClientHandler class.
 *     - broadcast(): a method that broadcasts a message to all of the clients.
 *     - closeHandler(): a method that closes the connection to the server.
 * 
 * @author Luisel Muller | Ian Colon
 * @version 0.1
 * @since 04/18/2023
 */

package com.mobileweb.sca;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class ClientHandler implements Runnable
{
    private Socket client;
    private BufferedReader br;
    private BufferedWriter bw;
    private String name;
    static final ArrayList<ClientHandler> clients = new ArrayList<>();
    
    Scanner scn = new Scanner(System.in);

    /**
     * Client handler constructor initialized the client socket, buffered reader, and buffered writer. It also sets the clients username
     * and adds them to the client array.
     * 
     * @param client a Socket object that represents the client's connection to the server.
     */
    public ClientHandler(Socket client)
    {
        try
        {
            this.client = client;

            this.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));

            name = br.readLine();
                
            clients.add(this);

            System.out.println(this.name);

            System.out.println("PROTOCOL CODE: " + ProtocolCodes.JOIN + " - USER_JOINED");
            broadcast(name + " has joined the chat!");
        }
        catch (IOException e)
        {
            closeHandler(client, br, bw);
        }
    }

    /**
     * This method runs the ClientHandler class, while the client is connected it will listen for messages from the client,
     * when the client types in a command with "/" it will handle it accordingly, or it will broadcast the message if it is
     * not a command.
     */
    @Override
    public void run()
    {
        String clientMessage;
        while(client.isConnected())
        {
            try
            {
                clientMessage = br.readLine();
                if(clientMessage.split(": ")[1].equals("/quit"))
                {
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.QUIT + " - QUIT_REQUEST");
                    throw new IOException();
                }
                else if(clientMessage.split(": ")[1].equals("/list"))
                {
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.LIST + " - LIST_REQUEST");
                    bw.write(">> User list:\n");
                    for(ClientHandler c : clients)
                    {
                        
                        bw.write("\t" + c.name + "\n");
                    }
                    bw.flush();
                }
                else
                {
                    broadcast(clientMessage);
                }
            }
            catch (IOException e)
            {
                closeHandler(client, br, bw);
                break;
            }
        }
    }
        

    /**
     * This method is used to broadcast messages to all clients.
     * 
     * @param msg a message sent by a client
     */
    public void broadcast(String msg)
    {
        for(ClientHandler c : clients)
        {
            if(!c.name.equals(name))
            {
                try
                {
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.MESSAGE + " Brodcast message from " + msg + " - MESSAGE_REQUEST");
                    c.bw.write(msg);
                    c.bw.newLine();
                    c.bw.flush();
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.MESSAGE_DELIVERED + " Brodcast message from " + msg + " - DELIVERED");
                }
                catch (IOException e)
                {
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.MESSAGE_NOT_DELIVERED + " Brodcast message from " + msg + " - FAILED");
                    closeHandler(client, br, bw);
                    break;
                }
            }
        }
    }

    /**
     * This method closes the connection to the server.
     * 
     * @param client object representing the clients socket connection
     * @param br object for the clients buffered reader
     * @param bw object for the clients buffered writer
     */
    public void closeHandler(Socket client, BufferedReader br, BufferedWriter bw)
    {
        try
        {
            clients.remove(this);
            broadcast(this.name + " has left the chat!");
            if(client != null)
            {
                client.close();
            }
            if(bw != null)
            {
                bw.close();
            }
            if(br != null)
            {
                br.close();
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

}
