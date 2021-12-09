package ru.otus.listener.homework;


import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.ProcessorException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorExceptionTest {

    @Test
    void processorExceptionTest() {
        final DateTimeProvider localDateTime = new DateTimeProvider() {
            LocalDateTime datetime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
            @Override
            public LocalDateTime getDate() {
                datetime = datetime.plusSeconds(1);
                return datetime;
            }
        };

        //given
        var processorException = new ProcessorException(localDateTime);
        var id = 100L;

        var message = new Message.Builder(id)
                .field10("field10")
                .build();

        //then
        assertThat(processorException.process(message)).isEqualTo(message);

        assertThrows(IllegalStateException.class, () -> processorException.process(message));
    }
}
