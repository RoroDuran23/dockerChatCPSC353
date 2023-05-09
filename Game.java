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
 * Game.java
 *
 * <p>This program 
 */

public class Game {
  private Client myClient = null;
  private ArrayList<Client> clientList;

  Game(Client myClient, ArrayList<Client> clientList) {
    this.myClient = myClient;
    this.clientList = clientList;  // Keep reference to master list
  }

  public void runGame(){
    try {
      ServerSocket serverSock = new ServerSocket(9023);
    
      Client host = clientList.get(0);
      
      Socket connectionSock = serverSock.accept();
      BufferedReader cInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
      DataOutputStream cOutput = new DataOutputStream(connectionSock.getOutputStream());
      PrintStream ps = new PrintStream(connectionSock.getOutputStream());

      if(myClient == host){
        // ask them to enter which question number from txt file they want to ask
        ps.println("Enter question number from 'Questions.txt' that you would like to ask.");
        int questionType = Integer.parseInt(cInput.readLine());
        if(questionType == 1) {
          // System.out.println("Question: How many reindeers pull Santa's sleigh?");
          for(Client c : clientList){
            if (c.connectionSock != myClient.connectionSock) {
              DataOutputStream clientOutput = new 
                  DataOutputStream(c.connectionSock.getOutputStream());
              clientOutput.writeBytes("Question: How many reindeers pull Santa's sleigh?");
            }
          }
        }

        // depending on question choosen, set answer and send out question to clients
      } else {
        // question gets sent to clients
        // clients can type in an answer
        // if answered correcly, they get awarded points
        // other clients will be notified who got the correct answer
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}