package ru.itis.jlmqwithstomp.dto;

import lombok.*;
import ru.itis.jlmqwithstomp.models.JlmqMessage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class JlmqMessageDto {
    private String command;
    private String queueName;
    private String messageId;
    private String body;

    public static JlmqMessageDto from(JlmqMessage message, String command) {
        return JlmqMessageDto.builder()
                .command(command)
                .body(message.getBody())
                .messageId(message.getMessageId())
                .queueName(message.getQueue().getName())
                .build();
    }
}
