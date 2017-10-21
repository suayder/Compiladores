package Trabalho2;

import javax.swing.*;

public class Trabalho2 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Trabalho 1");
        frame.setContentPane(new Trabalho2GUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
