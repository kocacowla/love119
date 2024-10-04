package com.smu.love119.domain.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;

    // 유효한 MBTI 리스트
    private static final List<String> VALID_MBTIS = Arrays.asList(
            "INTJ", "INTP", "ENTJ", "ENTP",
            "INFJ", "INFP", "ENFJ", "ENFP",
            "ISTJ", "ISFJ", "ESTJ", "ESFJ",
            "ISTP", "ISFP", "ESTP", "ESFP"
    );

    @Autowired
    public ChatbotService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
    }

    // MBTI 검증 후 ChatGPT API 호출
    public String askChatGPT(String mbti, String questionType) {
        if (!isValidMBTI(mbti)) {
            return "올바른 MBTI를 입력하세요.";
        }

        String prompt = createPrompt(mbti, questionType);

        // ChatGPT API 요청 설정
        Mono<String> response = webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createChatRequest("gpt-4", prompt))
                .retrieve()
                .bodyToMono(String.class);

        return response.block(); // 비동기 응답을 동기식으로 처리
    }

    // MBTI가 유효한지 검증하는 메서드
    private boolean isValidMBTI(String mbti) {
        return VALID_MBTIS.contains(mbti.toUpperCase());
    }

    // ChatGPT API 요청에 사용할 메시지 형식 생성
    private Map<String, Object> createChatRequest(String model, String userMessage) {
        return Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.7
        );
    }

    // 프롬프트 생성
    private String createPrompt(String mbti, String questionType) {
        switch (questionType.toLowerCase()) {
            case "이상형":
                return String.format("MBTI가 %s인 사람에게 어울리는 이상형은 무엇인가요? 친한 친구가 이야기 해주는 것 처럼 친근하고 간결하게 대답해 주세요.", mbti);
            case "데이트 코스":
                return String.format("%s 유형이 좋아하는 데이트 코스는? 친한 친구가 이야기 해주는 것 처럼 친근하고 간결하게 대답해 주세요.", mbti);
            case "선호하는 연락방식":
                return String.format("%s 유형의 사람이 선호하는 연락 방식은? 친한 친구가 이야기 해주는 것 처럼 친근하고 간결하게 대답해 주세요.", mbti);
            case "좋아하는 플러팅":
                return String.format("%s 유형이 좋아하는 플러팅은? 친한 친구가 이야기 해주는 것 처럼 친근하고 간결하게 대답해 주세요.", mbti);
            case "싫어하는 행동":
                return String.format("%s 유형이 싫어하는 행동은 무엇인가요? 친한 친구가 이야기 해주는 것 처럼 친근하고 간결하게 대답해 주세요.", mbti);
            default:
                return "알 수 없는 질문 유형입니다.";
        }
    }
}
