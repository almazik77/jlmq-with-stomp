package ru.itis.jlmqwithstomp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.itis.jlmqwithstomp.dto.QueueDto;
import ru.itis.jlmqwithstomp.services.QueueService;

@RestController
public class QueueController {
    @Autowired
    private QueueService queueService;

    @PostMapping("/queue/create")
    public ResponseEntity<?> createQueue(@RequestBody QueueDto dto) {
        try {
            queueService.createQueue(dto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
