package ru.itis.jlmqwithstomp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.jlmqwithstomp.models.JlmqMessage;
import ru.itis.jlmqwithstomp.models.Queue;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<JlmqMessage, Long> {
    Optional<JlmqMessage> findByMessageId(String token);

    Optional<JlmqMessage> findFirstByQueueAndStatus(Queue queue, JlmqMessage.Status status);

    List<JlmqMessage> findAllByQueueAndStatus(Queue queue, JlmqMessage.Status status);
}
