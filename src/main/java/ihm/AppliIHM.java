/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;
import java.awt.*;
import java.awt.event.*;

class AppliIHM {

    public static void main(String[] args) { // lance le programme
        Cadre1 fenetre = new Cadre1();
        fenetre.setVisible(true);
        fenetre.addWindowListener(new WindowAdapter( )   {
            public void windowClosing(WindowEvent e)
            { System.exit(0);
            }
        });
    }
}
