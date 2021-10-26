package pdytr.uno.a.iii;

import io.grpc.*;


import pdytr.example.grpc.GreetingServiceGrpc;
import pdytr.example.grpc.GreetingServiceOuterClass;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
            .usePlaintext(true)
            .build();

        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingServiceOuterClass.HelloRequest request =
        GreetingServiceOuterClass.HelloRequest.newBuilder()
            .setName("Ray")
            .build();

        // Create a thread that waits half a second and kills the client, hoping the request
        // is sent and the server remains with no one to send the response to - :( -.
        new Thread() {
            public void run() {
                try {
                    this.sleep(350);
                    System.exit(125);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


      // Finally, make the call using the stub
      GreetingServiceOuterClass.HelloResponse response =
        stub.greeting(request);

      System.out.println(response);

      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();
    }
}
