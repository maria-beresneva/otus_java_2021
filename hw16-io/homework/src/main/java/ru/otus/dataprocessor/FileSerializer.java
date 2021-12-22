package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileNameForOutput;

    public FileSerializer(String fileName) {
        fileNameForOutput = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileNameForOutput)){
            new ObjectMapper().writeValue(fileOutputStream, data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
