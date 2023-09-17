package client;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Entrez votre pseudonyme: ");
            String pseudonyme = stdIn.readLine();
            out.println(pseudonyme);

            String serverResponse = in.readLine();
            while ("Pseudonyme déjà utilisé. Veuillez en choisir un autre.".equals(serverResponse)) {
                System.out.println(serverResponse);
                System.out.print("Entrez votre pseudonyme: ");
                pseudonyme = stdIn.readLine();
                out.println(pseudonyme);
                serverResponse = in.readLine();
            }


            MessageServeurThread messageThread = new MessageServeurThread(socket);
            SaisieUtilisateurThread saisieThread = new SaisieUtilisateurThread(socket);

            messageThread.start();
            saisieThread.start();

            // Attendez que les threads se terminent
            messageThread.join();
            saisieThread.join();

        } catch (SocketException e) {
            System.out.println("Déconnecté du serveur.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}





