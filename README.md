# dockerchat

* This repo contains programs to implement a multi-threaded TCP chat server and client game.
  - [x] Prompt the client to type a username
  - [x] Notify the client when their username is accepted
  - [x] Display a list of possible commands to the client when their username is accepted
  - [x] The first client to submit the username "host" will become the host of the game.  This should be the first client that connects to the server. Tell the host how to ask questions and assign points, and how to use the SCORES command
  - [x] The host will send a multiple-choice or true/false question to the other clients.


* MtClient.java handles keyboard input from the user.
* ClientListener.java receives responses from the server and displays them
* MtServer.java listens for client connections and creates a ClientHandler for each new client; looks-out for repeated usernames
* ClientHandler.java receives messages from a client and relays it to the other clients; handles "Who?", "QUIT" and other functions
* Client.java creates a new client


## Identifying Information

* Name: Emily Nguyen, Rodrigo Duran, Samith Lakka
* Student ID: 2378382, 2348338, 2352425
* Email: eminguyen@chapman.edu, Rduran@chapman.edu, lakka@chapman.edu
* Course: CPSC-353-02
* Assignment: PA04 - Chat Submission 3

## Source Files

* Client.java
* MtClient.java
* MtServer.java
* ClientListener.java
* ClientHandler.java
* Docker.md
* README.md

## References

* Java Oracle
* W3 Schools
* Stack Overflow
* https://www.geeksforgeeks.org/establishing-the-two-way-communication-between-server-and-client-in-java/

## Contributions

* Emily: Created questions and answers; made the first client a host; prompt the host for   questions and answers; added score param to Client; SCORES command
* Rodrigo: Created questions and answers; did checkstyle; README; glued parts together in the ClientHandler; added game instructions; did documentation; fixed usernames repetition
* Samith: addScore() fucntion in the Client.java file; ADDSCORE function in the ClientHandler

## Known Errors

* None

## Build Insructions

* javac *.java

## Execution Instructions

* java MtServer
* java MtClient

## Game Instructions

* Run one (1) MtServer and two (2) MtClient's on separate terminals
* The first MtClient to connect with the server will be the host. She should type 'Q' to type her first question.
* Then, she will choose if she wants to do a MC or T/F question.
* After that, she will create the question, answers and correct answer.
  * (She should copy and paste the questions and answers from the Questions.txt file!)
* The clients apart from the host will see the question and answer accordingly.
* After the host sees the first correct answer, she shall type 'ADDSCORE' and award points to the respective winner.
