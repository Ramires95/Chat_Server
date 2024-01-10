package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        
        try {
            openStreams();
            while (!socket.isClosed()) {
                String input = scanner.nextLine();
                writer.println(input);
            }
        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        } finally {
            close();
        }
        
    }

    private void openStreams() throws IOException {
        this.scanner = new Scanner(System.in);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    private void close() {
        try {
            if (!socket.isClosed()) socket.close();
            if(scanner != null) scanner.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}
