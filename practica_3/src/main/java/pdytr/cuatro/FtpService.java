package pdytr.cuatro;

import io.grpc.stub.StreamObserver;
import com.google.protobuf.ByteString;

import pdytr.cuatro.FtpServiceOuterClass.ReadRequest;
import pdytr.cuatro.FtpServiceOuterClass.ReadResponse;
import pdytr.cuatro.FtpServiceOuterClass.WriteRequest;
import pdytr.cuatro.FtpServiceOuterClass.WriteResponse;
import pdytr.cuatro.FtpServiceGrpc.FtpServiceImplBase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

public class FtpService extends FtpServiceImplBase {

	private String database = "src" + File.separator
                            + "main" + File.separator
                            + "java" + File.separator
                            + "pdytr" + File.separator
                            + "cuatro" + File.separator
                            + "files";

    public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {

        try {
            File file = new File(this.database, request.getName());

            // if file exists
            if (file.exists() && file.isFile()) {

                // create stream
                FileInputStream stream = new FileInputStream(file);

                // create data array
                byte[] data = new byte[((request.getReadingAmount() > 0) && (file.length() - request.getOffset() >= request.getReadingAmount()))
                        ? request.getReadingAmount()
                        : (int)(file.length() - request.getOffset())];

                // read file into array
                stream.read(data, request.getOffset(), data.length);

                // load data into a response
                responseObserver.onNext(ReadResponse.newBuilder().setContent(ByteString.copyFrom(data)).setLength(data.length).build());

                // close stream
                stream.close();

            } else {
                // if file doesn't exist
                // load an empty response
                responseObserver.onNext(ReadResponse.newBuilder().setContent(null).setLength(0).build());
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // return the response
            responseObserver.onCompleted();
        }
    }

    public void write(WriteRequest request, StreamObserver<WriteResponse> responseObserver) {

        try {
            File file = new File(this.database, request.getName());
            FileOutputStream stream = new FileOutputStream(file, true);

            stream.write(request.getData().toByteArray(), 0, request.getWritingAmount());
            stream.close();

            responseObserver.onNext(WriteResponse.newBuilder().setLength(request.getWritingAmount()).build());

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            responseObserver.onCompleted();
        }
    }
}
