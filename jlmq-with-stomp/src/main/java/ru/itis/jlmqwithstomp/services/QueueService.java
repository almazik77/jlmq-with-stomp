package ru.itis.jlmqwithstomp.services;

import ru.itis.jlmqwithstomp.dto.QueueDto;

public interface QueueService {
    void createQueue(QueueDto queueDto);
}
