package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private List<Connection> clients;
    private int PORT = 9000;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.executorService = Executors.newCachedThreadPool();
        this.clients = Collections.synchronizedList(new LinkedList<>());
    }

    public void start() throws IOException {

        while (!serverSocket.isClosed()) {
            
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this);
                executorService.submit(connection);
                clients.add(connection);
            } catch (IOException e) {
                System.err.println("Server Error: " + e.getMessage());
            }
        }

        close();
    }

    public void remove(Connection client) {
        clients.remove(client);
    }

    public void broadcast(String message, Connection sender) {
        clients.stream()
            .filter(client -> !client.equals(sender) && message != null)
            .forEach(client -> client.send(message));
    }

    private void close() throws IOException {
        serverSocket.close();
        executorService.shutdown();
    }
}
