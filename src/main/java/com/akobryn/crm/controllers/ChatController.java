package com.akobryn.crm.controllers;

import com.akobryn.crm.websocket.message.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message, @AuthenticationPrincipal Principal principal) {
        message.setSender(principal.getName());
        message.setTimestamp(LocalDateTime.now());

        return message;
    }

    @GetMapping("/chat")
    public String getChat() {
        return "group-chat";
    }
}
