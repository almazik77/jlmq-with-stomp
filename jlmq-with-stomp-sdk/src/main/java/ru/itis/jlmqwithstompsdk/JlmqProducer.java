package ru.itis.jlmqwithstompsdk;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class JlmqProducer {
    private ListenableFuture<StompSession> session;
    private String queueName;

    public JlmqProducer(ListenableFuture<StompSession> session) {
        this.session = session;
    }

    public JlmqProducer toQueue(String queueName) {
        this.queueName = queueName;
        return this;
    }


    public void send(Object body) throws ExecutionException, InterruptedException {
        JlmqMessage message = JlmqMessage.builder()
                .body(body.toString())
                .command("send")
                .queueName(queueName)
                .build();
        System.out.println("sending : " + message.toString());
    }

}
