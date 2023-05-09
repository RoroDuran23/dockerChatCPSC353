import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * MTClient.java
 * 
 * <p>This program implements a simple multithreaded chat client.  It connects to the
 * server (assumed to be localhost on port 9023), starts a thread:
 * listens for data sent from the server and waits
 * for the user to type something in that will be sent to the server.
 * Anything sent to the server is broadcast to all clients.
 * 
 * If user types QUIT it ends the program.
 * 
 * <p>The MTClient uses a ClientListener whose code is in a separate file.
 * The ClientListener runs in a separate thread, recieves messages form the server,
 * and displays them on the screen.
 *  
 * <p>Data received is sent to the output screen, so it is possible that as
 * a user is typing in information a message from the server will be
 * inserted.
 */
public class MtClient {
  /**
   * main method.
   *
   * @params not used.
   */
  public static void main(String[] args) {
    try {
      String hostname = "localhost";
      int port = 9023;

      System.out.println("Connecting to server on port " + port);
      Socket connectionSock = new Socket(hostname, port);

      DataOutputStream serverOutput = new DataOutputStream(connectionSock.getOutputStream());
      BufferedReader sInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));

      System.out.println("Connection made.");

      // Start a thread to listen and display data sent by the server
      ClientListener listener = new ClientListener(connectionSock);
      Thread theThread = new Thread(listener);
      theThread.start();

      // Read input from the keyboard and send it to everyone else.
      
      Scanner keyboard = new Scanner(System.in);

      System.out.print("Enter username: ");
      String username = keyboard.nextLine();
      if (username != null){
        serverOutput.writeBytes(username + "\n");
      }
      
      while (true) {
        String data = keyboard.nextLine();
        serverOutput.writeBytes(data + "\n");
        if (data.equals("QUIT")){
          System.out.println("Goodbye");
          System.exit(0);
          break;
        }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
} // MtClient


//https://www.scaler.com/topics/java/exit-in-java/
