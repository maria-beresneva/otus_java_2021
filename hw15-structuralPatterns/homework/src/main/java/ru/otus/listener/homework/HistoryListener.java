package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.*;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, TreeMap<Long, Message>> messages = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        ObjectForMessage savedObjectForMessage = null;

        if(msg.getField13()!=null){
            savedObjectForMessage = new ObjectForMessage();
            savedObjectForMessage.setData(new ArrayList<>(msg.getField13().getData()));
        }

        Message savedMessage = msg.toBuilder().field13(savedObjectForMessage).build();

        messages.computeIfAbsent(msg.getId(), k -> new TreeMap<>());
        var messageHistory = messages.get(msg.getId());
        messageHistory.put(System.currentTimeMillis(),savedMessage);
        messages.put(msg.getId(), messageHistory);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messages.get(id).firstEntry().getValue());
    }
}
