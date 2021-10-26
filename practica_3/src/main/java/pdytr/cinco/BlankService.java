package pdytr.cinco;

import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;

import pdytr.cinco.BlankServiceGrpc.BlankServiceImplBase;

public class BlankService extends BlankServiceImplBase {

    public void blank(Empty request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
