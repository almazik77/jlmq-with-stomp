package ru.itis.jlmqwithstomp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "message")
public class JlmqMessage {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    private String messageId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status;

    private String body;

    public enum Status {
        RECEIVED, ACCEPTED, COMPLETED;
    }
}
