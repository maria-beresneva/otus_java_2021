package ru.otus.homework.controllers;

import com.google.gson.Gson;
import ru.otus.homework.controllers.model.InputClient;
import ru.otus.homework.model.Address;
import ru.otus.homework.model.Client;
import ru.otus.homework.model.Phone;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputClientParser {
    static Client parseInputClient(String inputClientJson) throws IllegalArgumentException {
        InputClient unparsedClient = new Gson().fromJson(inputClientJson, InputClient.class);
        List<Phone> phones = Arrays.stream(unparsedClient.getPhones())
                .map(Phone::new)
                .collect(Collectors.toList());
        return new Client(null, unparsedClient.getName(),
                new Address(unparsedClient.getAddress()),
                phones);
    }
}
