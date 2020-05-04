package ru.itis.jlmqwithstompsdk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.websocket.Session;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class JlmqMessage {
    private String command;
    private String queueName;
    private String messageId;
    private String body;

}
