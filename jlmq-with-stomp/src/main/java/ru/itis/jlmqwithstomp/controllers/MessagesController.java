package ru.itis.jlmqwithstomp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import ru.itis.jlmqwithstomp.dto.JlmqMessageDto;
import ru.itis.jlmqwithstomp.models.JlmqMessage;
import ru.itis.jlmqwithstomp.services.MessageService;

import java.util.List;

@Controller
public class MessagesController {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;


    @SneakyThrows
    @MessageMapping("/tasks")
    public void receiveMessageFromClient(Message<JlmqMessageDto> message) {
        JlmqMessageDto messageDto = message.getPayload();
        System.out.println("received : " + messageDto);
        switch (messageDto.getCommand()) {
            case "send" -> {
                JlmqMessage jlmqMessage = messageService.send(messageDto.getQueueName(), messageDto.getBody());
                send(jlmqMessage, messageDto.getQueueName());
            }
            case "accepted" -> {
                messageService.accepted(messageDto.getMessageId());
            }
            case "completed" -> {
                messageService.completed(messageDto.getMessageId());
            }
            default -> {
                throw new IllegalArgumentException("command not supported");
            }
        }
    }

    @SneakyThrows
    @SendTo("/tasks.{queueName}")
    public void send(JlmqMessage jlmqMessage, @DestinationVariable String queueName) {
        JlmqMessageDto message = JlmqMessageDto.from(jlmqMessage, "receive");

        System.out.println("sending : " + message);

        template.convertAndSend("/tasks." + queueName, message);
    }

    @SubscribeMapping("/tasks.{queueName}")
    @SendTo("/tasks.{queueName}")
    public void subscribe(@DestinationVariable String queueName) {
        System.out.println("subscribed to " + queueName);
        List<JlmqMessage> jlmqMessages = messageService.findAll(queueName);
        jlmqMessages.forEach(message -> send(message, queueName));
    }

}
