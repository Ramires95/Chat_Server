package server;

public class ServerLauncher {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (Exception e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}
