package com.example.interviewsimulator.controller;

import com.example.interviewsimulator.model.AnswerRequest;
import com.example.interviewsimulator.model.GeminiResponse;
import com.example.interviewsimulator.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InterviewControllerTest {

    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private InterviewController interviewController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(interviewController).build();
        objectMapper = new ObjectMapper();
        session = new MockHttpSession();
    }

    @Test
    void testStartInterview() throws Exception {
        String expectedQuestion = "What is your experience with Java?";
        when(geminiService.askGemini(anyString())).thenReturn(expectedQuestion);

        mockMvc.perform(get("/api/interview/startInterview")
                .param("role", "backend developer")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedQuestion));
    }

    @Test
    void testSubmitAnswer() throws Exception {
        AnswerRequest request = new AnswerRequest();
        request.setAnswer("I have 3 years of experience with Java");
        request.setQuestion("What is your experience with Java?");
        request.setRole("backend developer");

        String mockResponse = "{\"score\": 8, \"strengths\": [\"Clear answer\"], \"weaknesses\": [\"Could be more specific\"], \"feedback\": \"Good response\"}";
        when(geminiService.askGemini(anyString())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/interview/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .session(session))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.score").value(8))
                .andExpected(jsonPath("$.strengths[0]").value("Clear answer"))
                .andExpected(jsonPath("$.weaknesses[0]").value("Could be more specific"))
                .andExpected(jsonPath("$.feedback").value("Good response"));
    }

    @Test
    void testGetNextQuestion() throws Exception {
        String expectedQuestion = "Tell me about a challenging project you worked on";
        when(geminiService.askGemini(anyString())).thenReturn(expectedQuestion);

        mockMvc.perform(get("/api/interview/nextQuestion")
                .param("role", "software engineer")
                .param("questionIndex", "1")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedQuestion));
    }

    @Test
    void testGetScore() throws Exception {
        // Setup session with feedback
        GeminiResponse feedback1 = new GeminiResponse(8, Arrays.asList("Clear"), Arrays.asList("Too short"), "Good");
        GeminiResponse feedback2 = new GeminiResponse(7, Arrays.asList("Detailed"), Arrays.asList("Unclear"), "Okay");
        session.setAttribute("feedbackList", Arrays.asList(feedback1, feedback2));

        mockMvc.perform(get("/api/interview/score")
                .session(session))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.score").value(15))
                .andExpected(jsonPath("$.totalQuestions").value(2))
                .andExpected(jsonPath("$.maxScore").value(20));
    }

    @Test
    void testSubmitAnswerWithInvalidJSON() throws Exception {
        AnswerRequest request = new AnswerRequest();
        request.setAnswer("Test answer");
        request.setQuestion("Test question");
        request.setRole("test role");

        // Mock invalid JSON response from Gemini
        when(geminiService.askGemini(anyString())).thenReturn("Invalid JSON response");

        mockMvc.perform(post("/api/interview/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .session(session))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.score").value(5))
                .andExpected(jsonPath("$.feedback").value("Please try to be more specific in your answer."));
    }

    @Test
    void testSubmitAnswerWithException() throws Exception {
        AnswerRequest request = new AnswerRequest();
        request.setAnswer("Test answer");
        request.setQuestion("Test question");
        request.setRole("test role");

        // Mock exception from Gemini service
        when(geminiService.askGemini(anyString())).thenThrow(new RuntimeException("Service unavailable"));

        mockMvc.perform(post("/api/interview/submitAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .session(session))
                .andExpect(status().isInternalServerError())
                .andExpected(jsonPath("$.score").value(0))
                .andExpected(jsonPath("$.feedback").value("Sorry, there was a technical issue. Please try again."));
    }
}