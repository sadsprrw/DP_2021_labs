package com.company.lab1.b;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class Program {
    static Thread th1;
    static Thread th2;

    static GlobalValue sldrValue = new GlobalValue(50);
    static AtomicInteger semaphore = new AtomicInteger();

    public static void main(String[] args) {
        JFrame win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(400, 300);

        JPanel panel = getjPanel();

        win.setContentPane(panel);
        win.setLocationRelativeTo(null);
        win.setVisible(true);
    }

    private static JPanel getjPanel() {
        JPanel mainPanel = new JPanel();
        JPanel firstLine = new JPanel();
        JPanel secondLine = new JPanel();
        JPanel thirdLine = new JPanel();
        JPanel fourthLine = new JPanel();

        semaphore.set(1);
        JLabel lblTh1 = new JLabel("Thread 1", SwingConstants.CENTER);
        JLabel lblTh2 = new JLabel("Thread 2", SwingConstants.CENTER);

        lblTh1.setPreferredSize(new Dimension(200,50));
        lblTh2.setPreferredSize(new Dimension(200,50));

        firstLine.add(lblTh1);
        firstLine.add(lblTh2);

        JLabel semCondition = new JLabel("Free", SwingConstants.CENTER);
        semCondition.setPreferredSize(new Dimension(200,50));

        secondLine.add(semCondition);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 90, sldrValue.getValue());
        slider.setPreferredSize(new Dimension(350,50));

        thirdLine.add(slider);

        JButton btnStart1 = new JButton("Start 1");
        JButton btnStop1 = new JButton("Stop 1");
        JButton btnStart2 = new JButton("Start 2");
        JButton btnStop2 = new JButton("Stop 2");

        btnStop1.setEnabled(false);
        btnStop2.setEnabled(false);
        btnStart1.addActionListener(e -> {
            if(semaphore.compareAndSet(1,0)) {
                th1 = new Thread(
                        () -> {
                            while (!Thread.interrupted()) {
                                slider.setValue(10);
                                try {
                                    Thread.sleep(3);
                                } catch (InterruptedException interruptedException) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        });
                th1.setPriority(Thread.MIN_PRIORITY);
                th1.start();
                btnStart1.setEnabled(false);
                btnStart2.setEnabled(false);
                btnStop1.setEnabled(true);
                semCondition.setText("Busy");
            }
        });

        btnStop1.addActionListener(e -> {
            if(semaphore.compareAndSet(0,1)){
                th1.interrupt();
                btnStart1.setEnabled(true);
                btnStart2.setEnabled(true);
                btnStop1.setEnabled(false);
                semCondition.setText("Free");
            }
        });
        btnStart2.addActionListener(e -> {
            if(semaphore.get() == 1) {
                semaphore.set(0);
                th2 = new Thread(
                        () -> {
                            while (!Thread.interrupted()) {
                                slider.setValue(90);
                                try {
                                    Thread.sleep(3);
                                } catch (InterruptedException interruptedException) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        });
                th2.setPriority(Thread.MAX_PRIORITY);
                th2.start();
                btnStart1.setEnabled(false);
                btnStart2.setEnabled(false);
                btnStop2.setEnabled(true);
                semCondition.setText("Busy");
            }
        });

        btnStop2.addActionListener(e -> {
            if(semaphore.get() == 0) {
                semaphore.set(1);
                th2.interrupt();
                btnStart1.setEnabled(true);
                btnStart2.setEnabled(true);
                btnStop2.setEnabled(false);
                semCondition.setText("Free");
            }
        });
        fourthLine.setLayout(new GridLayout(1,4,27,10));
        fourthLine.add(btnStart1);
        fourthLine.add(btnStop1);
        fourthLine.add(btnStart2);
        fourthLine.add(btnStop2);

        mainPanel.add(firstLine);
        mainPanel.add(secondLine);
        mainPanel.add(thirdLine);
        mainPanel.add(fourthLine);
        return mainPanel;
    }
}