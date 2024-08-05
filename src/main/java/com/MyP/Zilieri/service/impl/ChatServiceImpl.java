package com.MyP.Zilieri.service.impl;

import com.MyP.Zilieri.entities.Message;
import com.MyP.Zilieri.repository.MessageRepository;
import com.MyP.Zilieri.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;

    public Message saveMessage(String sender, String receiver, String content) {
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();
        return messageRepository.save(message);
    }

    public List<Message> getMessages(String sender, String receiver) {
        return messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(sender, receiver, receiver, sender);
    }

    public List<Message> getMessagesByReceiverAndUnread(String receiver) {
        return messageRepository.findByReceiverAndIsReadFalse(receiver);
    }

}