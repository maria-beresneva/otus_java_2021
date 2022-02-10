package ru.otus.homework.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.controllers.model.InputClient;
import ru.otus.homework.model.Address;
import ru.otus.homework.model.Client;
import ru.otus.homework.model.Phone;
import ru.otus.homework.service.DBServiceAddress;
import ru.otus.homework.service.DBServiceClient;
import ru.otus.homework.service.DBServicePhone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ClientApiController {

    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;
    private final DBServicePhone dbServicePhone;

    public ClientApiController(DBServiceClient dbServiceClient,
                               DBServiceAddress dbServiceAddress,
                               DBServicePhone dbServicePhone) {
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
        this.dbServicePhone = dbServicePhone;
    }

    @PostMapping("/api/client")
    public Client createClient(@RequestBody String clientString) {
        Client parsedClient = InputClientParser.parseInputClient(clientString);
        Client singleClient = dbServiceClient.saveClient(new Client(parsedClient.getName()));
        Address address = dbServiceAddress.saveAddress(new Address(null, parsedClient.getAddress().getStreet(), singleClient.getId()));

        List<Phone> phones = new ArrayList<>();
        parsedClient.getPhones().forEach(phone ->
                phones.add(dbServicePhone.savePhone(new Phone(null, phone.getNumber(), singleClient.getId()))));

        return new Client(singleClient.getId(), singleClient.getName(), address, phones);
    }

    @GetMapping("/api/client/{id}")
    public Client getClient(@PathVariable(name="id") Long clientId) {
        Optional<Client> client = dbServiceClient.getClient(clientId);
        return client.get();
    }
}
