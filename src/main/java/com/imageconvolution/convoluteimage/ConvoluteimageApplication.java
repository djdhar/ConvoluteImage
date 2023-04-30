package com.imageconvolution.convoluteimage;

import nu.pattern.OpenCV;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConvoluteimageApplication {

	public static void main(String[] args) {
		OpenCV.loadShared();
		SpringApplication.run(ConvoluteimageApplication.class, args);
	}

}
