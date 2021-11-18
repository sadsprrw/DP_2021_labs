package com.company.lab2.c;

import java.util.concurrent.ForkJoinPool;

public class Program {
    public static void main(String[] args) {
        Integer countOfMonks = 50;
        Tournament tournament = new Tournament(countOfMonks);
        ForkJoinPool pool = new ForkJoinPool();
        Monk winner = pool.invoke(tournament);
        System.out.println("The winner is " + winner.toString());
    }
}
