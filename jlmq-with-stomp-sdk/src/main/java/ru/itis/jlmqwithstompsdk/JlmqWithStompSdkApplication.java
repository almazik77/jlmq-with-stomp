package ru.itis.jlmqwithstompsdk;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class JlmqWithStompSdkApplication {

    @SneakyThrows
    public static void main(String[] args) {



        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient transport = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        String url = "ws://127.0.0.1:8084/messages";
        MyStompSessionHandler sessionHandler = new MyStompSessionHandler();
        sessionHandler.setQueueName("1234124");
        sessionHandler.setConsumer(message -> {
            System.out.println("payload : " + message);
        });
        ListenableFuture<StompSession> session = stompClient.connect(url, sessionHandler);


        JlmqMessage message = JlmqMessage.builder()
                .command("send")
                .queueName("1234124")
                .body("Help !@#@@#@")
                .build();

        session.get().send("/tasks", message);
        System.out.println("sending : " + message);
        Thread.sleep(60000);

//        JlmqConnector connector = new JlmqConnector("1234124");
//
//        JlmqProducer producer = connector.producer().toQueue("1234124");
//
//        JlmqConsumer consumer = connector.consumer()
//                .onReceive(jlmqMessage -> {
//                    System.out.println("received : " + jlmqMessage);
//                });
//
//
//        producer.send("Help ?!?!@");
//
//
//        Thread.sleep(60000);
    }

}
