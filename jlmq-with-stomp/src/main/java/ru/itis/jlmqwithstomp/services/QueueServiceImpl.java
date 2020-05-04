package ru.itis.jlmqwithstomp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.jlmqwithstomp.dto.QueueDto;
import ru.itis.jlmqwithstomp.models.Queue;
import ru.itis.jlmqwithstomp.repositories.QueueRepository;

@Service
public class QueueServiceImpl implements QueueService {
    @Autowired
    private QueueRepository queueRepository;

    @Override
    public void createQueue(QueueDto queueDto) {
        Queue queue = Queue.builder()
                .name(queueDto.getName())
                .build();
        queueRepository.save(queue);
    }
}
