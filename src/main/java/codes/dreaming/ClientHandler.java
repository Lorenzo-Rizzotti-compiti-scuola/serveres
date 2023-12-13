package codes.dreaming;

import codes.dreaming.out.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                System.out.println(line);

                // Analisi di stringa2 per il percorso del file
                if (line.matches("^\\S+ \\S+ \\S+$")) {
                    String[] parts = line.split(" ");
                    String filePath = parts[1];
                    //remove first slash
                    if (filePath.startsWith("/")) {
                        filePath = filePath.substring(1);
                    }
                    Path path = Paths.get(filePath);
                    if (Files.exists(path)) {
                        System.out.println("File trovato: " + filePath);
                        writer.println(new Packet(200, Files.readString(path)).serialize());
                    } else {
                        System.out.println("File non trovato: " + filePath);
                        writer.println(new Packet(404, "File non trovato").serialize());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Errore di I/O nel BufferedReader");
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Errore di I/O nella chiusura del Socket");
                e.printStackTrace();
            }
        }
    }
}
