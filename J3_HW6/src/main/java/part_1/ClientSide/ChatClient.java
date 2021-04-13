package part_1.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends JFrame {

    private final static String IP_ADDRESS = "localhost";
    private final static int SERVER_PORT = 8081;

    private JTextField jInputMessageHere;
    private JLabel jInputNameHere;
    private JTextArea jChatField;
    private final String VERSION = "0.4.2";

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isAuthorized;

    public ChatClient(){
        try {
            connection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatClientGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    private void connection() throws IOException {
        socket = new Socket(IP_ADDRESS, SERVER_PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        LogSave ls = new LogSave();
        setAuthorized(false);
        Thread thread = new Thread(()->{
            Thread authCheckerThread = new Thread(() -> {
                try {
                    Thread.sleep(120000);
                    while(true){
                        if (!this.isAuthorized()) {
                            closeConnection();
                            jChatField.setText("No auth");
                            break;
                        }else break;
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }});
            authCheckerThread.start();
            try {
                while (true){
                    String serverMessage = dis.readUTF();
                    if(serverMessage.startsWith("/authok")){
                       setAuthorized(true);
                        jChatField.setText("");
                        jChatField.append(serverMessage + "\n");
                        jChatField.append(ls.logGet100().toString() + "\n");
                        jInputNameHere.setText("User: " + serverMessage.substring(8));
                        break;
                    }
                    jChatField.append("\n" +serverMessage +"\n");
                }
                while (true) {
                    String serverMessage = dis.readUTF();
                    if(serverMessage.startsWith("/cn OK"))jInputNameHere.setText("User: " + serverMessage.substring(7));
                    if(serverMessage.equals("/q")){
                        break;
                    }
                    jChatField.append(serverMessage + "\n");
                    ls.logSaveFull(serverMessage);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            closeConnection();
        });
        thread.start();
    }

    private void sendMessageToServer(){
        if(!jInputMessageHere.getText().trim().isEmpty()){
            try {
                String messageToServer = jInputMessageHere.getText();
                dos.writeUTF(messageToServer);
            } catch (IOException ignored) {
            }
        }
    }

    public void ChatClientGUI() {

        setBounds(640, 480, 640, 480);
        setMinimumSize(new Dimension(640,480));
        setTitle(VERSION);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jChatField = new JTextArea();
        jChatField.setText("for start - /auth [login] [password]" + "\n" + " or /reg [login] [password] [nickname]");
        jChatField.setEditable(false);
        jChatField.setLineWrap(true);

        add(new JScrollPane(jChatField), BorderLayout.CENTER);

        JLabel jlYourMessages = new JLabel("Your messages: ");
        add(jlYourMessages, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        JButton jbSendMessage = new JButton("Send");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);

        jInputMessageHere = new JTextField("Input your message here: ");
        bottomPanel.add(jInputMessageHere, BorderLayout.CENTER);

        jInputNameHere = new JLabel("not logged in");
        bottomPanel.add(jInputNameHere, BorderLayout.WEST);
        jInputNameHere.setPreferredSize(new Dimension(120,50));
        jInputNameHere.setHorizontalAlignment(JLabel.CENTER);

        jbSendMessage.addActionListener(e ->  {
            sendMessageToServer();
            jInputMessageHere.grabFocus();
        });

        jInputMessageHere.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jInputMessageHere.setText("");
            }
        });

        jInputMessageHere.addActionListener(e ->  {
            sendMessageToServer();
            jInputMessageHere.setText("");
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    dos.writeUTF("/q");
                    closeConnection();
                } catch (IOException ignored) {
                }
            }
        });
        setVisible(true);
    }

    private void closeConnection(){
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
}
