package com.MyP.Zilieri.repository;

import com.MyP.Zilieri.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(String sender, String receiver, String receiver2, String sender2);
    List<Message> findByReceiverAndIsReadFalse(String receiver);
}
