package com.imageconvolution.convoluteimage.controller;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
public class ConvolutionController {



    @PostMapping(value = "/convImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Flux<byte[]>> uploadTargets(@RequestPart(name = "image") FilePart filePart) throws ExecutionException, InterruptedException, IOException {
                var byteArray = convertFilePartToByteArray(filePart);
                    final HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    var flux = converToConvoluteImages(byteArray);
                    return ResponseEntity.ok()
                    .headers(headers)
                    .body(flux);
    }

    private byte[] convertFilePartToByteArray(FilePart file) throws ExecutionException, InterruptedException {
        return DataBufferUtils.join(file.content())
                .map(dataBuffer -> dataBuffer.asByteBuffer().array()).toFuture().get();
    }

    private Flux<byte[]> converToConvoluteImages(byte[] imageBytes) throws IOException {
        Mat mat = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_UNCHANGED);

//        Mat mat3 = mat.clone();
//        Mat mat2 = mat.clone();
//        int i_x = 0;
//        for(int i=0; i<mat.rows();i=i+2) {
//            for(int j=0;j<mat.cols(); j++) {
//                mat2.put(i_x, j, mat.get(i, j));
//            }
//            i_x++;
//        }
//        Mat first = mat2.submat(0, mat2.rows()/2-1, 0, mat2.cols()-1);
//
//        int j_x = 0;
//        for(int j=0; j<mat.cols();j=j+2) {
//            for(int i=0;i<mat.rows(); i++) {
//                mat3.put(i, j_x, mat.get(i, j));
//            }
//            j_x++;
//        }
//        Mat second = mat3.submat(0, mat2.rows()-1, 0, mat2.cols()/2-1);
//        Mat matRet = new Mat();
//        Core.hconcat(Arrays.asList(first, first), matRet);
        List<Mat> rowWiseSplitted = rowWiseSplit(mat);
        List<Mat> colWiseSplitted1 = colWiseSplit(rowWiseSplitted.get(0));
        Mat mat1 = colWiseSplitted1.get(0);
        Mat mat2 = colWiseSplitted1.get(1);
        List<Mat> colWiseSplitted2 = colWiseSplit(rowWiseSplitted.get(1));
        Mat mat3 = colWiseSplitted2.get(0);
        Mat mat4 = colWiseSplitted2.get(1);
        Imgproc.resize(mat1, mat1, new Size(200, 200));
        Imgproc.resize(mat2, mat2, new Size(200, 200));
        Imgproc.resize(mat3, mat3, new Size(200, 200));
        Imgproc.resize(mat4, mat4, new Size(200, 200));
        Mat joinMat1 = new Mat(); Mat joinMat2 =  new Mat(); Mat joinMat3 =  new Mat();
        Core.vconcat(Arrays.asList(mat1, mat2), joinMat1);
        Core.vconcat(Arrays.asList(mat3, mat4), joinMat2);
        Core.hconcat(Arrays.asList(joinMat1, joinMat2), joinMat3);

        return Flux.just(concatImageBytes(Arrays.asList(matToBytes(joinMat3))))
                .log()
                .delayElements(Duration.ofSeconds(3));
    }

    public static byte[] matToBytes(Mat image)
    {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, matOfByte);
        return matOfByte.toArray();
    }

    private byte[] concatImageBytes(List<byte[]> imageBytesList) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        for (byte[] arr: imageBytesList)
            outputStream.write( arr );
        //outputStream.write( b );

        return outputStream.toByteArray( );
    }

    private List<Mat> rowWiseSplit(Mat mat) {
        Mat mat2 = mat.clone();
        Mat mat3 = mat.clone();
        int i_x = 0;
        for(int i=0; i<mat.rows();i=i+2) {
            for(int j=0;j<mat.cols(); j++) {
                mat2.put(i_x, j, mat.get(i, j));
            }
            i_x++;
        }

        mat2 = mat2.submat(0, i_x-1, 0, mat2.cols()-1);

        i_x = 0;
        for(int i=1; i<mat.rows();i=i+2) {
            for(int j=0;j<mat.cols(); j++) {
                mat3.put(i_x, j, mat.get(i, j));
            }
            i_x++;
        }

        mat3 = mat3.submat(0, i_x-1, 0, mat3.cols()-1);


        return Arrays.asList(mat2, mat3);
    }

    private List<Mat> colWiseSplit(Mat mat) {
        Mat mat2 = mat.clone();
        int j_x = 0;
        for(int j=1; j<mat.cols();j=j+2) {
            for(int i=0;i<mat.rows(); i++) {
                mat2.put(i, j_x, mat.get(i, j));
            }
            j_x++;
        }
        mat2 = mat2.submat(0, mat2.rows()-1, 0, j_x-1);

        Mat mat3 = mat.clone();
        j_x = 0;
        for(int j=0; j<mat.cols();j=j+2) {
            for(int i=0;i<mat.rows(); i++) {
                mat3.put(i, j_x, mat.get(i, j));
            }
            j_x++;
        }
        mat3 = mat3.submat(0, mat3.rows()-1, 0, j_x-1);

        return Arrays.asList(mat2, mat3);
    }

}
