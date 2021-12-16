package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClientRmiTask6 {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        int choice = 1000;
        int x, y, z;
        Scanner in = new Scanner(System.in);
        try {
            RMIServer st = (RMIServer) Naming.lookup("//localhost:123/exam");
            System.out.println("Choose option:\n"
                    + "1 - output Houses with rooms count X\n"
                    + "2 - output Houses with rooms count X and floor in interval of Y and Z\n"
                    + "3 - output Houses with area more than X\n"
                    + "4 - exit");
            choice = in.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("Enter X:");
                    x = in.nextInt();
                    System.out.println(st.displayWithRoomsCount(x));
                }
                case 2 -> {
                    System.out.print("Enter X value: ");
                    x = in.nextInt();
                    System.out.print("Enter Y value: ");
                    y = in.nextInt();
                    System.out.print("Enter Z value: ");
                    z = in.nextInt();
                    System.out.println(st.displayWithRoomsCountInIntervalOfFloor(x, y, z));
                }
                case 3 -> {
                    System.out.println("Enter X:");
                    x = in.nextInt();
                    System.out.println(st.displayWithAreaMoreThan(x));
                }
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
