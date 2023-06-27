package Servers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SongInfoServlet")
public class SongInfoController {

    @PostMapping
    public String searchSong(@RequestBody String searchQuery) {
        try {
            String response = SongInfoServer.searchSong(searchQuery);
            return parseSongInfo(response);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    private String parseSongInfo(String response) {
        // Parse the JSON response to extract song information
        // Implement your logic to extract the relevant details from the JSON response
        // and construct the desired output

        // Example:
        String songTitle = "Example Song Title";
        String artistName = "Example Artist Name";
        String youtubeVideoId = "abc123"; // Example YouTube video ID

        // Construct the response with YouTube link
        return "<a href=\"https://www.youtube.com/watch?v=" + youtubeVideoId + "\">" +
                songTitle + " by " + artistName + "</a>";
    }
}
