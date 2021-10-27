package pdytr.uno.bc;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;

import pdytr.example.grpc.GreetingServiceGrpc;
import pdytr.example.grpc.GreetingServiceOuterClass;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
    @Override
    public void greeting(GreetingServiceOuterClass.HelloRequest request,
        StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {

        try {

            TimeUnit.MILLISECONDS.sleep(2500);

            // HelloRequest has toString auto-generated.
            System.out.println(request);

            // You must use a builder to construct a new Protobuffer object
            GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
                .setGreeting("Hello there, " + request.getName())
                .build();

            // Use responseObserver to send a single response back
            responseObserver.onNext(response);

            // When you are done, you must call onCompleted.
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
