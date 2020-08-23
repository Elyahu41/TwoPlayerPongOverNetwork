package com.company;

import javax.swing.*;
// Elyahu Jacobi
public class Player1Starter {
    public static void main(String[] args) {//start this method first
        PongPlayer1 pongPlayer1 = new PongPlayer1(); // create server
        pongPlayer1.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        pongPlayer1.runServer();
        }
}
