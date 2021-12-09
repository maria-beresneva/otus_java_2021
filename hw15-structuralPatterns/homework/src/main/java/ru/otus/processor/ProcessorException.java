package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorException implements Processor {

    private final DateTimeProvider localDateTime;

    public ProcessorException(DateTimeProvider localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public Message process(Message message) {
        if((localDateTime.getDate().getSecond() % 2) == 0) {
            throw new UnsupportedOperationException();
        }
        else return message;
    }
}
