package com.company;
// Client portion of a stream-socket connection between client and server.

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
// Elyahu Jacobi
public class PongPlayer2 extends PongParent
{
   private String chatServer; // host server for this application
   private Socket client; // socket to communicate with server


   // initialize chatServer and set up GUI
   public PongPlayer2(String host )
   {
      super("Player 2 (Client)");
      chatServer = host; // set server to which this client connects
   }

   // connect to server and process messages from server
   public void runClient() 
   {
      try // connect to server, get streams, process connection
      {
         connectToServer(); // create a Socket to make connection
         super.getStreams(client); // get the input and output streams
         super.processConnection(); // process connection
      }
      catch ( EOFException eofException ) 
      {
         displayMessage( "\nClient terminated connection" );
      }
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      }
      finally 
      {
         closeConnection(client,"\nClosing connection" ); // close connection
      }
   }

   private void connectToServer() throws IOException
   {      
      displayMessage( "Attempting connection\n" );

      // create Socket to make connection to server
      client = new Socket( InetAddress.getByName( chatServer ), 12345 );

      // display connection information
      displayMessage( "Connected to: " + 
         client.getInetAddress().getHostName() );
   }

   // manipulates enterField in the event-dispatch thread
}