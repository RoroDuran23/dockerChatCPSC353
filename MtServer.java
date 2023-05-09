import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * MTServer.java
 *
 * <p>This program implements a simple multithreaded chat server.  Every client that
 * connects to the server can broadcast data to all other clients.
 * The server stores an ArrayList of usernames to perform the broadcast.
 *
 * <p>The MTServer uses a ClientHandler whose code is in a separate file.
 * When a client connects, the MTServer starts a ClientHandler in a separate thread
 * to receive messages from the client.
 *
 * <p>Sets the first client as the host of the game, prompts host and players with useful commands
 *
 * <p> Looks out for repeated usernames
 */

public class MtServer {
  // Maintain list of all client sockets for broadcast
  private ArrayList<Client> clientList;

  public MtServer() {
    clientList = new ArrayList<Client>();
  }

  private void getConnection() {
    // Wait for a connection from the client
    try {
      System.out.println("Waiting for client connections on port 9023.");
      ServerSocket serverSock = new ServerSocket(9023);

      while (true) {
        Socket connectionSock = serverSock.accept();
        BufferedReader cInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
        DataOutputStream cOutput = new DataOutputStream(connectionSock.getOutputStream());
        PrintStream ps = new PrintStream(connectionSock.getOutputStream());

        String username = cInput.readLine();

        if(clientList.size() == 0 && username != null) { //starting the list
          System.out.println(username + " has joined the chat");
          ps.println("\nWelcome, you are the host.");
          ps.println("\tYou will ask questions to the clients."); 
          ps.println("\tFor each round, you will ask a question and assign a number of points.");
          ps.println("\tThe first client that answers correctly will be awarded points.");
          ps.println("\n\t\tCommands:\n\t'Q' to ask a question\n\t'SCORES' to display current " 
          + "standings to all clients\n\t'Who?' to get list of clients\n\t'ADDSCORE' to add scores\n\t'QUIT' to quit");
          Client host = new Client(connectionSock, username, 0);
          // Add this socket to the list
          clientList.add(host);

          // Send to ClientHandler the socket and arraylist of all sockets
          ClientHandler handler = new ClientHandler(host, this.clientList);
          Thread theThread = new Thread(handler);
          theThread.start();

        } else {
            // Checks if username is already taken
            for (Client cl : clientList){
              while (username.equals(cl.username)) {
                ps.println("Username taken, try again"); 
                System.out.println("HIIIII " + cl.username);
                username = cInput.readLine();
              }
            }

            if (username != null) {
              ps.println("Welcome, username accepted");
              ps.println("\nGame Rules:");
              ps.println("\tYour host for this game is: " + clientList.get(0).username);
              ps.println("\tWhen the host asks a question, type in your answer");
              ps.println("\tThe first client to answer correctly will receive points");
              ps.println("\tThe first client to reach 5 points wins");
              ps.println("Below are the list of commands you can use:\n\t'QUIT': to quit\n\t'Who?': look at list of usernames");
              System.out.println(username + " has joined the chat");
              Client newClient = new Client(connectionSock, username, 0);
              // Add this socket to the list
              clientList.add(newClient);
              // Send to ClientHandler the socket and arraylist of all sockets
              ClientHandler handler = new ClientHandler(newClient, this.clientList);
              Thread theThread = new Thread(handler);
              theThread.start();
              // break;
            }
          }
        }
        // serverSock.close();
      // Will never get here, but if the above loop is given
      // an exit condition then we'll go ahead and close the socket
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    MtServer server = new MtServer();
    server.getConnection();
  }
} // MtServer

//https://www.javatpoint.com/java-serversocket-close-method
