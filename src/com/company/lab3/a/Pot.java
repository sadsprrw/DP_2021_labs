package com.company.lab3.a;

public class Pot {
    private int capacity;
    private int current_fullness;

    Pot(int capacity){
        this.capacity = capacity;
    }

    public synchronized void eat(){
        System.out.println("Pot was eaten away.");
        current_fullness = 0;
        notifyAll();
    }

    public synchronized void addHoney(){
        while (isFull()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        current_fullness++;
    }

    public boolean isFull(){
        return current_fullness == capacity;
    }
}
