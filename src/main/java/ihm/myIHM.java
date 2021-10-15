package ihm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class myIHM {
    private JTextField enterYourMessageTextField;
    private JButton OK;
    private JTextPane textPanel;
    private JList list1;
    private JPanel panelMain;
    private JTextField textField1;
    private JButton créerButton;

    public myIHM() {
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("myIHM");
        myIHM ihm = new myIHM();
        frame.setContentPane(ihm.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        ihm.textPanel.setText("rbrbr");
    }
}
