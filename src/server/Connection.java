package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable{

    private Server server;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public Connection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run()  {

        try {

            openStreams();
            setName();
            
            while (!socket.isClosed()) {
                printReceivedMessage(in);
            }
        } catch (IOException e) {
            handleConnectionError(e);
        } finally {
            close();
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public void printReceivedMessage(BufferedReader in) throws IOException{
        String message = in.readLine();
        server.broadcast(name + ": " + message, this);
    }


    private void openStreams() throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    private void setName() throws IOException{
        out.println("Insert username:");
        name = in.readLine();
    }

    private void handleConnectionError(IOException e) {
        System.err.println("Connection Error: " + e.getMessage());
        server.remove(this);
    }

    private void close() {
        try {
            if (in != null) in.close();
            if(out != null) out.close();
            if(!socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
