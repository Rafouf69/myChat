package ihm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class myIHM {
    private JTextField enterYourMessageTextField;
    private JButton OK;
    private JTextPane okTextPane;
    private JList list1;
    private JPanel panelMain;

    public myIHM() {
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("myIHM");
        frame.setContentPane(new myIHM().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
