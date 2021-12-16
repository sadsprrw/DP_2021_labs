package socket;

import model.House;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Callback
{
    public boolean shouldEnd = false;
}

class HouseHandler implements Runnable
{
    private Socket socket;
    private Callback callback;
    private List<House> houses;

    public HouseHandler(Socket socket, Callback callback, List<House> houses)
    {
        this.callback = callback;
        this.socket = socket;
        this.houses = houses;
    }

    @Override
    public void run() {
        try {
            InputStreamReader ois = new InputStreamReader(socket.getInputStream());
            System.out.println("Receiving input");
            BufferedReader reader = new BufferedReader(ois);
            String message = reader.readLine();
            String[] splitMessage = message.split("#");
            String commandIndex = splitMessage[0];
            System.out.println("Command " + splitMessage[0]);

            if (commandIndex.equals("4"))
            {
                System.out.println("Close command");
                callback.shouldEnd = true;
                return;
            }
            List<House> result = new ArrayList<>();
            switch (commandIndex) {
                case "1" -> {
                    Integer x = Integer.parseInt(splitMessage[1]);
                    for (House house : houses) {
                        if (house.getRoomsCount().equals(x)) {
                            result.add(house);
                        }
                    }
                    break;
                }
                case "2" -> {
                    Integer x = Integer.parseInt(splitMessage[1]);
                    Integer y = Integer.parseInt(splitMessage[2]);
                    Integer z = Integer.parseInt(splitMessage[3]);
                    for (House house : houses) {
                        if ((house.getRoomsCount().equals(x)) &&
                                (house.getFloor() > y && house.getFloor() < z)) {
                            result.add(house);
                        }
                    }
                    break;
                }
                case "3" -> {
                    Integer x = Integer.parseInt(splitMessage[1]);
                    for (House house : houses) {
                        if (house.getArea() > x){
                            result.add(house);
                        }
                    }
                    break;
                }
                default -> throw new IllegalStateException("Unexpected value: " + commandIndex);
            }
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            ois.close();
            oos.close();
            socket.close();
        }
        catch (IOException e) { }
    }
}


public class ServerSocketTask6 {
    private static ServerSocket server;

    private static int port = 9876;

    public static Callback c = new Callback();

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server = new ServerSocket(port);
        List<House> houses =  new ArrayList<House>() {
            {
                add(new House("1", 112, 50, 4, 1,
                        "Velyka Vasylkivska", "residential", 20));
                add(new House("2", 173, 100, 8, 3,
                        "Mate Zalki", "residential", 35));
                add(new House("3", 44, 70, 2, 2,
                        "Mejigirska", "business center", 40));
                add (new House("4", 6, 150, 1, 4,
                        "Pecherska", "residential", 40));
                add(new House("5", 121, 50, 1, 1,
                        "Pochtova Plosha", "business center", 10));
            }
        };

        while(!c.shouldEnd){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            HouseHandler handler = new HouseHandler(socket, c, houses);
            handler.run();
        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }
}
