package com.company.lab2.b;

import java.util.LinkedList;

public class Program {

    static boolean warehouseStatus = true;
    static boolean goodsStatus = true;
    static boolean truckStatus = true;
    static int truckLoad = 0;

    public static void main(String[] args) throws InterruptedException {
        final PC pc = new PC(30);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (warehouseStatus) {
                    try {
                        pc.grab();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (goodsStatus) {
                    try {
                        pc.transfer();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (truckStatus) {
                    try {
                        pc.count();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();


    }

    public static class PC {

        final LinkedList<Integer> truck;
        LinkedList<Integer> warehouse;
        final LinkedList<Integer> goods;
        int capacity;
        int result = 0;
        int goodsCapacity;

        public PC(int capacity){
            this.capacity = capacity;
            this.goodsCapacity = 2;
            warehouse = new LinkedList<Integer>();
            goods = new LinkedList<Integer>();
            truck = new LinkedList<Integer>();
            for(int i = 0; i < capacity; i++){
                warehouse.add((int)(Math.random()*100));
            }
        }

         synchronized public void grab() throws InterruptedException
        {
            if(warehouse.size() == 0){
                System.out.println("Ivanov done!");
                warehouseStatus = false;
                return;
            }

                while (goods.size() == goodsCapacity)
                    wait();

                System.out.println("Ivanov taking something... Value is - " + warehouse.getFirst());
                goods.add(warehouse.getFirst());
                warehouse.removeFirst();
                notify();
        }

         synchronized public void transfer() throws InterruptedException
        {
            if(warehouse.size() == 0 && goods.size() == 0){
                System.out.println("Petrov done!");
                goodsStatus = false;
                return;
            }

            while (goods.size() == 0)
                wait();

            System.out.println("Petrov loads something... Value is - " + goods.getFirst());
            truck.add(goods.getFirst());
            goods.removeFirst();
            notifyAll();

        }

        synchronized public void count() throws InterruptedException
        {
            if(truck.size() == 0 && warehouse.size() == 0 && goods.size() == 0){
                System.out.println("Nechiporchuk done! Robbed on - " + result + " hryvnas!");
                truckStatus = false;
                return;
            }
            while (truck.size() == 0)
                wait();

            System.out.println("Nechiporchuk counting new values..");
            result += truck.getFirst();
            truck.removeFirst();
        }
    }
}

