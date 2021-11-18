package com.company.lab2.c;

import java.util.Random;

public class Monk implements Comparable {
    private Integer power;
    private String monastery;
    private int monkId;
    private static int monkCounter = 0;
    public Monk() {
        Random r = new Random();
        power = r.nextInt(100);
        monastery = (r.nextInt(2) == 0) ? "Guan-un" : "Guan-in";
        monkId = monkCounter++;
    }

    @Override
    public String toString() {
        return "Monk №" + monkId + " (" + monastery +" monastery) | Power: " + power;
    }

    public int compareTo(Object o) {
        Monk other = (Monk)o;
        if(this.power > other.power) {
            return 1;
        } else if (this.power < other.power){
            return -1;
        } else {
            return 0;
        }
    }
    static Monk max(Monk first, Monk second){
        if(first.power > second.power){
            return first;
        } else {
            return second;
        }
    }
}
