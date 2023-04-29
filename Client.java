/**
 * This file contains the Client class that handles the client-side communication in the Chat Room application.
 * 
 * It includes methods for sending and receiving messages, as well as for closing the connection.
 * This class has the following attributes:
 *     - clientSocket: the Socket object used to connect to the server
 *     - br: a BufferedReader object used to read messages from the server
 *     - bw: a BufferedWriter object used to send messages to the server
 *     - name: a String that represents the client's username
 * This class has the following methods:
 *     - sendMessage(): a method that starts a new thread for sending messages to the server. It reads input from 
 *       the console and sends it to the server.
 *     - readMessage(): a method that starts a new thread for reading messages from the server. It listens for 
 *       messages and prints them to the console.
 *     - closeClient(): a method that closes the client's connection to the server, as well as the input and output streams.
 *     - main(): the entry point of the program. It prompts the user for their username and establishes a connection to the 
 *       server using the provided IP address and port number.
 * 
 * @author Luisel Muller | Ian Colon
 * @version 0.1
 * @since 04/18/2023
 */

package com.mobileweb.sca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{
    
    private Socket clientSocket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String name;

    /**
     * Creates a new Client object with the specified name and client socket.
     * Initializes the BufferedReader and BufferedWriter objects for sending and receiving messages.
     * 
     * @param name the client's username
     * @param client the client's socket object
     */
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

    /**
     * Starts a new thread for sending messages to the server.
     * Reads input from the console and sends it to the server.
     */
    public synchronized void sendMessage()
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
                        while(clientSocket.isConnected() && !clientSocket.isClosed())
                        {
                            String clientMessage = "";
                            if(sc.hasNextLine())
                            {
                                clientMessage = sc.nextLine();
                            }
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

    /**
     * Starts a new thread for reading messages from the server.
     * Listens for messages and prints them to the console.
     */
    public synchronized void readMessage() 
    {
        new Thread
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    String message = "";
                    while(clientSocket.isConnected())
                    {
                        if(message == null)
                        {
                            System.out.println("Connection closed");
                            closeClient(clientSocket, br, bw);
                            break;
                        }
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

    /**
     * This method is used to close the client's connection to the server, as well as the input and output streams.
     * 
     * @param client the client socket
     * @param br the client BufferedReader object
     * @param bw the client BufferedWriter object
     */
    public void closeClient(Socket client, BufferedReader br, BufferedWriter bw)
    {
        try
        {
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

    /**
     * The main method of the client class, it takes an argument in the command-line which is the port number.
     * 
     * @param args the server port number
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException
    {

        if(args.length != 1)
        {
            System.out.println("Please provide the port number");
            return;
        }

        Scanner sc = new Scanner(System.in);
        try
        {
            System.out.println("Enter your username: ");

            String name = "";

            name = sc.nextLine();
                  
            Socket client = new Socket("192.168.0.2", Integer.parseInt(args[0]));
            Client c = new Client(name, client);

            // Starting client threads
            c.readMessage();
            c.sendMessage();
        }
        catch(IOException e)
        {
            sc.close();
        }

    }   
}
