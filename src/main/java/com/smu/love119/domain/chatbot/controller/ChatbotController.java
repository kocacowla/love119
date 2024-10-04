package com.smu.love119.domain.chatbot.controller;

import com.smu.love119.domain.chatbot.dto.ChatRequest;
import com.smu.love119.domain.chatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chatbot/ask")
    public String askQuestion(@RequestBody ChatRequest chatRequest) {
        // ChatRequest에서 MBTI와 질문 유형을 받아 ChatGPT 서비스 호출
        String response = chatbotService.askChatGPT(chatRequest.getMbti(), chatRequest.getQuestionType());

        // API의 응답 결과 반환
        return response;
    }
}
