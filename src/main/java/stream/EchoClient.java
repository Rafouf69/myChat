/** *
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;
import java.lang.Thread;
import com.google.gson.*;

public class EchoClient {

    /**
     * main method accepts a connection, receives a message from client then
     * sends an echo to the client
     *
     */
    public static void main(String[] args) throws IOException {

        final Socket echoSocket;
        final PrintStream socOut;
        final BufferedReader stdIn;
        final BufferedReader socIn;

        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            echoSocket = new Socket(args[0], new Integer(args[1]).intValue());
            socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            socOut = new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            Thread envoyer = new Thread(new Runnable() {
                String msg;
                String type;
                @Override
                public void run() {
                    try{
                        while (true) {
                            JsonObject json = new JsonObject();
                            type = stdIn.readLine();
                            switch(type){
                                //liste Salon
                                case "1":
                                    json.addProperty("type", "listeSalon");
                                    break;
                                //joindre salon
                                case "2":
                                    json.addProperty("type", "canal");
                                    System.out.println("Entrez le nom du salon que vous voulez joindre");
                                    msg = stdIn.readLine();
                                    json.addProperty("msg", msg);
                                    break;
                                //envoyer message
                                case "3":
                                    json.addProperty("type", "msg");
                                    System.out.println("Entrez le message que vous voulez envoyer");
                                    msg = stdIn.readLine();
                                    json.addProperty("msg", msg);
                                    break;
                                case "4":
                                    json.addProperty("type", "nom");
                                    System.out.println("Entrez votre nom d'utilisateur");
                                    msg = stdIn.readLine();
                                    json.addProperty("msg", msg);
                                    break;
                                default:
                                    System.out.println("Vous devez entrer un code : \n1 pour liste de canal\n2 pour joindre un canal\n3 pour envoyer un msg\n4 pour envoyer votre nom d'utilisateur");
                                    break;
                            }
                            if(json.size()>0){
                                socOut.println(json.toString());
                                socOut.flush();
                            }
                        }
                    }catch(Exception e){
                        System.err.println("Error in EchoServer:" + e);
                    }
                }
            });
            envoyer.start();

            Thread recevoir = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = socIn.readLine();
                        while (msg != null) {
                            System.out.println("Serveur : " + msg);
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
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to:" + args[0]);
            System.exit(1);
        }
    }
}
