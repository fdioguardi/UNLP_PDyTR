package pdytr.cinco;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.Arrays;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pdytr.cinco.BlankServiceOuterClass.Content;

import pdytr.cinco.BlankServiceGrpc.BlankServiceBlockingStub;
import static pdytr.cinco.BlankServiceGrpc.newBlockingStub;

public class Client {

    private static final int ITERATIONS = 100000;

    private static final String DATABASE = "src" + File.separator
                                         + "main" + File.separator
                                         + "java" + File.separator
                                         + "pdytr" + File.separator
                                         + "cinco" + File.separator
                                         + "files";

    public static void main(String[] args) throws Exception {

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
            .usePlaintext(true)
            .build();

        try {
            // create the stub
            BlankServiceBlockingStub stub = newBlockingStub(channel);

            // create buffered writer
            BufferedWriter emptyBw = createStreamWriter("times_con_mensaje_vacio.csv");
            BufferedWriter fullBw = createStreamWriter("times.csv");

            // craete the read requests
            Empty empty = Empty.newBuilder().build();
            Content full = Content.newBuilder().setData(createData(1000)).build();

            long start, end;

            /*
             * Measure times when calling the server with an
             * empty request and expecting an empty response.
            */
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.nanoTime();
                stub.blank(empty);
                end = System.nanoTime();

                emptyBw.write(Double.toString((end - start) / 1000000.0));
                emptyBw.newLine();
            }
            emptyBw.close();

            /*
             * Now do the same sending a 1000 bytes long request,
             * expecting an 8 bytes long response.
            */
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.nanoTime();
                stub.full(full);
                end = System.nanoTime();

                fullBw.write(Double.toString((end - start) / 1000000.0));
                fullBw.newLine();
            }
            fullBw.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            channel.shutdownNow();
        }
    }

    private static ByteString createData(int size) {
        byte[] data = new byte[size];
        Arrays.fill(data, (byte)'$');
        return ByteString.copyFrom(data);
    }

    private static BufferedWriter createStreamWriter(String filename) throws Exception {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(DATABASE, filename))));
    }
}
