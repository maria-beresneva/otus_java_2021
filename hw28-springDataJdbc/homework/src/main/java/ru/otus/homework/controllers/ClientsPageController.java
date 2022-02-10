package ru.otus.homework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.homework.model.Client;
import ru.otus.homework.service.DBServiceClient;

import java.util.List;

@Controller
public class ClientsPageController {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final DBServiceClient dbServiceClient;

    public ClientsPageController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/clients")
    protected String getAllClients(Model model) {
        List<Client> allClients = dbServiceClient.findAll();

        model.addAttribute(TEMPLATE_ATTR_CLIENTS, allClients);
        return CLIENTS_PAGE_TEMPLATE;
    }

}
