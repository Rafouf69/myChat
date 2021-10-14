/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ihm;

/**
 *
 * @author vigno
 */
import java.awt.*; // utilisation des classes du package awt

class Cadre1 extends Frame { // la classe Cadre1 hérite de la classe des fenêtres Frame

    Button button1 = new Button();// création d'un objet de classe Button
    Label label1 = new Label();// création d'un objet de classe Label
    CheckboxGroup checkboxGroup1 = new CheckboxGroup();// création d'un objet groupe de checkbox
    Checkbox checkbox1 = new Checkbox();
    Checkbox checkbox2 = new Checkbox();
    Checkbox checkbox3 = new Checkbox();
    TextField textField1 = new TextField();// création d'un objet de classe TextField

    //Constructeur de la fenêtre
    public Cadre1() {  //Constructeur sans paramètre
        Initialiser();// Appel à une méthode privée de la classe
    }

    private void Initialiser() {  //Création et positionnement de tous les composants
        this.setResizable(false); // la fenêtre ne peut pas être retaillée par l'utilisateur
        this.setLayout(null); // pas de Layout, nous positionnons les composants nous-mêmes
        this.setBackground(Color.yellow); // couleur du fond de la fenêtre
        this.setSize(348, 253); // widht et height de la fenêtre
        this.setTitle("Bonjour - Filière C.C.Informatique"); // titre de la fenêtre
        this.setForeground(Color.black); // couleur de premier plan de la fenêtre
        button1.setBounds(70, 200, 200, 30); // positionnement du bouton
        button1.setLabel("Validez votre entrée !"); // titre du bouton
        label1.setBounds(24, 115, 50, 23); // positionnement de l'étiquette
        label1.setText("Entrez :"); // titre de l'étiquette
        checkbox1.setBounds(20, 25, 88, 23); // positionnement du CheckBox
        checkbox1.setCheckboxGroup(checkboxGroup1); // ce CheckBox est mis dans le groupe checkboxGroup1
        checkbox1.setLabel("Madame");// titre du CheckBox
        checkbox2.setBounds(20, 55, 108, 23);// positionnement du CheckBox
        checkbox2.setCheckboxGroup(checkboxGroup1);// ce CheckBox est mis dans le groupe checkboxGroup1
        checkbox2.setLabel("Mademoiselle");// titre du CheckBox
        checkbox3.setBounds(20, 85, 88, 23);// positionnement du CheckBox
        checkbox3.setCheckboxGroup(checkboxGroup1);// ce CheckBox est mis dans le groupe checkboxGroup1
        checkbox3.setLabel("Monsieur");// titre du CheckBox
        checkboxGroup1.setSelectedCheckbox(checkbox1);// le CheckBox1 du groupe est coché au départ
        textField1.setBackground(Color.white);// couleur du fond de l'éditeur mono ligne
        textField1.setBounds(82, 115, 198, 23);// positionnement de l'éditeur mono ligne
        textField1.setText("Votre nom ?");// texte de départ de l'éditeur mono ligne
        this.add(checkbox1, null);// ajout dans la fenêtre du CheckBox
        this.add(checkbox2, null);// ajout dans la fenêtre du CheckBox
        this.add(checkbox3, null);// ajout dans la fenêtre du CheckBox
        this.add(button1, null);// ajout dans la fenêtre du bouton
        this.add(textField1, null);// ajout dans la fenêtre de l'éditeur mono ligne
        this.add(label1, null);// ajout dans la fenêtre de l'étiquette
    }
}
