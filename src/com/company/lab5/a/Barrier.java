package com.company.lab5.a;

public class Barrier {
    private int partiesAtStart;
    private int partiesAwait;

    public Barrier(int parties) {
        this.partiesAtStart = parties;
        this.partiesAwait = parties;
    }

    public synchronized void await() throws InterruptedException {
        partiesAwait--;
        if(partiesAwait > 0) {
            this.wait();
        }

        partiesAwait = partiesAtStart;
        notifyAll();
    }
}
