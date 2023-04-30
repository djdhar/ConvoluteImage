package com.imageconvolution.convoluteimage.controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class DemoController {

    @GetMapping("/data")
    public ResponseEntity<Flux<String>> getData() {
        // Simulating an asynchronous data stream
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_STREAM_JSON);
        var flux = Flux.just("Data 1", "Data 2", "Data 3")
                .delayElements(Duration.ofSeconds(1));
        return ResponseEntity.ok()
                .headers(headers)
                .body(flux);
    }
}
