import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Chat {

    ArrayList<Client> clients = new ArrayList<>();

    public void sendAll(String name, String message){
        for (Client client : clients) {
            client.receive("[" + name + "]-> " + message);
        }
    }

    public void personalMessage(String name, String message, String fromName){
        for (Client client : clients){
            if (client.getName().equals(name)){
                client.receive("#" + fromName + "#-> " + message);
            }
        }
    }

    public void greeting(String name){
        for (Client client : clients) {
            client.receive("Welcome a new member of a chat: " + name);
        }
    }

    public void goodbye(String name){
        for (Client client : clients) {
            client.receive(name + " has left the chat...");
        }
    }

    public boolean checkInClients(String name){
        for(Client client : clients){
            if(client.getName().equals(name)){
                return true;
            }
        }
        return false;
    }


    private void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        while (true){
            System.out.println("Waiting...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            clients.add(new Client(socket, this));
        }
    }


    public static void main(String[] args) throws IOException {
        new Chat().run();
    }
}
