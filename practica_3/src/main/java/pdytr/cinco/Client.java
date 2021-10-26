package pdytr.cinco;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.google.protobuf.Empty;

import pdytr.cinco.BlankServiceGrpc.BlankServiceBlockingStub;
import static pdytr.cinco.BlankServiceGrpc.newBlockingStub;

public class Client
{
	private static final String database = "src" + File.separator
                            + "main" + File.separator
                            + "java" + File.separator
                            + "pdytr" + File.separator
                            + "cinco" + File.separator
                            + "files";

    public static void main(String[] args) throws Exception
    {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
            .usePlaintext(true)
            .build();

        try {
            // create stream writer
            File file = new File(Client.database, "times.csv");
            FileOutputStream stream = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));

            // create the stub
            BlankServiceBlockingStub stub = newBlockingStub(channel);

            // craete the read request
            Empty empty = Empty.newBuilder().build();

			long start, end;
			for (int i = 0; i < 100000; i++) {
				start = System.nanoTime();
                stub.blank(empty);
				end = System.nanoTime();

                bw.write(Double.toString((end - start) / 1000000.0));
                bw.newLine();
			}
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdownNow();
        }
    }
}
