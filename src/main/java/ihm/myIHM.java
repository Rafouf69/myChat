package ihm;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class myIHM {
    private JTextField enterYourMessageTextField;
    private JButton envoyerMessageButton;
    private JTextPane msgTextPanel;

    private JPanel panelMain;
    private JTextField nomSalonTextField;
    private JButton creerSalonButton;
    private JLabel labelConnected;
    private JLabel labelError;
    private JButton actualiserSalonButton;
    private JLabel labelNom;
    private JButton envoyerNomButton;
    private JTextField textFieldChanger;
    private JComboBox comboBoxSalon;
    private JLabel labelSalon;

    public myIHM() {
        envoyerMessageButton.addActionListener(new ActionListener() {
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

        final Socket echoSocket;
        final PrintStream socOut;
        final BufferedReader stdIn;
        final BufferedReader socIn;

        try {
            // creation socket ==> connexion
            echoSocket = new Socket("localhost", 5555);
            socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            socOut = new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            ihm.labelConnected.setText("Connected");

            ihm.envoyerMessageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JsonObject json = new JsonObject();
                    if(!ihm.enterYourMessageTextField.getText().equals("")) {
                        json.addProperty("type", "msg");
                        json.addProperty("msg", ihm.enterYourMessageTextField.getText());
                        socOut.println(json.toString());
                    }

                }
            });

            ihm.comboBoxSalon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(ihm.comboBoxSalon.getItemCount()!=0){
                        JsonObject json = new JsonObject();
                        json.addProperty("type", "choisirCanal");
                        json.addProperty("msg", ihm.comboBoxSalon.getSelectedItem().toString());
                        socOut.println(json.toString());
                    }
                }
            });

            ihm.envoyerNomButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JsonObject json = new JsonObject();
                    if(!ihm.textFieldChanger.getText().equals("")) {
                        json.addProperty("type", "nom");
                        json.addProperty("msg", ihm.textFieldChanger.getText());
                        socOut.println(json.toString());
                    }
                }
            });

            ihm.creerSalonButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JsonObject json = new JsonObject();
                    if(!ihm.nomSalonTextField.getText().equals("")){
                        json.addProperty("type", "creerCanal");
                        json.addProperty("msg", ihm.nomSalonTextField.getText());
                        socOut.println(json.toString());
                    }
                }
            });

            ihm.actualiserSalonButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JsonObject json = new JsonObject();
                    json.addProperty("type", "listeSalon");
                    socOut.println(json.toString());
                }
            });

            Thread recevoir = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = socIn.readLine();
                        while (msg != null) {
                            System.out.println((msg));
                            JsonObject json = new JsonParser().parse(msg).getAsJsonObject();
                            switch (json.get("type").getAsString()){
                                case "msg":
                                    ihm.msgTextPanel.setText(json.get("msg").getAsString());
                                    break;
                                case "nom":
                                    ihm.labelNom.setText(json.get("msg").getAsString());
                                    break;
                                case "listeSalon":
                                    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                                    ArrayList<String> listeSalon = new Gson().fromJson(json.get("msg"), listType);
                                    ihm.comboBoxSalon.removeAllItems();
                                    /*for(int i=0; i<ihm.comboBoxSalon.getItemCount(); i++){
                                        System.out.println(ihm.comboBoxSalon.getItemAt(0));
                                        ihm.comboBoxSalon.removeItemAt(0);
                                    }*/
                                    System.out.println(ihm.comboBoxSalon.getItemCount());
                                    for(String salon : listeSalon){
                                        ihm.comboBoxSalon.addItem(salon);
                                    }
                                    break;
                                case "creerCanal":
                                    ihm.labelError.setText(json.get("msg").getAsString());
                                    break;
                                case "choisirCanal":
                                    ihm.labelSalon.setText(json.get("msg").getAsString());
                                    break;
                                case "error":
                                    ihm.labelError.setText(json.get("msg").getAsString());
                                    break;
                                default:
                                    break;
                            }
                            msg = socIn.readLine();
                        }
                        System.out.println("Serveur déconecté");
                        socOut.close();
                        echoSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            recevoir.start();

        } catch (UnknownHostException e) {
            ihm.labelConnected.setText("Don't know about host: localhost");
        } catch (IOException e) {
            ihm.labelConnected.setText("Couldn't get I/O for the connection to:5555");
        }
    }

}
