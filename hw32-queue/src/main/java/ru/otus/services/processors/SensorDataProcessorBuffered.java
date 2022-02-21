package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.time.LocalDateTime;
import java.util.*;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final Map<LocalDateTime, SensorData> bufferedData = new TreeMap<>();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        if (bufferedData.size() >= bufferSize) {
            flush();
        }
        bufferedData.put(data.getMeasurementTime(), data);
    }

    public synchronized void flush() {
        try {
            if(bufferedData.size()>0) {
                writer.writeBufferedData(bufferedData.values().stream().toList());
                bufferedData.clear();
           }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
