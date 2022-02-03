package ru.otus.servlet.api.client;

import com.google.gson.Gson;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.servlet.api.client.model.InputClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputClientParser {
    static Client parseInputClient(String inputUserJson) throws IllegalArgumentException {
        InputClient unparsedClient = new Gson().fromJson(inputUserJson, InputClient.class);
        List<Phone> phones = Arrays.stream(unparsedClient.getPhones())
                .map(Phone::new)
                .collect(Collectors.toList());
        return new Client(null, unparsedClient.getName(),
                new Address(unparsedClient.getAddress()),
                phones);
    }
}
