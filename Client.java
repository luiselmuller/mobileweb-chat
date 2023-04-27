package com.mobileweb.sca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.lang.model.type.NullType;

public class Client 
{
    
    private Socket clientSocket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String name;

    public Client(String name, Socket client)
    {
        try
        {
            this.clientSocket = client;
            this.name = name;
            this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        }
        catch (IOException e)
        {
            closeClient(clientSocket, br, bw);
        }
    }

    public void sendMessage()
    {
        new Thread
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        bw.write(name);
                        bw.newLine();
                        bw.flush();

                        Scanner sc = new Scanner(System.in);
                        while(clientSocket.isConnected())
                        {
                            String clientMessage = sc.nextLine();
                            bw.write(name + ": " + clientMessage);
                            bw.newLine();
                            bw.flush();
                        }
                        sc.close();
                    }
                    catch(IOException e)
                    {
                        closeClient(clientSocket, br, bw);
                    }
                }
            }
        ).start();
    }

    public void readMessage() 
    {
        new Thread
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    String message;
                    while(clientSocket.isConnected())
                    {
                        try 
                        {
                            message = br.readLine();
                            System.out.println(message);
                        }
                        catch (IOException e)
                        {
                            closeClient(clientSocket, br, bw);
                        }
                    }
                }
            }  
        ).start();
    }

    public void closeClient(Socket client, BufferedReader br, BufferedWriter bw)
    {
        try
        {
            if(client != null)
            {
                this.clientSocket.close();
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

    public static void main(String[] args) throws IOException
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");

        String name = sc.nextLine();

        Socket client = new Socket("192.168.0.2", Integer.parseInt(args[0]));
        Client c = new Client(name, client);

        // Starting client threads
        c.readMessage();
        c.sendMessage();

    }   
}
