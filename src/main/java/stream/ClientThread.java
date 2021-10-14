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
import java.util.HashMap;
import com.google.gson.*;


public class ClientThread
        extends Thread {

    private BufferedReader socIn  ;
    private PrintStream socOut;

    private Socket clientSocket;

    private String chatName;

    private String salon = null;

    private ArrayList<String> listeSalon = new ArrayList<>();

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
                socOut.println("Enter your name : ");

                chatName = socIn.readLine();

                boolean result = clientInfo.containsKey(chatName);

                if (result) {
                    socOut.println("This username is already present : ");
                    continue;
                }

                clientInfo.put(chatName, this);

                int i = 0;
                for (String key : clientInfo.keySet()) {
                    if (key.equals(chatName)) {
                        System.out.println(chatName + " added at " + (i + 1) + " position");
                    }
                    i++;
                }

                // tell other users about new added user and update their online users list

                for (ClientThread client : clientInfo.values()) {
                    String toSend = "client connecté\nnbr de client : " + clientInfo.size() + "\nListe des clients: \n";

                    for (ClientThread client1 : clientInfo.values()) {
                        toSend +=client1.chatName+"\n";
                    }
                    client.socOut.print(toSend);
                }


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
                                        socOut.println("liste des salons :");
                                        socOut.println(listeSalon.toString());
                                        break;
                                    case "canal":
                                        String canal = json.get("msg").getAsString();
                                        if(!listeSalon.contains(canal)){
                                            listeSalon.add(canal);
                                            salon = canal;
                                        }else{
                                            salon = canal;
                                        }
                                        socOut.println("vous faites partie du salon "+canal);
                                        break;
                                    case "msg":
                                        if(salon==null){
                                            socOut.println("vous ne faites pas partie d'un salon");
                                        } else {
                                            String msg = json.get("msg").getAsString();
                                            for (ClientThread client : clientInfo.values()) {
                                                if(client.getSalon().equals(salon) && !client.getChatName().equals(chatName)){
                                                    client.socOut.println(chatName+" : "+msg);
                                                }
                                            }
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
