package ServerSide.service;

import ServerSide.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {

    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String name;

    public ClientHandler(MyServer myServer, Socket socket) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.name ="";

            service.execute(()->{
                try {
                    authentication();
                    readMessage();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }finally {
                    closeConnection();
                }
            });
        }catch (IOException e) {
            System.out.println(" Server error, go to admin");
        }finally {
            service.shutdown();
        }
    }

    private void authentication() throws IOException {
        while (true){
            String messageFromClient = dis.readUTF();
            if(messageFromClient.startsWith("/auth")){
                String [] arr = messageFromClient.split("\\s");
                if(arr.length==3) {
                    String sqlResult = myServer.getDbs().loginCheck(arr[1], arr[2]);
                    if (!sqlResult.equalsIgnoreCase("-1")) {
                        String nick = sqlResult.substring(8);
                        if (!nick.isEmpty()) {
                            if (!myServer.isNickBusy(nick)) {
                                sendMessage("/authok" + " " + nick);
                                name = nick;
                                myServer.subscribe(this);
                                myServer.sendMessageToClients(nick + " joined to chat");
                                return;
                            } else {
                                sendMessage("This user '" + name + "' alrdy in chat. ");
                            }
                        }
                    } else {
                        sendMessage("Wrong login/password");
                    }
                } else {
                    sendMessage("Wrong login/password");
                }
            }
            if (messageFromClient.startsWith("/reg")) {
                String[] incomeMessageOriginal = messageFromClient.split("\\s");
                String[] incomeMessageReworked = Arrays.copyOfRange(incomeMessageOriginal, 1, incomeMessageOriginal.length);
                if (incomeMessageReworked.length==3) {
                    String newLogin = incomeMessageReworked[0];
                    String newPassword = incomeMessageReworked[1];
                    String newNickname = incomeMessageReworked[2];
                    String sqlResult = myServer.getDbs().userReg(newLogin,newPassword,newNickname );
                    if(sqlResult.equalsIgnoreCase("1")){
                        sendMessage("/reg OK");
                    }else{
                        sendMessage("/reg: failed - nickName or login already exists");
                    }
                }else{
                    sendMessage("Wrong syntax, try again");
                }
            }
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException ignored) {
        }
    }

    public String getName() {
        return name;
    }

    private void readMessage() throws IOException {
        while(true) {
            String messageFromClient = dis.readUTF();
            if (messageFromClient.startsWith("/")){

                if (messageFromClient.startsWith("/oc")){
                    myServer.sendOnlineClientList(this);
                    continue;
                }
                if (messageFromClient.equals("/q")) {
                    sendMessage(messageFromClient);
                    return;
                }
                if (messageFromClient.startsWith("/w")) {
                    String[] incomeMessageOriginal = messageFromClient.split("\\s");
                    String[] incomeMessageReworked = Arrays.copyOfRange(incomeMessageOriginal, 2, incomeMessageOriginal.length);
                    String name = incomeMessageOriginal[1];
                    String outputMessage = String.join(" ", incomeMessageReworked);
                    myServer.sendPrivateMessage(outputMessage, name, this.getName());
                    continue;
                }
                if (messageFromClient.startsWith("/cn")) {
                    String[] incomeMessageOriginal = messageFromClient.split("\\s");
                    String[] incomeMessageReworked = Arrays.copyOfRange(incomeMessageOriginal, 1, incomeMessageOriginal.length);
                    if (incomeMessageReworked.length==2) {
                        String oldNickName = incomeMessageReworked[0];
                        String newNickName = incomeMessageReworked[1];
                        String sqlResult = myServer.getDbs().nickNameChange(oldNickName, newNickName);
                        if(sqlResult.equalsIgnoreCase("1")){
                            sendMessage("/cn OK " + newNickName);
                            name = newNickName;
                        }else{
                            sendMessage("Changing name: failed");
                        }
                    }else{
                        sendMessage("Wrong syntax, try again");
                    }
                }
            }else {
                myServer.sendMessageToClients(name + ": " + messageFromClient);
            }
        }
    }

    private void closeConnection(){
        myServer.unSubscribe(this);
        myServer.sendMessageToClients(name + " leave chat");
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
            socket.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public String toString() {
        return name;
    }
}