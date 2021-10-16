/** *
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.google.gson.*;


public class ClientThread
        extends Thread {

    private BufferedReader socIn  ;
    private PrintStream socOut;

    private Socket clientSocket;

    private String chatName;

    private String salon = null;

    private HashMap<String,String> listeSalon = new HashMap<>();

    private HashMap<String, ClientThread> clientInfo = new HashMap<>();

    private volatile boolean isRunning = true;

    public String getChatName() {
        return chatName;
    }

    public String getSalon() {
        return salon;
    }

    ClientThread(Socket s) {
        try {
            this.clientSocket = s;
            this.clientInfo = EchoServerMultiThreaded.getClientInfo();
            this.listeSalon = EchoServerMultiThreaded.getSalonInfo();
            socIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            socOut = new PrintStream(s.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * receives a request from client then sends an echo to the client
     *
     * @param clientSocket the client socket
     *
     */
    public void run() {
        try {

            while (isRunning) {
                chatName = "guest"+new Date().getTime();
                clientInfo.put(chatName, this);

                int i = 0;
                for (String key : clientInfo.keySet()) {
                    if (key.equals(chatName)) {
                        System.out.println(chatName + " added at " + (i + 1) + " position");
                    }
                    i++;
                }

                JsonObject json = new JsonObject();
                json.addProperty("type", "nom");
                json.addProperty("msg", chatName);
                socOut.println(json.toString());

                // tell other users about new added user and update their online users list

                /*for (ClientThread client : clientInfo.values()) {
                    String toSend = "client connecté\nnbr de client : " + clientInfo.size() + "\nListe des clients: \n";

                    for (ClientThread client1 : clientInfo.values()) {
                        toSend +=client1.chatName+"\n";
                    }
                    client.socOut.print(toSend);
                }*/


                while (true) {
                    String line = socIn.readLine();

                    // a la reception d'un nouveau message on crée un nouveau thread pour repondre
                    new Thread(){
                        public void run(){
                            try{
                                System.out.println(line);
                                JsonObject json = new JsonParser().parse(line).getAsJsonObject();

                                switch(json.get("type").getAsString()){
                                    case "listeSalon":
                                        json = new JsonObject();
                                        json.addProperty("type", "listeSalon");

                                        JsonArray jsonArray = new JsonArray();
                                        for(String salon : listeSalon.keySet()){
                                            jsonArray.add(salon);
                                        }

                                        json.add("msg", jsonArray);

                                        socOut.println(json.toString());
                                        break;
                                    case "creerCanal":
                                        String canal = json.get("msg").getAsString();
                                        if(!listeSalon.containsKey(canal)){
                                            listeSalon.put(canal, "");
                                        }
                                        json = new JsonObject();
                                        json.addProperty("type", "creerCanal");
                                        json.addProperty("msg", "canal créé : "+canal);
                                        socOut.println(json.toString());
                                        break;
                                    case "choisirCanal":
                                        canal = json.get("msg").getAsString();
                                        json = new JsonObject();
                                        if(listeSalon.containsKey(canal)){
                                            salon = canal;
                                            json.addProperty("type", "choisirCanal");
                                            json.addProperty("msg", "vous faites partie du salon "+salon);
                                        }else{
                                            json.addProperty("type", "error");
                                            json.addProperty("msg", "le canal n'existe pas");
                                        }
                                        socOut.println(json.toString());
                                        break;
                                    case "msg":
                                        if(salon==null){
                                            json = new JsonObject();
                                            json.addProperty("type", "error");
                                            json.addProperty("msg", "vous ne faites pas partie d'un salon");
                                            socOut.println(json.toString());
                                        } else {
                                            String msg = json.get("msg").getAsString();
                                            listeSalon.put(salon, listeSalon.get(salon)+chatName+" : "+msg+"\n");
                                            for (ClientThread client : clientInfo.values()) {
                                                if(client.getSalon().equals(salon)){
                                                    json = new JsonObject();
                                                    json.addProperty("type", "msg");
                                                    json.addProperty("msg", listeSalon.get(salon));
                                                    client.socOut.println(json.toString());
                                                }
                                            }
                                        }
                                        break;
                                    case "nom":
                                        String msg = json.get("msg").getAsString();
                                        if(clientInfo.containsKey(msg)){
                                            json = new JsonObject();
                                            json.addProperty("type", "error");
                                            json.addProperty("msg", "Ce nom d'utilisateur est deja pris");
                                            socOut.println(json.toString());
                                        } else {
                                            clientInfo.remove(chatName);
                                            chatName = msg;
                                            clientInfo.put(chatName, ClientThread.this);
                                            //socOut.println("votre nouveau nom d'utilisateur est :"+ chatName);
                                            json = new JsonObject();
                                            json.addProperty("type", "nom");
                                            json.addProperty("msg", chatName);
                                            socOut.println(json.toString());
                                        }
                                        break;
                                    default:
                                        socOut.println("type non reconnu");
                                        break;
                                }
                            }catch (Exception e){
                                System.err.println("Error in EchoServer:" + e);
                            }
                        }
                    }.start();
                }
            }
            socOut.close();
            socIn.close();
            clientSocket.close();
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

}
