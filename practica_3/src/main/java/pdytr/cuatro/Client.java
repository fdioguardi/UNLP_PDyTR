package pdytr.cuatro;

import java.io.File;

import com.google.protobuf.ByteString;
import pdytr.cuatro.FtpServiceGrpc.FtpServiceBlockingStub;
import static pdytr.cuatro.FtpServiceGrpc.newBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import pdytr.cuatro.FtpServiceOuterClass.ReadRequest;
import pdytr.cuatro.FtpServiceOuterClass.ReadResponse;
import pdytr.cuatro.FtpServiceOuterClass.WriteRequest;
import pdytr.cuatro.FtpServiceOuterClass.WriteResponse;

public class Client {
	private static final String database = "src" + File.separator
                            + "main" + File.separator
                            + "java" + File.separator
                            + "pdytr" + File.separator
                            + "cuatro" + File.separator
                            + "files";

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.err.println("usage: Client <filename>");
            System.exit(1);
        }

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
            .usePlaintext(true)
            .build();

        try {
            // create file to read
            File file = new File(Client.database, args[0]);

            // create the stub
            FtpServiceBlockingStub stub = newBlockingStub(channel);

            // create a read request of a maximum size of 1MB
            // (there's no need to read the whole file)
            ReadRequest readRequest = ReadRequest.newBuilder()
                .setName(file.getName())
                .setOffset(0)
                .setReadingAmount((int)file.length() < 1024 ? (int)file.length() : 1024)
                .build();

            // make the call to read using the stub (expect a response from the server)
            ReadResponse readResponse = stub.read(readRequest);

            /*
             * now that the readRequest holds some data, make the server write it
             * (as every client tries to write to the same file, this should boom boom most of the time)
            */

            int offset = 0;
            while (offset < readResponse.getContent().size()) {
                // create the write request
                WriteRequest writeRequest = WriteRequest.newBuilder()
                    .setName("file.txt")
                    .setData(ByteString.copyFrom(readResponse.getContent().substring(offset, offset + 2).toByteArray()))
                    .setWritingAmount(2)
                    .build();

                // make the call to write using the stub (expect a response from the server)
                WriteResponse writeResponse = stub.write(writeRequest);

                // update the offset
                offset += 2;
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // A Channel should be shutdown before stopping the process.
            channel.shutdownNow();
            channel.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
        }
    }
}
