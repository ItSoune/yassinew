package client;

import java.io.*;
import java.net.*;

class SaisieUtilisateurThread extends Thread {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader stdIn;

    public SaisieUtilisateurThread(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                if ("exit".equalsIgnoreCase(userInput)) {
                    System.out.println("Vous avez quitté la conversation. Merci pour votre utilisation !");
                    out.println("exit");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
