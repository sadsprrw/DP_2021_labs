package com.company.lab3.a;

public class Bear implements Runnable {
    private int eatenPots;
    private final int capacity;
    public boolean isSleeping = true;
    Pot pot;

    Bear(Pot pot, int capacity){
        this.pot = pot;
        this.eatenPots = 0;
        this.capacity = capacity;
    }

    public synchronized boolean isNotSatiated(){
        return eatenPots != capacity;
    }

    public synchronized void wakeUp(){
        isSleeping = false;
        notifyAll();
    }

    @Override
    public void run() {
        while(isNotSatiated()){
            synchronized (this){
                while (isSleeping){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pot.eat();
                eatenPots++;
                System.out.println("Bear satiety: " + eatenPots + " / " + capacity);
                isSleeping = true;
                if(isNotSatiated()) notifyAll();
            }
        }
    }
}
