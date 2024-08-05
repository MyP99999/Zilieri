package com.MyP.Zilieri.controller;

import com.MyP.Zilieri.dto.ChatMessage;
import com.MyP.Zilieri.entities.Message;
import com.MyP.Zilieri.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public void processMessage(ChatMessage chatMessage) {
        Message savedMessage = chatService.saveMessage(
                chatMessage.getSender(),
                chatMessage.getReceiver(),
                chatMessage.getContent()
        );

        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver(),
                "/topic/messages",
                savedMessage
        );
    }

    @GetMapping("/unread-messages/{receiver}")
    public List<Message> getUnreadMessages(@PathVariable String receiver) {
        return chatService.getMessagesByReceiverAndUnread(receiver);
    }

}