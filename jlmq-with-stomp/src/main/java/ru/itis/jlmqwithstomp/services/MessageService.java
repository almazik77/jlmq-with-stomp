package ru.itis.jlmqwithstomp.services;

import ru.itis.jlmqwithstomp.models.JlmqMessage;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    JlmqMessage send(String queueName, Object body);

    List<JlmqMessage> findAll(String queueName);

    Optional<JlmqMessage> findMessageToSend(String queueName);

    void accepted(String messageId);

    void completed(String messageId);
}
