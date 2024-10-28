package com.smu.love119.domain.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.smu.love119.domain.weather.service.WeatherService;

import java.util.Arrays;
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

    public String askChatGPT(String mbti, String questionType) {
        if (!isValidMBTI(mbti)) {
            return "올바른 MBTI를 입력하세요.";
        }

        String weatherInfo = weatherService.getWeatherInfo(); // 날씨 정보 가져오기
        String prompt = createPrompt(mbti, questionType, weatherInfo); // 날씨 정보와 함께 프롬프트 생성
        System.out.println("ChatGPT로 보낼 프롬프트: " + prompt);

        Mono<String> response = webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createChatRequest("gpt-4", prompt))
                .retrieve()
                .bodyToMono(String.class);

        return response.block();
    }

    private String createPrompt(String mbti, String questionType, String weatherInfo) {
        String basePrompt = "";

        switch (questionType.toLowerCase()) {
            case "이상형":
                basePrompt = String.format("저는 MBTI가 %s인 사람에게 관심이 있어요. 그 사람에게 어울리는 이상형은 무엇인가요?", mbti);
                break;
            case "데이트 코스":
                basePrompt = String.format("%s 유형이 좋아하는 데이트 코스는?", mbti);
                break;
            case "선호하는 연락방식":
                basePrompt = String.format("%s 유형의 사람이 선호하는 연락 방식은?", mbti);
                break;
            case "좋아하는 플러팅":
                basePrompt = String.format("%s 유형이 좋아하는 플러팅은?", mbti);
                break;
            case "싫어하는 행동":
                basePrompt = String.format("%s 유형이 싫어하는 행동은 무엇인가요?", mbti);
                break;
            default:
                basePrompt = "알 수 없는 질문 유형입니다.";
        }

        // 날씨 정보 추가
        return basePrompt + " 친한 친구가 대답해주는 것 처럼 친근하고 간결하게 대답해주세요. 또한 현재 날씨 기반으로 대답해주세요. 현재 날씨는 다음과 같습니다: " + weatherInfo;
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
