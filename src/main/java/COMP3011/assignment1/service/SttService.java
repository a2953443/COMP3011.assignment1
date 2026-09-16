package COMP3011.assignment1.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class SttService {

    private final WebClient webClient;
    private final StatsService statsService;

    public SttService(WebClient webClient, StatsService statsService) {
        this.webClient = webClient;
        this.statsService = statsService;
    }

    public String transcribe(MultipartFile audioFile) throws IOException {
        // Read the API key fresh from the environment at call time
        String apiKey = System.getenv("OPENAI_API_KEY");

        // Build a multipart request body matching what OpenAI's API expects
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("model", "gpt-4o-mini-transcribe");
        body.add("file", new ByteArrayResource(audioFile.getBytes()) {
            @Override
            public String getFilename() {
                return "recording.webm"; // filename to infer the audio format
            }
        });

        // Send the request to OpenAI and block until the response comes back as a Map
        Map<String, Object> response = webClient.post()
                .uri("/v1/audio/transcriptions")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        Object usageObj = response.get("usage");
        if (usageObj instanceof Map<?, ?> usage) {
            Object inputTokens = usage.get("input_tokens");
            Object outputTokens = usage.get("output_tokens");

            long input = inputTokens instanceof Number ? ((Number) inputTokens).longValue() : 0;
            long output = outputTokens instanceof Number ? ((Number) outputTokens).longValue() : 0;

            statsService.addUsage(input, output);
        }

        return (String) response.get("text");
    }
}