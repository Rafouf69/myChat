/** *
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class EchoServerMultiThreaded {

    private static HashMap<String, ClientThread> clientInfo = new HashMap<>();

    public static HashMap<String, ClientThread> getClientInfo() {
        return clientInfo;
    }

    private static File history = new File("./history.json");

    private static HashMap<String, String> listeSalon = new HashMap<>();

    public static HashMap<String, String> getSalonInfo() {
        return listeSalon;
    }

    /**
     * main method
     *
     * @param EchoServer port
     *
     *
     */
    public static void main(String args[]) {
        ServerSocket listenSocket;

        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        try {
            String content = Files.readString(Path.of("target/classes/history.json"));

            JsonObject json = new JsonParser().parse(content).getAsJsonObject();

            JsonArray jsonListeSalon = (JsonArray) json.get("listeSalon");

            for(JsonElement element: jsonListeSalon){
                JsonObject jsonSalon = element.getAsJsonObject();
                String nom = jsonSalon.get("nom").getAsString();
                String historique = jsonSalon.get("historique").getAsString();

                listeSalon.put(nom,historique);
            }

            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
                ClientThread ct = new ClientThread(clientSocket);
                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}



