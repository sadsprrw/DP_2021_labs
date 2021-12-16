package socket;

import model.House;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class ClientSocketTask6 {
    private static int port = 9876;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Scanner scan = new Scanner(System.in);
        while (true)
        {
            System.out.println("Choose option:\n"
                    + "1 - output Houses with rooms count X\n"
                    + "2 - output Houses with rooms count X and floor in interval of Y and Z\n"
                    + "3 - output Houses with area more than X\n"
                    + "4 - exit");
            socket = new Socket(host.getHostName(), port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            int commandIndex = scan.nextInt();
            if (commandIndex == 4)
            {
                socket = new Socket(host.getHostName(), port);
                oos = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Sending close Request");
                oos.writeInt(commandIndex); oos.flush();
                break;
            }
            switch (commandIndex) {
                case 1, 3 -> {
                    System.out.println("Enter X:");
                    int x = scan.nextInt();
                    String message = "" + commandIndex + "#" + x;
                    oos.writeBytes(message);
                    oos.flush();
                    break;
                }
                case 2 -> {
                    System.out.println("Enter X:");
                    int x = scan.nextInt();
                    System.out.println("Enter Y:");
                    int y = scan.nextInt();
                    System.out.println("Enter Z:");
                    int z = scan.nextInt();
                    String message = "" + commandIndex + "#" + x + "#" + y + "#" + z;
                    oos.writeBytes(message);
                    oos.flush();
                    break;
                }
            }
            System.out.println("Results: ");
            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<House> results = (ArrayList<House>) ois.readObject();
            for (House house: results)
            {
                System.out.println(house.toString());
            }
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
        oos.writeInt(3);
        System.out.println("Shutting down client!!");
    }
}
