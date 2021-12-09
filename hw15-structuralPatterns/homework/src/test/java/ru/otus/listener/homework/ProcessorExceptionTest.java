package ru.otus.listener.homework;


import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.ProcessorException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorExceptionTest {

    @Test
    void processorExceptionTest() throws InterruptedException {
        final DateTimeProvider localDateTime = LocalDateTime::now;

        //given
        var processorException = new ProcessorException(localDateTime);
        var id = 100L;

        var message = new Message.Builder(id)
                .field10("field10")
                .build();

        //when
        if(localDateTime.getDate().getSecond() % 2 == 0) {
            Thread.sleep(1000);
        }

        //then
        assertThat(processorException.process(message)).isEqualTo(message);
        Thread.sleep(1000);

        assertThrows(UnsupportedOperationException.class, () -> processorException.process(message));
    }
}
