package com.company.lab3.b;

public class Visitor implements Runnable {
    private final Barber barber;

    public Visitor(Barber barber) {
        this.barber = barber;
    }

    @Override
    public void run() {
        barber.addVisitorToQueue(this);
    }
}
