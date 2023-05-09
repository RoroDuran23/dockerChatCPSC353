import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Client.java
 *
 * <p>This class creates a new Client object with their username and socket and score
 * as the parameters.
 */
public class Client {
  public Socket connectionSock = null;
  public String username = "";
  public Integer score = 0;

  Client(Socket sock,  String username, Integer score) { //, Integer score
    this.connectionSock = sock;
    this.username = username; 
    this.score = score;
  }

  //function to add score
  public void addScore(int score) {
    this.score += score;
  }
}
