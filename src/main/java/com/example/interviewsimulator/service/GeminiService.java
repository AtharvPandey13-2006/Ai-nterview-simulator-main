package com.example.interviewsimulator.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String modelName;

    private Client client;

    @PostConstruct
    public void init() {
        try {
            System.setProperty("GOOGLE_API_KEY", apiKey);
            client = new Client();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Gemini AI client", e);
        }
    }

    public String askGemini(String userInput) {
        try {
            GenerateContentResponse response = client.models.generateContent(
                modelName,
                userInput,
                null
            );
            return response.text();
        } catch (Exception e) {
            return "Error communicating with Gemini AI: " + e.getMessage();
        }
    }
}
