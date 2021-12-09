package ru.otus.listener.homework;


import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.ProcessorSwapFields;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class SwapProcessorTest {

    @Test
    void swapProcessorTest() {
        //given
        var swapProcessor = new ProcessorSwapFields();

        var id = 100L;

        var message = new Message.Builder(id)
                .field11("field11")
                .field12("field12")
                .build();

        //when
        var newMessage = swapProcessor.process(message);

        //then
        assertThat(newMessage.getField11()).isEqualTo(message.getField12());
        assertThat(newMessage.getField12()).isEqualTo(message.getField11());
    }
}