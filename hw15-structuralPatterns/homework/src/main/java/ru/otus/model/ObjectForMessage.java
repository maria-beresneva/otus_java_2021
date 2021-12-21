package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage clone() {
        var newObjectForMessage = new ObjectForMessage();
        newObjectForMessage.setData(new ArrayList<>(getData()));
        return newObjectForMessage;
    }
}
