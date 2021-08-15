import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{

    private String name;
    Socket socket;
    Chat chat;

    Scanner scanner;
    PrintStream ps;

    public Client(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat = chat;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void receive(String message){
        ps.println(message);
    }

    @Override
    public void run() {
        try{
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            scanner = new Scanner(is);
            ps = new PrintStream(os);

            ps.println("Please, write your name");
            ps.print("Text name here->");
            String input = scanner.nextLine();
            name = input;
            chat.greeting(name);

            while (!input.equals("EXIT")){
                input = scanner.nextLine();
                chat.sendAll(name, input);
            }
            
            chat.goodbye(name);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
