# Chat Application

This is a simple chat application that allows clients to communicate with each other over a network. The server and client use sockets to communicate and the application is written in Java.

## Features

- Clients can send and receive messages to and from other clients connected to the server.
- The application uses multithreading to allow clients to send and receive messages simultaneously.
- The client can specify a username that is used to identify them in the chat.
- Clients can list the users currently in the chatroom.

## Requirements

- Java 8 or later installed on your system.

## Usage

To run the chat application, follow these steps:

1. Clone the repository to your local machine in a folder called com/mobileweb/sca.
2. Open a terminal or command prompt and navigate to the root directory of the project.
3. Compile the code using the following command:
```javac com/mobileweb/sca/*.java```
4. Start the server by running the following command:
```java com.mobileweb.sca.Server [port]```
where `[port]` is the port number for the server.
5. Start a client by running the following command:
```java com.mobileweb.sca.Client [port]```
where `[port]` is the port number the server is listening on.
6. Enter a username for the client when prompted.
7. The client can now send and receive messages to and from other clients connected to the server.

## Credits

- Luisel Muller
- Ian Colon
