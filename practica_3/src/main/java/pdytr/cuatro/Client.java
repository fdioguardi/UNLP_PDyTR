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
            System.err.println("usage: Client FILE_TO_READ");
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

            // craete the read request
            // to read the entire FILE_TO_READ (or as much as you can in one go)
            ReadRequest readRequest = ReadRequest.newBuilder()
                .setName(file.getName())
                .setOffset(0)
                .setReadingAmount(-1)
                .build();

            // make the call to read using the stub (expect a response from the server)
            ReadResponse readResponse = stub.read(readRequest);

            /*
             * now that the readRequest holds some data, make the server write it
             * (as every client tries to write to the same file, this should boom boom most of the time)
            */

			byte[] content = readResponse.getContent().toByteArray();
            byte[] bytesToSend = null;
			int offset = 0;
			int writing_amount;
            WriteResponse writeResponse = null;
			do {
				// create buffer to send
                bytesToSend = new byte[2];
                bytesToSend[0] = content[offset];
                bytesToSend[1] = content[offset + 1];

                // create the write request
                // to write to a file named "file.txt"
                WriteRequest writeRequest = WriteRequest.newBuilder()
                    .setName("file.txt")
                    .setWritingAmount(bytesToSend.length)
                    .setData(ByteString.copyFrom(bytesToSend))
                    .build();

                // make the call to write using the stub
                writeResponse = stub.write(writeRequest);

                // increment the offset
                offset += writeResponse.getLength();

            } while (offset < content.length - 1);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // A Channel should be shutdown before stopping the process.
            channel.shutdownNow();
        }
    }
}
