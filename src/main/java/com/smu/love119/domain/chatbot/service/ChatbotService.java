package com.smu.love119.domain.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.smu.love119.domain.weather.service.WeatherService;

import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final WeatherService weatherService;

    @Autowired
    public ChatbotService(WebClient.Builder webClientBuilder, WeatherService weatherService) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.weatherService = weatherService;
    }

    public String askChatGPT(String mbti, String questionType, String myMbti) {
        if (!isValidMBTI(mbti)) {
            return "올바른 MBTI를 입력하세요.";
        }

        String weatherInfo = weatherService.getWeatherInfo(); // 날씨 정보 가져오기
        String prompt = createPrompt(mbti, questionType, myMbti, weatherInfo); // user의 myMbti 정보도 포함하여 프롬프트 생성
        System.out.println("ChatGPT로 보낼 프롬프트: " + prompt);

        Mono<String> response = webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createChatRequest("gpt-4", prompt))
                .retrieve()
                .bodyToMono(String.class);

        return response.block();
    }


    private String createPrompt(String mbti, String question, String myMbti, String weatherInfo) {
        // 유저가 입력한 질문을 그대로 프롬프트에 포함하여 전달
        String prompt = String.format(
                "제 MBTI는 %s입니다. 저는 MBTI가 %s인 사람에게 관심이 있는데 이 사람에 대해서 이런 게 궁금해요. : \"%s\". 가장 뒤에 현재 날씨를 첨부했어요. 현재 날씨, 제 mbti, 상대방의 mbti를 모두 고려해서 간결하게 답변해주세요. 현재 날씨 정보: %s",
                myMbti, mbti, question, weatherInfo
        );
        return prompt;
    }
    public String generateAdviceForPost(String postTitle, String postContent, String postMbti, String myMbti) {
        String prompt = String.format("제목: %s\n내용: %s\n해당 사용자가 궁금해하는 MBTI: %s\n작성자의 MBTI: %s\n위의 게시글에 대해 30자 이내로 간단한 조언을 주세요.",
                postTitle, postContent, postMbti, myMbti);
        System.out.println("ChatGPT로 보낼 프롬프트: " + prompt);

        Mono<String> response = webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createChatRequest("gpt-4", prompt))
                .retrieve()
                .bodyToMono(String.class);

        String adviceJson = response.block();
        String adviceContent = "";

        try {
            // JSON 파싱을 위한 ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(adviceJson);

            // JSON 응답에서 content 값 추출
            adviceContent = rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .replaceAll("\"", "")  // 불필요한 따옴표 제거
                    .trim();               // 양쪽 공백 제거

        } catch (Exception e) {
            System.err.println("Error parsing advice JSON: " + e.getMessage());
        }

        // 필요 시 길이 제한
        return adviceContent.length() > 255 ? adviceContent.substring(0, 255) : adviceContent;
    }


    private Map<String, Object> createChatRequest(String model, String userMessage) {
        return Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.7
        );
    }

    private boolean isValidMBTI(String mbti) {
        return List.of("INTJ", "INTP", "ENTJ", "ENTP", "INFJ", "INFP", "ENFJ", "ENFP", "ISTJ", "ISFJ", "ESTJ", "ESFJ", "ISTP", "ISFP", "ESTP", "ESFP").contains(mbti.toUpperCase());
    }

}
