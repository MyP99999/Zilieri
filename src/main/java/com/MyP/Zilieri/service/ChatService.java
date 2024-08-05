package com.MyP.Zilieri.service;

import com.MyP.Zilieri.entities.Message;

import java.util.List;

public interface ChatService {
    Message saveMessage(String sender, String receiver, String content);

    List<Message> getMessages(String sender, String receiver);

    List<Message> getMessagesByReceiverAndUnread(String receiver);
}
