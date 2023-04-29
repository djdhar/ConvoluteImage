package com.imageconvolution.convoluteimage.controller;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
public class ConvolutionController {

    @PostMapping(value = "/convImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono <ResponseEntity<byte[]>> uploadTargets(@RequestPart(name = "image") Mono<FilePart> file) {
        return file.flatMap(filePart -> {
                return convertFilePartToByteArray(filePart).map(byteArray -> {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_PNG);
                    return new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.CREATED);
                });

        });
    }

    private Mono<byte[]> convertFilePartToByteArray(FilePart file) {
        return DataBufferUtils.join(file.content())
                .map(dataBuffer -> dataBuffer.asByteBuffer().array());
    }
}
