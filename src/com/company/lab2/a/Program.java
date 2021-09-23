package com.company.lab2.a;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Program {

    private int[][] forest;
    private final AtomicBoolean founded;
    private final Integer forestSize;
    public final Integer threadsCount;
    private final AtomicInteger currentRow;
    private Thread[] threads;
    private class BeeHive extends Thread{
        public BeeHive(){

        }

        public void run(){
            while (!founded.get() && currentRow.get() < forestSize){
                    patrolRow(currentRow.get());
                    currentRow.set(currentRow.get() + 1);
            }
        }
    }

    public Program(Integer forestSize){
        this.forestSize = forestSize;
        this.threadsCount = (int) Math.sqrt(forestSize);
        this.threads = new Thread[threadsCount];
        forest = new int[forestSize][forestSize];
        for (int i = 0; i < forestSize; i++){
            for (int j = 0; j < forestSize; j++){
                forest[i][j] = 0;
            }
        }

        int row = (int)(Math.random()*forestSize);
        int col = (int)(Math.random()*forestSize);
        System.out.println("Bear located in row:" + row + ", col: " + col);
        forest[row][row] = 1;

        founded = new AtomicBoolean(false);
        currentRow = new AtomicInteger(0);

    }

    public static void main (String[] args){
        Program program = new Program(1000);
        program.patrolForest();
    }

    private void patrolForest(){
        for(int i = 0; i < threadsCount; i++){
            threads[i] = new BeeHive();
            threads[i].start();
        }
        for(int i = 0; i < threadsCount; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void patrolRow(int row){
        if(founded.get()){
            return;
        }
        System.out.println(Thread.currentThread().getName() + " patrol in row:" + row);
        for(int i = 0; i < forestSize; i++) {
            if (forest[row][i] == 1) {
                System.out.println(Thread.currentThread().getName() + " founded Bear in row:" + row);
                founded.set(true);
            }
        }
    }
}
