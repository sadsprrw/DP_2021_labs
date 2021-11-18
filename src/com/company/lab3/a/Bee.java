package com.company.lab3.a;

public class Bee implements Runnable{
    Bear winnie;
    Pot pot;
    private int beeNumber;


    Bee(int beeNumber, Bear winnie, Pot pot){
        this.beeNumber = beeNumber;
        this.winnie = winnie;
        this.pot = pot;
    }


    @Override
    public void run() {
        while(winnie.isNotSatiated()){
            try {
                Thread.sleep(70 + (int) (Math.random() * 30));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Bee №" + beeNumber + " brought honey to pot.");
            pot.addHoney();
            if(pot.isFull()){
                System.out.println("Pot is ready to be eaten.");
                winnie.wakeUp();
            }

        }
    }
}
