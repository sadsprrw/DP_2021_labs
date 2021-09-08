package com.company.lab1.a;

class GlobalValue{
    private int value;

    public GlobalValue(int value) {
        this.value = value;
    }

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }

    public synchronized void increment(int value){
        this.value+=value;
    }
}