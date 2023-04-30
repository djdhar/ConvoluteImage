package com.imageconvolution.convoluteimage.controller;

import com.imageconvolution.convoluteimage.service.ConvoluteImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


@RestController
public class ConvolutionController {

    @Autowired
    ConvoluteImageService convoluteImageService;

    @PostMapping(value = "/convImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Flux<byte[]>> uploadTargets(@RequestPart(name = "image") FilePart filePart) throws ExecutionException, InterruptedException, IOException {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    var flux = convoluteImageService.convoluteImage(filePart);
                    return ResponseEntity.ok()
                    .headers(headers)
                    .body(flux);
    }
}
