package pdytr.uno.bc;

import io.grpc.*;
import java.util.concurrent.TimeUnit;

import pdytr.example.grpc.GreetingServiceGrpc;
import pdytr.example.grpc.GreetingServiceOuterClass;

public class Client {

    public static void main( String[] args ) throws Exception {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
            .usePlaintext(true)
            .build();

        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingServiceOuterClass.HelloRequest request = GreetingServiceOuterClass.HelloRequest.newBuilder()
            .setName("General Kenobi")
            .build();

        // Finally, make the call using the stub
        GreetingServiceOuterClass.HelloResponse response = null;
        response = stub.withDeadlineAfter(3000, TimeUnit.MILLISECONDS).greeting(request);

        if (response != null) System.out.println(response);

        // A Channel should be shutdown before stopping the process.
        channel.shutdownNow();
    }
}
