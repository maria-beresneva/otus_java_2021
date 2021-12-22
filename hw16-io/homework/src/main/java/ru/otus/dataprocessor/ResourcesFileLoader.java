package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try {
            Iterator<JsonNode> jsonTree = new ObjectMapper()
                    .readTree(getClass().getClassLoader().getResourceAsStream(fileName)).elements();
            List<Measurement> measurements = new ArrayList<>();
            while(jsonTree.hasNext()) {
                JsonNode jsonNode = jsonTree.next();
                measurements.add(new Measurement(jsonNode.get("name").asText(), jsonNode.get("value").asDouble()));
            }
            return measurements;
        } catch (IOException e) {
            throw new ClassCastException();
        }
    }
}
