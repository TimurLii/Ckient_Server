package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //region Field
    private final ServerSocket serverSocket;
    //endregion

    //region Constructor

    public Server(ServerSocket socket) {
        this.serverSocket = socket;
    }
    //endregion

    //region Method runServer
    public void runServer() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                ClientManager clientManager = new ClientManager(socket);
                System.out.println("New connection from " + socket.getRemoteSocketAddress());
                Thread thread = new Thread(clientManager);
                thread.start();
            } catch (IOException e) {
                 closeSocket();
            }
        }
    }
    //endregion

    //region Method closeSocked
    private void closeSocket() {
        try{
            if(serverSocket != null) serverSocket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    //endregion
}
