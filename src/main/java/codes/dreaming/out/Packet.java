package codes.dreaming.out;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class Packet {
    private int status;
    private String content;

    public Packet(int status, String content) {
        this.status = status;
        this.content = content;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String serialize() {
        StringBuilder response = new StringBuilder("HTTP/1.1 ");

        // Aggiunta dello stato e del messaggio corrispondente
        if (status == 200) {
            response.append("200 OK");
        } else if (status == 404) {
            response.append("404 Not Found");
        } else {
            // Gestire altri codici di stato se necessario
        }

        response.append("\r\n");

        // Aggiunta dell'intestazione Content-Length
        response.append("Content-Length: ").append(content.length()).append("\r\n");

        // Linea vuota che separa le intestazioni dal corpo
        response.append("\r\n");

        // Aggiunta del contenuto
        response.append(content);

        return response.toString();
    }
}
