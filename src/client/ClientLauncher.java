package client;

public class ClientLauncher {

    public static void main(String[] args) {
        
        try {
            Client client = new Client("localhost", 9000);
            client.start();
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
