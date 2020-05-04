package ru.itis.jlmqwithstompsdk;

import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Type;
import java.util.function.Consumer;

@Setter
public class JlmqConsumer extends StompSessionHandlerAdapter {
    private ListenableFuture<StompSession> session;
    private String queueName;
    private Consumer<JlmqMessage> consumer;

    public JlmqConsumer onReceive(Consumer<JlmqMessage> consumer) {
        this.consumer = consumer;
        return this;
    }


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
                JlmqMessage message = (JlmqMessage) payload;

                message.setCommand("accepted");
                message.setBody(null);
                message.setQueueName(null);
                session.send("/tasks", message);

                consumer.accept(message);
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