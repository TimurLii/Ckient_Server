package chat.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {

    //region Field
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;
    public final static ArrayList<ClientManager> clientManagers = new ArrayList<>();

    private String nameCurrentUser;
    //endregion

    //region Constructor
    public ClientManager(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            name = bufferedReader.readLine();
            clientManagers.add(this);
            System.out.println(name + " подключился  к  чату.");
            broadcastMessage("Server : " + name + " подключился  чат.");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    //endregion

    //region Method run
    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (searchUser(messageFromClient) ){
                    broadcastMessage(messageFromClient, nameCurrentUser);
                }else {
                    broadcastMessage(messageFromClient);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }


    //endregion

    //region Method broadcastMessage
    private void broadcastMessage(String message) {
        for (ClientManager manager : clientManagers) {
            try {
                if (!manager.name.equals(name)) {
                    manager.bufferedWriter.write(message);
                    manager.bufferedWriter.newLine();
                    manager.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    //endregion

    //region Method broadcastMessage
    private void broadcastMessage(String message, String userName) {
        for (ClientManager manager : clientManagers) {
            if (manager.name.equals(userName)) {
                try {
                    manager.bufferedWriter.write(message);
                    manager.bufferedWriter.newLine();
                    manager.bufferedWriter.flush();

                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    //endregion

    //region Method closeEverything
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            removeClient();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//endregion

    //region Method  removeClient
    private void removeClient() {
        clientManagers.remove(this);
        System.out.println(this.name + " покинул чат.");
        broadcastMessage("Server : " + this.name + " покинул чат.");
    }

//endregion

    //region Method searchCurrentUser
    //поиск  по имени
    private boolean searchUser(String message) {
        String[] s = message.split(" ");
        nameCurrentUser = s[2];
        for (ClientManager manager : clientManagers) {
            if (manager.name.equals(s[0])) {
                return  true;
            }
        }
        return false;
    }

    //endregion

}
