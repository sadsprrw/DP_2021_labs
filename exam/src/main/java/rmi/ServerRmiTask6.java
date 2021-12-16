package rmi;

import model.House;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

interface RMIServer extends Remote {
    List<House> displayWithRoomsCount(Integer x);
    List<House> displayWithRoomsCountInIntervalOfFloor(Integer x, Integer y, Integer z);
    List<House> displayWithAreaMoreThan(Integer x);
}


public class ServerRmiTask6 {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(123);
        RMIServer service = new Service();
        registry.rebind("exam", service);
        System.out.println("Server started!");
    }

    static class Service extends UnicastRemoteObject implements RMIServer {
        List<House> houses;

        Service() throws RemoteException {
            super();
            houses = new ArrayList<House>() {
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
        }

        @Override
        public List<House> displayWithRoomsCount(Integer x) {
            List<House> results = new ArrayList<>();
            for (House house : houses) {
                if (house.getRoomsCount().equals(x)) {
                    results.add(house);
                }
            }
            return results;
        }

        @Override
        public List<House> displayWithRoomsCountInIntervalOfFloor(Integer x, Integer y, Integer z) {
            List<House> results = new ArrayList<>();
            for(House house: houses) {
                if((house.getRoomsCount().equals(x)) &&
                        (house.getFloor() > y && house.getFloor() < z)) {
                    results.add(house);
                }
            }
            return results;
        }

        @Override
        public List<House> displayWithAreaMoreThan(Integer x){
            List<House> results = new ArrayList<>();
            for (House house : houses) {
                if (house.getArea() > x){
                    results.add(house);
                }
            }
            return results;
        }
    }
}
