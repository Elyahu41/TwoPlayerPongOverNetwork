package com.company;

import javax.swing.*;
// Elyahu Jacobi
public class Player2Starter {
    public static void main(String[] args) {//start this method second
        PongPlayer2 application; // declare client application

        // if no command line args
        if ( args.length == 0 )
            application = new PongPlayer2( "localhost" ); // connect to localhost
        else
            application = new PongPlayer2( args[ 0 ] ); // use args to connect

        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.runClient(); // run client application
    }
}
