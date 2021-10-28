package pdytr.cinco;

import io.grpc.stub.StreamObserver;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;

import pdytr.cinco.BlankServiceOuterClass.Content;
import pdytr.cinco.BlankServiceGrpc.BlankServiceImplBase;

public class BlankService extends BlankServiceImplBase {

    public void blank(Empty request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    public void full(Content request, StreamObserver<Content> responseObserver) {
        responseObserver.onNext(Content.newBuilder().setData(ByteString.copyFrom(new byte[8])).build());
        responseObserver.onCompleted();
    }
}
