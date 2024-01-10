package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader in;

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public void start() {

        Thread thread = new Thread(new ClientHandler(socket));
        thread.start();

        readMessageLoop();
    }

    private void readMessageLoop() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!socket.isClosed()) {
                String message = in.readLine();
                if (message != null) {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            System.err.println("Connection Error: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void close() {
        try {
            if (in != null) in.close();
            if (!socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage() );
        }
    }

}
