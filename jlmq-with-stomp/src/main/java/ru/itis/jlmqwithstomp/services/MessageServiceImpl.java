package ru.itis.jlmqwithstomp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.jlmqwithstomp.models.JlmqMessage;
import ru.itis.jlmqwithstomp.models.Queue;
import ru.itis.jlmqwithstomp.repositories.MessageRepository;
import ru.itis.jlmqwithstomp.repositories.QueueRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public JlmqMessage send(String queueName, Object body) {

        Optional<Queue> queue = queueRepository.findByName(queueName);
        if (queue.isPresent()) {

            JlmqMessage message = JlmqMessage.builder()
                    .messageId(UUID.randomUUID().toString())
                    .queue(queue.get())
                    .body(objectMapper.writeValueAsString(body))
                    .status(JlmqMessage.Status.RECEIVED)
                    .build();

            return messageRepository.save(message);
        }
        throw new IllegalArgumentException("no queue wih name " + queueName);
    }

    @Override
    public List<JlmqMessage> findAll(String queueName) {
        Optional<Queue> queue = queueRepository.findByName(queueName);
        if (queue.isPresent()) {
            return messageRepository.findAllByQueueAndStatus(queue.get(), JlmqMessage.Status.RECEIVED);
        }

        throw new IllegalArgumentException("no queue wih name " + queueName);
    }

    @Override
    public Optional<JlmqMessage> findMessageToSend(String queueName) {
        Optional<Queue> queueOptional = queueRepository.findByName(queueName);
        if (queueOptional.isPresent()) {
            return messageRepository.findFirstByQueueAndStatus(queueOptional.get(), JlmqMessage.Status.RECEIVED);
        }
        throw new IllegalArgumentException("no queue with name " + queueName);
    }

    @Override
    public void accepted(String messageId) {
        messageRepository.findByMessageId(messageId)
                .ifPresent(message -> {
                    message.setStatus(JlmqMessage.Status.ACCEPTED);
                    messageRepository.save(message);
                });
    }

    @Override
    public void completed(String messageId) {
        messageRepository.findByMessageId(messageId)
                .ifPresent(message -> {
                    message.setStatus(JlmqMessage.Status.COMPLETED);
                    messageRepository.save(message);
                });
    }
}
