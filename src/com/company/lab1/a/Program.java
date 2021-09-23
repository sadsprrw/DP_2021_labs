package com.company.lab1.a;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Program {
    static Thread th1;
    static Thread th2;
    static int prTh1 = 1;
    static int prTh2 = 1;
    static GlobalValue sldrValue = new GlobalValue(50);

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

        JLabel lblTh1 = new JLabel("Thread 1", SwingConstants.CENTER);
        JLabel lblTh2 = new JLabel("Thread 2", SwingConstants.CENTER);

        lblTh1.setPreferredSize(new Dimension(200,50));
        lblTh2.setPreferredSize(new Dimension(200,50));

        firstLine.add(lblTh1);
        firstLine.add(lblTh2);


        JLabel prLblTh1 = new JLabel("1", SwingConstants.CENTER);
        JLabel prLblTh2 = new JLabel("1", SwingConstants.CENTER);
        JButton bTh1Plus = new JButton("+");
        bTh1Plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (prTh1 != 10) {
                    prTh1 += 1;
                    prLblTh1.setText(String.valueOf(prTh1));
                    if (th1 != null) {
                        th1.setPriority(prTh1);
                    }
                }
            }
        });
        bTh1Plus.setPreferredSize(new Dimension(60,50));
        JButton bTh1Minus = new JButton("-");
        bTh1Minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (prTh1 != 1) {
                    prTh1 -= 1;
                    prLblTh1.setText(String.valueOf(prTh1));
                    if (th1 != null) {
                        th1.setPriority(prTh1);
                    }
                }
            }
        });
        bTh1Minus.setPreferredSize(new Dimension(60,50));
        JButton bTh2Plus = new JButton("+");
        bTh2Plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (prTh2 != 10) {
                    prTh2 += 1;
                    prLblTh2.setText(String.valueOf(prTh2));
                    if (th2 != null) {
                        th2.setPriority(prTh2);
                    }
                }
            }
        });
        bTh2Plus.setPreferredSize(new Dimension(60,50));
        JButton bTh2Minus = new JButton("-");
        bTh2Minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (prTh2 != 1) {
                    prTh2 -= 1;
                    prLblTh2.setText(String.valueOf(prTh2));
                    if (th2 != null) {
                        th2.setPriority(prTh2);
                    }
                }
            }
        });
        bTh2Minus.setPreferredSize(new Dimension(60,50));

        secondLine.setLayout(new GridLayout(1,6,7,10));
        secondLine.add(bTh1Minus);
        secondLine.add(prLblTh1);
        secondLine.add(bTh1Plus);
        secondLine.add(bTh2Minus);
        secondLine.add(prLblTh2);
        secondLine.add(bTh2Plus);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 90, sldrValue.getValue());
        slider.setPreferredSize(new Dimension(350,50));

        thirdLine.add(slider);

        JButton btnStart = new JButton("Start");
        JButton btnStop = new JButton("Stop");

        btnStart.addActionListener(e -> {
            sldrValue.setValue(50);
            th1 = new Thread(
                    () -> {
                        while (!Thread.interrupted()) {
                            synchronized (slider) {
                                sldrValue.increment(1);
                                slider.setValue(sldrValue.getValue());
                            }
                            try {
                                Thread.sleep(3);
                            } catch (InterruptedException interruptedException) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
            th2 = new Thread(
                    () -> {
                        while (!Thread.interrupted()) {
                            synchronized (slider) {
                                sldrValue.increment(-1);
                                slider.setValue(sldrValue.getValue());
                            }
                            try {
                                Thread.sleep(3);
                            } catch (InterruptedException interruptedException) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
            th1.setDaemon(true);
            th2.setDaemon(true);
            th1.start();
            th2.start();
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        });

        btnStop.addActionListener(e -> {
            th1.interrupt();
            th2.interrupt();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        });

        btnStop.setEnabled(false);

        fourthLine.add(btnStart);
        fourthLine.add(btnStop);

        mainPanel.add(firstLine);
        mainPanel.add(secondLine);
        mainPanel.add(thirdLine);
        mainPanel.add(fourthLine);
        return mainPanel;
    }
}