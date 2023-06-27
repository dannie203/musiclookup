package Clients;

import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientController {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final String HTML_FILE_PATH = " Z:\\musiclookup\\src\\main\\java\\Clients\\Index.html"; // Update the path to "/index.html"

    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server");

            // Send HTTP GET request to the server
            sendGetRequest(socket, HTML_FILE_PATH);

            // Receive and display the response from the server
            String response = receiveResponse(socket);
            System.out.println("Response from server:");
            System.out.println(response);

            // Open the response in a web browser
            openInBrowser(response);

            // Close the connection
            socket.close();
            System.out.println("Disconnected from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendGetRequest(Socket socket, String path) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        String request = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + SERVER_ADDRESS + "\r\n" +
                "\r\n";
        outputStream.write(request.getBytes());
        outputStream.flush();
        System.out.println("Sent GET request: " + request);
    }

    private static String receiveResponse(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }
        System.out.println("Received response from server");
        return response.toString();
    }

    private static void openInBrowser(String htmlContent) {
        try {
            File tempFile = File.createTempFile("temp", ".html");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(htmlContent);
            writer.close();
            Desktop.getDesktop().browse(tempFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
