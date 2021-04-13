package part_1.ServerSide.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import part_1.ServerSide.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {

    public static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

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
                                LOGGER.info(nick + " has join the chat");
                                return;
                            } else {
                                sendMessage("This user '" + nick + "' alrdy in chat. ");
                                LOGGER.info("user tried to get " + nick + ". This nick is alrdy in the chat");
                            }
                        }
                    } else {
                        sendMessage("Wrong login/password");
                        LOGGER.info("user tried to get auth with wrong pass or username");
                    }
                } else {
                    sendMessage("Wrong login/password");
                    LOGGER.info("user tried to get auth with wrong pass or username");
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
                        LOGGER.info("New user reg: " + newNickname);
                    }else{
                        sendMessage("/reg: failed - nickName or login already exists");
                        LOGGER.info("user tried to reg with existed username or nickname");
                    }
                }else{
                    sendMessage("Wrong syntax, try again");
                    LOGGER.info("user tried to reg with wrong syntax");
                }
            }
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException ignored) {
            LOGGER.error(ignored.getMessage());
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
                    LOGGER.info("user " + this.getName() + " call for OnlineUsers");
                    continue;
                }
                if (messageFromClient.equals("/q")) {
                    sendMessage(messageFromClient);
                    LOGGER.info("user " + this.getName() + " left teh chat");
                    return;
                }
                if (messageFromClient.startsWith("/w")) {
                    String[] incomeMessageOriginal = messageFromClient.split("\\s");
                    String[] incomeMessageReworked = Arrays.copyOfRange(incomeMessageOriginal, 2, incomeMessageOriginal.length);
                    String name = incomeMessageOriginal[1];
                    String outputMessage = String.join(" ", incomeMessageReworked);
                    myServer.sendPrivateMessage(outputMessage, name, this.getName());
                    LOGGER.info("user " + this.getName() + " whisper to " + name + " " + messageFromClient);
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
                            LOGGER.info("user " + oldNickName + " changed nick to  " + newNickName);
                            name = newNickName;
                        }else{
                            sendMessage("Changing name: failed");
                            LOGGER.info("user " + oldNickName + " Changing name: failed to  " + newNickName);
                        }
                    }else{
                        sendMessage("Wrong syntax, try again");
                        LOGGER.info("user " + incomeMessageReworked[0] + " Changing name: failed to  " + incomeMessageReworked[1] + " due to wrong syntax");
                    }
                }
            }else {
                myServer.sendMessageToClients(name + ": " + messageFromClient);
                LOGGER.info("user " + getName() + " sending  " + messageFromClient);
            }
        }
    }

    private void closeConnection(){
        myServer.unSubscribe(this);
        myServer.sendMessageToClients(name + " leave chat");
        try {
            dos.flush();
        } catch (IOException ignored) {
            LOGGER.error(ignored.getMessage());
        }
        try {
            dis.close();
        } catch (IOException ignored) {
            LOGGER.error(ignored.getMessage());
        }
        try {
            dos.close();
        } catch (IOException ignored) {
            LOGGER.error(ignored.getMessage());
        }
        try {
            socket.close();
        } catch (IOException ignored) {
            LOGGER.error(ignored.getMessage());
        }
    }

    @Override
    public String toString() {
        return name;
    }
}