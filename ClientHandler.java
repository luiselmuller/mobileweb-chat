package com.mobileweb.sca;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class ClientHandler implements Runnable
{
    private Socket client;
    private BufferedReader br;
    private BufferedWriter bw;
    static String name;
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    
    Scanner scn = new Scanner(System.in);

    public ClientHandler(Socket client)
    {
        try
        {
            boolean userExists = false;

            this.client = client;

            this.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));

            this.name = br.readLine();
                
            clients.add(this);

            System.out.println("PROTOCOL CODE: " + ProtocolCodes.JOIN + " - USER_JOINED");
            broadcast(name + " has joined the chat!");
        }
        catch (IOException e)
        {
            closeHandler(client, br, bw);
        }
    }

    @Override
    public void run()
    {
        String clientMessage;
        while(client.isConnected())
        {
            try
            {
                clientMessage = br.readLine();
                if(clientMessage.split("/")[1].equals("quit"))
                {
                    closeHandler(client, br, bw);
                }
                else if(clientMessage.split("/")[1].equals("list"))
                {
                    bw.write("Users in the room:");
                    bw.newLine();
                    bw.flush();
                    for(ClientHandler c : clients)
                    { 
                        bw.write("[" + c.name + "]");
                        bw.newLine();
                        bw.flush();
                    }
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

    public void handleCommands(String command)
    {

    }

    public void broadcast(String msg)
    {
        for(ClientHandler c : clients)
        {
            if(c.name.equals(name))
            {
                try
                {
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.MESSAGE + " Brodcast message from " + c.name + ": " + msg + " - BROADCASTING");
                    c.bw.write(msg);
                    c.bw.newLine();
                    c.bw.flush();
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.MESSAGE_DELIVERED + " Brodcast message from " + c.name + ": " + msg + " - DELIVERED");
                }
                catch (IOException e)
                {
                    System.out.println("PROTOCOL CODE: " + ProtocolCodes.MESSAGE_NOT_DELIVERED + " Brodcast message from " + c.name + ": " + msg + " - FAILED");
                    closeHandler(client, br, bw);
                }
            }
        }
    }

    public void closeHandler(Socket client, BufferedReader br, BufferedWriter bw)
    {
        clients.remove(this);
        broadcast(name + " has left the chat!");
        try
        {
            if(client != null)
            {
                this.client.close();
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
