package server;


import thread.ThreadClient;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Serveur {
    private static final int PORT = 8080;
    private static Set<Socket> clientSockets = new HashSet<>();
    private static Set<String> activePseudonyms = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Un utilisateur a rejoint la conversation ");
                clientSockets.add(clientSocket);
                new ThreadClient(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static synchronized void diffuserMessage(String message, Socket excludeSocket, boolean quit) {
        Iterator<Socket> iterator = clientSockets.iterator();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (quit) {activePseudonyms.remove(message);}

        while (iterator.hasNext()) {
            Socket client = iterator.next();
            if (client.isClosed()) {
                iterator.remove(); // Retirer le client de la liste si son socket est fermé
            } else {
                if (client != excludeSocket) {
                    try {
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        if (!quit)
                            {out.println("[" + currentTime + "] " +message);}
                        else {
                            {
                                out.println("[" + currentTime + "] " + "L'utilisateur " + message + " a quitté la conversation." + " Le nombre d'utilisateurs actuel est : " + activePseudonyms.size());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean addPseudonym(String pseudonyme) {
        return activePseudonyms.add(pseudonyme);
    }
    public static boolean removePseudonym(String pseudonyme) {
        return activePseudonyms.remove(pseudonyme);
    }

}
