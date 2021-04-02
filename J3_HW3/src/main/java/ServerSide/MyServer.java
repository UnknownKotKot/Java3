package ServerSide;

import ServerSide.service.ClientHandler;
import ServerSide.service.DatabaseService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final int PORT =8081;
    private Socket socket;

    private List<ClientHandler> clientsList;
    private DatabaseService dbs;

    public DatabaseService getDbs() {
        return this.dbs;
    }

    public static void main(String[] args) {
        new MyServer();
    }

    public MyServer(){

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            this.dbs = new DatabaseService();
            dbs.start();
            clientsList = new ArrayList<>();
            while(true){
                socket = serverSocket.accept();
                new ClientHandler(this, socket);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }finally {
            if (dbs!=null){
                dbs.stop();
            }
        }
    }

    public synchronized void sendMessageToClients(String message){
        for (ClientHandler c : clientsList){
            c.sendMessage(message);
        }
    }

    public synchronized void sendPrivateMessage(String message, String nameRecipient, String nameSender) {
        if(clientsList.stream()
                .noneMatch(a -> a.getName().equals(nameRecipient))){
            clientsList.stream()
                    .filter(a->a.getName().equals(nameSender))
                    .forEach(a->a.sendMessage("No such active users."));
        }else{
            clientsList.stream()
                    .filter(a->a.getName().equals(nameRecipient))
                    .forEach(a->a.sendMessage("Message from [" + nameSender +"] :" + message));
        }
    }

    public synchronized void  sendOnlineClientList(ClientHandler c) {
        c.sendMessage("Users online: " + clientsList.toString());
    }

    public synchronized void subscribe(ClientHandler c){
        clientsList.add(c);
    }
    public synchronized void unSubscribe(ClientHandler c){
        clientsList.remove(c);
    }

    public boolean isNickBusy(String nick){
        return clientsList.stream()
                .anyMatch(a->a.getName().equals(nick));
    }

    private void closeConnection(Socket s, DataInputStream dis, DataOutputStream dos){
        try {
            dos.flush();
        } catch (IOException ignored) {
        }
        try {
            dis.close();
        } catch (IOException ignored) {
        }
        try {
            dos.close();
        } catch (IOException ignored) {
        }
        try {
            s.close();
        } catch (IOException ignored) {
        }
    }
}