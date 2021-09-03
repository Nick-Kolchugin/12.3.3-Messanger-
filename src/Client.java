import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements Runnable{

    private String name;
    Socket socket;
    Chat chat;

    Scanner scanner;
    PrintStream ps;

    Pattern patternLs = Pattern.compile("^(.*?)\\s(.*?)\\s(.*?)$");
    Matcher matcher;

    String lsCode = "";
    String lsName = "";
    String lsMsg = "";

    public String getName() {
        return name;
    }

    public Client(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat = chat;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void receive(String message){
        ps.println(message);
    }

    private boolean canSendLs(){
        if(lsCode.equals("/ls") && chat.checkInClients(lsName)){
            return true;
        }
        return false;
    }

    /*private String getPersonalMessageToName(Matcher matcher){
        String name = null;
        while(matcher.find()){
            name = matcher.group(2);
        }
        return name;
    }

    private String getPersonalMessage(Matcher matcher){
        String msg = null;
        while(matcher.find()){
            msg =  matcher.group(3);
        }
        return msg;
    }*/

    private void convert(Matcher matcher){
        while (matcher.find()){
            lsCode = matcher.group(1);
            lsName = matcher.group(2);
            lsMsg = matcher.group(3);
        }
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

                matcher = patternLs.matcher(input);
                convert(matcher);
                if(canSendLs()){
                    chat.personalMessage(lsName, lsMsg, name);
                    lsCode = "";
                    lsName = "";
                    lsMsg = "";
                }
                else{
                    chat.sendAll(name, input);
                }

            }
            
            chat.goodbye(name);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
