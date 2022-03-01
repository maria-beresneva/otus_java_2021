package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.ResponseMessage;

public class ClientStreamObserver implements StreamObserver<ResponseMessage> {

    private int lastValue = 0;

    @Override
    public void onNext(ResponseMessage message) {
        setLastValue(message.getValue());
        System.out.printf("Next value: %d \n", lastValue);
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t);
    }

    @Override
    public void onCompleted() {
        System.out.println("Work finished");
    }

    public synchronized void setLastValue(int value) {
        lastValue = value;
    }

    public synchronized int getLastValueAndReset() {
        int result = lastValue;
        lastValue = 0;
        return result;
    }
}
