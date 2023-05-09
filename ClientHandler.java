import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ClientHandler.java
 *
 * <p>This class handles communication between the client
 * and the server.  It runs in a separate thread but has a
 * link to a common list of sockets to handle broadcast. 
 * Shows the client-list when asked for it: Who?
 * Lets other users know when someone quits.
 * Removes user after she says QUIT
 * Allows host to create questions, choose answers and give points to clients
 *
 */
public class ClientHandler implements Runnable {
  private Client myClient = null;
  private ArrayList<Client> clientList;

  ClientHandler(Client myClient, ArrayList<Client> clientList) {
    this.myClient = myClient;
    this.clientList = clientList;  // Keep reference to master list
  }

  /**
   * received input from a client.
   * sends it to other clients.
   */
  public void run() {
    try {
      BufferedReader clientInput = new BufferedReader(
          new InputStreamReader(myClient.connectionSock.getInputStream()));
      DataOutputStream currClientOutput = 
          new DataOutputStream(myClient.connectionSock.getOutputStream());

      System.out.println("Connection made with user: " + myClient.username);

      // while loop for the host
      while (myClient == clientList.get(0)) {
        // Get data sent from a client
        String clientText = clientInput.readLine();
        if (clientText.equals("Q")) {
          // ask user what type of question
          DataOutputStream clientOutput = new 
                DataOutputStream(myClient.connectionSock.getOutputStream());

          clientOutput.writeBytes("Would you like to ask a multiple choice (1)");
          clientOutput.writeBytes(" or true/false (2) question?\n");
          int questionType = Integer.parseInt(clientInput.readLine());

          if (questionType == 1) { // multiple choice
            clientOutput.writeBytes("Copy & paste the question you would like to ask: \n");
            String mcQuestion = clientInput.readLine();
            clientOutput.writeBytes("Type in option 1: \n");
            String option1 = clientInput.readLine();
            clientOutput.writeBytes("Type in option 2: \n");
            String option2 = clientInput.readLine();
            clientOutput.writeBytes("Type in option 3: \n");
            String option3 = clientInput.readLine();
            clientOutput.writeBytes("Type in option 4: \n");
            String option4 = clientInput.readLine();
            clientOutput.writeBytes("Which option is the answer? (Type 1, 2, 3, or 4): \n");
            int mcAnswer = Integer.parseInt(clientInput.readLine());

            for (Client c :  clientList) {
              if (c.connectionSock != myClient.connectionSock) {
                DataOutputStream coutput = new 
                    DataOutputStream(c.connectionSock.getOutputStream());
                coutput.writeBytes("Question: " + mcQuestion + "\n");
                coutput.writeBytes("a. " + option1 + "\n");
                coutput.writeBytes("b. " + option2 + "\n");
                coutput.writeBytes("c. " + option3 + "\n");
                coutput.writeBytes("d. " + option4 + "\n");
                coutput.writeBytes("Answer: \n");
              }
            }
          } else {
            clientOutput.writeBytes("Copy & paste the question you would like to ask: \n");
            String tfQuestion = clientInput.readLine();
            clientOutput.writeBytes("Is the answer true (t) or false (f)? \n");
            String tfAnswer = clientInput.readLine();

            for (Client c :  clientList) {
              if (c.connectionSock != myClient.connectionSock) {
                DataOutputStream coutput = new 
                    DataOutputStream(c.connectionSock.getOutputStream());
                coutput.writeBytes("Question: " + tfQuestion + "\n");
                coutput.writeBytes("Answer: \n");
              }
            }
          }
        }

        if (clientText.equals("QUIT")) {
          clientList.remove(myClient);
          System.out.println(myClient.username + " has left the chat");
          for (Client c :  clientList) {
            if (c.connectionSock != myClient.connectionSock) {
              DataOutputStream clientOutput = new 
                  DataOutputStream(c.connectionSock.getOutputStream());
              clientOutput.writeBytes(myClient.username + " has left the chat\n");
            }
          }
          break;
        } else if (clientText.equals("Who?")) {
          DataOutputStream clientOutput = new 
                  DataOutputStream(myClient.connectionSock.getOutputStream());
          clientOutput.writeBytes("Client List: \n");
          for (Client c :  clientList) {
            clientOutput.writeBytes(c.username + "\n");
          }
        } else if (clientText.equals("SCORES")) {
          for (Client c :  clientList) {
            if (c.connectionSock != myClient.connectionSock) {
              DataOutputStream clientOutput = new 
                  DataOutputStream(c.connectionSock.getOutputStream());
              clientOutput.writeBytes("\nScores for each client: \n");
              for (Client cl :  clientList) {
                if (cl != clientList.get(0)) {
                  clientOutput.writeBytes(cl.username + ": " + cl.score + "\n");
                }
              }
            }
          }
        } else if (clientText.equals("ADDSCORE")) {
          DataOutputStream toClient =
                new DataOutputStream(myClient.connectionSock.getOutputStream());
          toClient.writeBytes("Enter the username of the user you want to add score to: \n");
          String username = clientInput.readLine();
          toClient.writeBytes("Enter the number of score you want to award: \n");
          int score = Integer.parseInt(clientInput.readLine());
          for (Client c : clientList) {
            if (c.connectionSock != myClient.connectionSock) {
              if (c.username.equals(username)) {
                c.addScore(score);
                // String pointsGiven = clientInput.readLine();
                System.out.println(username + " now has " + score + " point(s)!");
              }
            }
          } 
        } else if (clientText != null && !clientText.equals("QUIT") && !clientText.equals("Who?")) {
          // System.out.println(myClient.username + ": " + clientText);
          // // Turn around and output this data
          // // to all other clients except the one
          // // that sent us this information
          // for (Client c :  clientList) {
          //   if (c.connectionSock != myClient.connectionSock) {
          //     DataOutputStream clientOutput = new 
          //         DataOutputStream(c.connectionSock.getOutputStream());
          //     clientOutput.writeBytes(myClient.username + ": " + clientText + "\n");
          //   }
          // }
          
        } else {
          // Connection was lost
          System.out.println("Closing connection for socket " + myClient.username);
          // Remove from arraylist
          clientList.remove(myClient);
          myClient.connectionSock.close();
          break;
        }
      }

      // while loop for the players
      while (myClient != clientList.get(0)) {
        // Get data sent from a client
        String clientText = clientInput.readLine();

        if (clientText.equals("QUIT")) {
          clientList.remove(myClient);
          System.out.println(myClient.username + " has left the chat");
          for (Client c :  clientList) {
            if (c.connectionSock != myClient.connectionSock) {
              DataOutputStream clientOutput = new 
                  DataOutputStream(c.connectionSock.getOutputStream());
              clientOutput.writeBytes(myClient.username + " has left the chat\n");
            }
          }
          break;
        } else if (clientText.equals("Who?")) {
          DataOutputStream clientOutput = new 
                  DataOutputStream(myClient.connectionSock.getOutputStream());
          clientOutput.writeBytes("Client List: \n");
          for (Client c :  clientList) {
            clientOutput.writeBytes(c.username + "\n");
          }
        } else if (clientText != null && !clientText.equals("QUIT") && !clientText.equals("Who?")) {
          System.out.println(myClient.username + ": " + clientText);
          // Turn around and output this data
          // to all other clients except the one
          // that sent us this information
          for (Client c :  clientList) {
            if (c.connectionSock != myClient.connectionSock) {
              DataOutputStream clientOutput = new 
                  DataOutputStream(c.connectionSock.getOutputStream());
              clientOutput.writeBytes(myClient.username + ": " + clientText + "\n");
            }
          }
        } else {
          // Connection was lost
          System.out.println("Closing connection for socket " + myClient.username);
          // Remove from arraylist
          clientList.remove(myClient);
          myClient.connectionSock.close();
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      // Remove from arraylist
      clientList.remove(myClient);
    }
  }
} // ClientHandler for MtServer.java