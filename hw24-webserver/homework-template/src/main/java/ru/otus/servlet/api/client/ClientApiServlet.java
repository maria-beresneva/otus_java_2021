package ru.otus.servlet.api.client;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.servlet.api.client.InputClientParser.parseInputClient;

public class ClientApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String unparsedClient = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        try {
            Client client = parseInputClient(unparsedClient);
            dbServiceClient.saveClient(client);

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(client));
        } catch (IllegalArgumentException e) {
            System.out.printf("ERROR! Failed to parse user \n%s\nProbably invalid role value. Should be ADMIN or MORTAL.%n", unparsedClient);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<Client> clients = dbServiceClient.findAll();
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }
}
