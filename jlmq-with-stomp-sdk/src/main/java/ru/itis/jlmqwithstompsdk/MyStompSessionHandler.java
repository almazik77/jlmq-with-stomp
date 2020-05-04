package ru.itis.jlmqwithstompsdk;

import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.function.Consumer;

@Setter
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private String queueName = "default";

    private Consumer<JlmqMessage> consumer;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

        session.subscribe("/tasks." + queueName, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return JlmqMessage.class;
            }

            @SneakyThrows
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("headers : " + headers);
                System.out.println("payload : " + payload);
                JlmqMessage message = (JlmqMessage) payload;
                consumer.accept(message);
                message.setCommand("accepted");
                message.setBody(null);
                message.setQueueName(null);
                session.send("/tasks", message);
//
//                System.out.println("payload : " + payload);
//

                message.setCommand("completed");
                session.send("/tasks", message);

                Thread.sleep(2000);
            }
        });
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        throw new RuntimeException("Failure in WebSocket handling", exception);
    }
}
