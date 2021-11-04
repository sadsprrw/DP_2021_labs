package com.company.module1.first;

//Variant 6
//Task №11
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

enum RangeZone {
    FIRST_ZONE("First Zone"),
    SECOND_ZONE("Second Zone");

    private final String name;

    RangeZone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Terminal {
    private final int number;
    private final RangeZone zone;
    private final Semaphore rampsSemaphore;

    public Terminal(int number, RangeZone zone, int countOfRumps) {
        this.number = number;
        this.zone = zone;
        this.rampsSemaphore = new Semaphore(countOfRumps);
    }

    public int getNumber() {
        return number;
    }

    public RangeZone getZone() {
        return zone;
    }

    public boolean allRampsBusy() {
        return rampsSemaphore.availablePermits() == 0;
    }

    public void land() {
        try {
            rampsSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void depart() {
        rampsSemaphore.release();
    }
}

class Plane extends Thread {
    private final RangeZone zone;
    private final int capacity;
    private final Terminal[] terminals;

    public Plane(RangeZone zone, int capacity, Terminal[] terminals) {
        this.zone = zone;
        this.capacity = capacity;
        this.terminals = terminals;
    }

    private void doLand(Terminal terminal) {
        terminal.land();
        System.out.println("Plane №" + Thread.currentThread().getId() + " has landed in terminal №" +
                terminal.getNumber() + ".");
        try {
            Thread.sleep(capacity * 10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Plane №" + Thread.currentThread().getId() +
                " has finished loading/unloading in terminal №" + terminal.getNumber() + ".");
        terminal.depart();
    }

    @Override
    public void run() {
        int last_index = 0;
        boolean hasLanded = false;

        System.out.println("Plane №" + Thread.currentThread().getId() + " arrived at airport.");
        for (int i = 0; i < terminals.length; i++) {
            if (terminals[i].getZone() == zone) {
                if (!terminals[i].allRampsBusy()) {
                    doLand(terminals[i]);
                    hasLanded = true;
                    break;
                } else {
                    last_index = i;
                }
            }
        }

        if (!hasLanded) {
            doLand(terminals[last_index]);
        }
    }
}

public class Program {
    private static final int countTerminals = 4;
    private static final int countPlanes = 50;

    public static void main(String[] args) {
        Random random = new Random();

        Terminal[] terminals = new Terminal[countTerminals];
        for (int i = 0; i < countTerminals; i++) {
            terminals[i] = new Terminal(i, Arrays.asList(RangeZone.values())
                    .get(random.nextInt(RangeZone.values().length)), 3);
            System.out.println("Terminal №" + i + ": " + terminals[i].getZone().getName() + ", count of ramps: 3.");
        }

        Plane[] planes = new Plane[countPlanes];
        for (int i = 0; i < countPlanes; i++) {
            planes[i] = new Plane(Arrays.asList(RangeZone.values())
                    .get(random.nextInt(RangeZone.values().length)), 100, terminals);
            planes[i].start();

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}