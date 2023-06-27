package Servers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLEncoder;

public class SongInfoServer {
    private static final int PORT = 8080;
    private static final String API_KEY = "9a28b8a5ae0f34f3fc188241b57c029d";
    private static final String INDEX_FILE_PATH = "Z:\\musiclookup\\src\\main\\java\\Clients\\Index.html";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());

                    Thread clientThread = new Thread(() -> handleClient(clientSocket));
                    clientThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {
            String request = reader.readLine();
            System.out.println("Received request: " + request);

            if (request.startsWith("GET /index.html")) {
                sendFileResponse(outputStream, INDEX_FILE_PATH);
            } else if (request.startsWith("GET /search?query=")) {
                String searchQuery = extractSearchQuery(request);
                String response = searchSong(searchQuery);
                sendHtmlResponse(outputStream, response);
            } else {
                sendErrorResponse(outputStream, 404, "Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String extractSearchQuery(String request) {
        String query = "";
        int startIndex = request.indexOf("?query=");
        if (startIndex != -1) {
            int endIndex = request.indexOf(" ", startIndex);
            if (endIndex != -1) {
                query = request.substring(startIndex + 7, endIndex);
            }
        }
        return query;
    }

    private static void sendFileResponse(OutputStream outputStream, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);

        outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
        outputStream.write(("Content-Length: " + fileBytes.length + "\r\n").getBytes());
        outputStream.write("Content-Type: text/html\r\n".getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write(fileBytes);
        outputStream.flush();

        System.out.println("Sent file response: " + filePath);
    }

    private static void sendErrorResponse(OutputStream outputStream, int statusCode, String statusMessage) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n\r\n";
        outputStream.write(response.getBytes());
        outputStream.flush();

        System.out.println("Sent error response: " + statusCode + " " + statusMessage);
    }

    private static void sendHtmlResponse(OutputStream outputStream, String jsonResponse) throws IOException {
        String htmlResponse = constructHtmlResponse(jsonResponse);

        byte[] responseBytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
        outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
        outputStream.write(("Content-Length: " + responseBytes.length + "\r\n").getBytes());
        outputStream.write("Content-Type: text/html\r\n".getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write(responseBytes);
        outputStream.flush();

        System.out.println("Sent HTML response");
    }

    private static String constructHtmlResponse(String htmlRespond) {
        // Implement your logic to extract the relevant details from the JSON response
        // and construct the HTML response accordingly
        // Here's an example of constructing a simple HTML response with a YouTube link

        // Extract the song details from the JSON response
        String songTitle = "Example Song Title";
        String artistName = "Example Artist Name";
        String youtubeVideoId = "abc123";  // Example YouTube video ID

        // Construct the HTML response
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Song Info Client</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Song Info</h1>\n" +
                "<p>Song: " + songTitle + "</p>\n" +
                "<p>Artist: " + artistName + "</p>\n" +
                "<p>YouTube Link: <a href=\"https://www.youtube.com/watch?v=" + youtubeVideoId + "\">Watch on YouTube</a></p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String searchSong(String query) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String apiUrl = "http://ws.audioscrobbler.com/2.0/?method=track.search&track=" + encodedQuery +
                "&api_key=" + API_KEY + "&format=json";

        // Implement your logic to perform the song search using the Last.fm API or any other desired method
        // and return the JSON response
        return ""; // Placeholder return value
    }
}
