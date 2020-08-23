package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

// Elyahu Jacobi
public class PongParent extends JFrame {

    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server

    private final int BALL_DIAMETER= 50, PADDLE_WIDTH = 30, PADDLE_HEIGHT = 125;
    private Point ball = new Point(100,60), paddleRight = new Point(), paddleLeft = new Point();
    private int ball_dx = 2, ball_dy = 2;
    boolean isPlayer1 = true;
    int[] data;

    public PongParent(String title) {
        setTitle(title);
        setSize(800, 800);
        setVisible(true);
        if (!title.equals("Player 1 (Server)")){
            isPlayer1 = false;
        }
        Timer ballUpdater = new Timer(40, actionEvent -> {
            ball.translate(ball_dx, ball_dy);
            if (ball.x > 500 || ball.x < 10)
            {
                ball_dx = -ball_dx;
            }
            if (ball.y > 500 || ball.y < 10)
            {
                ball_dy = -ball_dy;
            }
            repaint();
            if (ball_dx < 0) {
                if (ball.x == paddleLeft.x + PADDLE_WIDTH && ball.y > paddleLeft.y && ball.y < paddleLeft.y + PADDLE_HEIGHT) {
                    ball_dx = -ball_dx;
                }
            }
            if (ball_dx > 0) {
                if (ball.x == paddleRight.x + 500 - PADDLE_WIDTH - 20 && ball.y > paddleRight.y && ball.y < paddleRight.y + PADDLE_HEIGHT) {
                    ball_dx = -ball_dx;
                }
            }
        });
        ballUpdater.start();
        GamePanel gamePanel = new GamePanel(isPlayer1);
        setContentPane(gamePanel);
    }

    class GamePanel extends JPanel
    {
        GamePanel(boolean isPlayer1) {
            setBackground(new Color(155, 60, 140));
            setFocusable(true);
            if (isPlayer1) {
                addMouseWheelListener(mouseWheelEvent -> {
                    paddleLeft.translate(0, 5 * mouseWheelEvent.getWheelRotation());
                    repaint();
                    data = new int[]{ball.x, ball.y,paddleLeft.y};
                    sendBallAndPaddleData(data);
                });
            } else {
                addMouseWheelListener(mouseWheelEvent -> {
                    paddleRight.translate(0, 5 * mouseWheelEvent.getWheelRotation());
                    repaint();
                    data = new int[]{ball.x, ball.y,paddleRight.y};
                    sendBallAndPaddleData(data);
                });
            }
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            g.fillOval(ball.x,ball.y, BALL_DIAMETER, BALL_DIAMETER);
            g.setColor(Color.white);
            g.fillRect(paddleLeft.x,paddleLeft.y, PADDLE_WIDTH, PADDLE_HEIGHT);
            g.fillRect(paddleRight.x+500,paddleRight.y,PADDLE_WIDTH,PADDLE_HEIGHT);
        }
    }

    public void getStreams(Socket socket) throws IOException
    {
        // set up output stream for objects
        output = new ObjectOutputStream( socket.getOutputStream() );
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream( socket.getInputStream() );

        displayMessage( "\nGot I/O streams\n" );
    }

    public void processConnection() throws IOException
    {
        do // process messages sent from client
        {
            int[] data;
            try {
                data = (int[]) input.readObject();
                if (isPlayer1){
                    paddleRight.y = data[2];
                } else{
                    paddleLeft.y = data[2];
                }
                ball.x = data[0];
                ball.y = data[1];
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    public void closeConnection(Socket socket, String message)
    {
        displayMessage(message);
        try
        {
            output.close(); // close output stream
            input.close(); // close input stream
            socket.close(); // close socket
        }
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }
    }

    private void sendBallAndPaddleData(int [] data) {
        try // send object to server
        {
            output.writeObject(data);
            output.flush(); // flush data to output
        } catch (IOException ioException) {
            System.out.println("\nError writing object");
        }
    }

    // manipulates displayArea in the event-dispatch thread
    public void displayMessage( final String messageToDisplay )
    {
        // updates displayArea
        SwingUtilities.invokeLater(
                () -> System.out.println( messageToDisplay )
        );
    }
}
