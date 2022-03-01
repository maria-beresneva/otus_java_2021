package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.RequestMessage;
import ru.otus.protobuf.generated.SequenceServiceGrpc;
import ru.otus.protobuf.service.ClientStreamObserver;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = SequenceServiceGrpc.newStub(channel);

        RequestMessage initMessage = RequestMessage.newBuilder().setFirstValue(0).setLastValue(30).build();

        int currentValue = 0;

        var responseObserver = new ClientStreamObserver();
        stub.getSequence(initMessage, responseObserver);

        for (int i = 0; i < 50; i++) {
            currentValue = currentValue + responseObserver.getLastValueAndReset() + 1;
            System.out.printf("currentValue: %d \n", currentValue);
            Thread.sleep(1000);
        }
        channel.shutdown();
    }
}
