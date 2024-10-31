package com.smu.love119.domain.chatbot.controller;

import com.smu.love119.domain.chatbot.dto.ChatRequest;
import com.smu.love119.domain.chatbot.service.ChatbotService;
import com.smu.love119.domain.user.dto.UserDTO;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.service.UserService; // User 엔티티를 불러오기 위한 서비스 주입 필요
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final UserService userService; // User 엔티티 조회를 위한 서비스 주입

    @Autowired
    public ChatbotController(ChatbotService chatbotService, UserService userService) {
        this.chatbotService = chatbotService;
        this.userService = userService;
    }

    @PostMapping("/chatbot/ask")
    public String askQuestion(@RequestBody ChatRequest chatRequest, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return "사용자를 찾을 수 없습니다.";
        }

        // User 엔티티를 UserDTO로 변환하여 myMbti 정보 가져오기
        UserDTO userDTO = UserDTO.fromEntity(user);
        String response = chatbotService.askChatGPT(chatRequest.getMbti(), chatRequest.getQuestion(), userDTO.getMyMbti());
        return response;
    }
}
