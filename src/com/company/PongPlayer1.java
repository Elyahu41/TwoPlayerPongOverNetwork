package com.company;
// Server portion of a client/server stream-socket connection. 

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
// Elyahu Jacobi
public class PongPlayer1 extends PongParent
{
   private ServerSocket server; // server socket
   private Socket connection; // connection to client
   private int counter = 1; // counter of number of connections

   public PongPlayer1()
   {
      super( "Player 1 (Server)" );
   }

   // set up and run server 
   public void runServer()
   {
      try // set up server to receive connections; process connections
      {
         server = new ServerSocket( 12345, 100 );

         while ( true )  // infinite loop
            {
               try {
                  waitForConnection(); // wait for a connection from Client
                  super.getStreams(connection); // get input & output streams
                  super.processConnection(); // process connection
               }
            catch ( EOFException eofException ) 
            {
               displayMessage( "\nServer terminated connection" );
            }
            finally
            {
               closeConnection(connection, "\nTerminating connection\n"); //  close connection
               ++counter;
            }
         }
      }
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      }
   }

   // wait for connection to arrive, then display connection info
   private void waitForConnection() throws IOException
   {
      displayMessage( "Waiting for connection\n" );
      // blocking call (synchronous call.... not asynchronous (= on demand...where your code gets called back like an event handler)
      connection = server.accept(); // allow server to accept connection
      displayMessage( "Connection " + counter + " received from: " +
         connection.getInetAddress().getHostName() );
   }
}