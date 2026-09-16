package COMP3011.assignment1.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // handle audio file
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TranscriptController {

    @PostMapping("/transcribe") // handles POST requests to /api/transcribe
    public Map<String, String> transcribe(@RequestParam("audio") MultipartFile audioFile) {
        // audioFile now holds the uploaded audio data, matched by the "audio" key used in the frontend's FormData

        // Return a fake response for now - Spring automatically converts this Map into JSON
        return Map.of("text", "This is a fake transcription for testing.");
    }
}