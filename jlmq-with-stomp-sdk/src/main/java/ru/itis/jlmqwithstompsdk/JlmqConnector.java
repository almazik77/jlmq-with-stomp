package ru.itis.jlmqwithstompsdk;

import lombok.SneakyThrows;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;

public class JlmqConnector {
    private JlmqConsumer consumer;
    private ListenableFuture<StompSession> session;
    private WebSocketStompClient stompClient;

    @SneakyThrows
    public JlmqConnector(String queueName) {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient transport = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(transport);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        String url = "ws://127.0.0.1:8084/messages";

        consumer = new JlmqConsumer();
        consumer.setQueueName(queueName);
        session = stompClient.connect(url, consumer);
        Thread.sleep(2000);
        consumer.setSession(session);

        JlmqMessage message = JlmqMessage.builder()
                .body("as")
                .command("send")
                .queueName(queueName)
                .build();
        session.get().send("/tasks." + queueName, message);
    }

    public JlmqConsumer consumer() {
        return consumer;
    }

    public JlmqProducer producer() {
        return new JlmqProducer(session);
    }


}
