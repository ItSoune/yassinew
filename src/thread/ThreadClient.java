package thread;


import server.Serveur;

import java.io.*;
        import java.net.*;

public class ThreadClient extends Thread {
    private Socket socket;
    private PrintWriter out;

    public ThreadClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrant = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String pseudonyme = entrant.readLine();

            while (!Serveur.addPseudonym(pseudonyme)) {
                out.println("Pseudonyme déjà utilisé. Veuillez en choisir un autre.");
                pseudonyme = entrant.readLine();
            }
            out.println("Pseudonyme accepté!");

            String message;
            while (true) {
                message = entrant.readLine();
                if (message.startsWith("exit")) {
                    Serveur.diffuserMessage(pseudonyme , socket, true);
                    this.socket.close(); // Close the socket for this client
                    break;

                }
                Serveur.diffuserMessage(pseudonyme + ": " + message, socket, false);
            }
        } catch (SocketException e) {
            System.out.println("Un client a perdu la connexion.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
