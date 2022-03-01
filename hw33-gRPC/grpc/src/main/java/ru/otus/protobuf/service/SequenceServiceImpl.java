package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RequestMessage;
import ru.otus.protobuf.generated.SequenceServiceGrpc;
import ru.otus.protobuf.generated.ResponseMessage;

public class SequenceServiceImpl extends SequenceServiceGrpc.SequenceServiceImplBase {

    @Override
    public void getSequence(RequestMessage request, StreamObserver<ResponseMessage> responseObserver) {

        for (int idx = request.getFirstValue(); idx <= request.getLastValue(); idx++) {
            responseObserver.onNext(ResponseMessage.newBuilder().setValue(idx).build());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        responseObserver.onCompleted();
    }
}
