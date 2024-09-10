package chat.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Program  {
    public static void main(String[] args) {


        try {
            System.out.println("Enter your name.");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();

            Socket socket = new Socket("localhost" , 1400);

            Client client = new Client(socket, name);

            InetAddress address = socket.getInetAddress();
            System.out.println("InetAddress  " + address);
            String remoteIP = address.getHostAddress();
            System.out.println("Remote IP " + remoteIP);
            System.out.println("LocalPort  " + socket.getLocalPort());

            client.listenForMessage();
            client.sendMessage();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
