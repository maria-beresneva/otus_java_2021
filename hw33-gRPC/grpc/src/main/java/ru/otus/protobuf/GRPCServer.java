package ru.otus.protobuf;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.SequenceServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        SequenceServiceImpl sequenceService = new SequenceServiceImpl();
        Server server = ServerBuilder.forPort(SERVER_PORT).addService(sequenceService).build();
        server.start();
        System.out.print("Server started and waiting...");
        server.awaitTermination();
    }
}
