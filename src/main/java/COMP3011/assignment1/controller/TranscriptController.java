package COMP3011.assignment1.controller;

import COMP3011.assignment1.service.SttService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TranscriptController {

    private final SttService sttService;

    // Constructor injection - Spring supplies the SttService bean
    public TranscriptController(SttService sttService) {
        this.sttService = sttService;
    }

    @PostMapping("/transcribe")
    public Map<String, String> transcribe(@RequestParam("audio") MultipartFile audioFile) throws IOException {
        String text = sttService.transcribe(audioFile);
        return Map.of("text", text);
    }
}