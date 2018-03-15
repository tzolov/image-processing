package org.springframework.cloud.stream.app.webcamsource;

import java.awt.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamUtils;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.context.annotation.Bean;

@EnableBinding(Source.class)
@EnableAutoConfiguration
@EnableConfigurationProperties(WebcamSourceProperties.class)
@SpringBootApplication
public class WebcamSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebcamSourceApplication.class, args);
	}

	@Autowired
	private WebcamSourceProperties properties;

	@Autowired
	private Webcam webcam;

	@StreamEmitter
	@Output(Source.OUTPUT)
	public Flux<byte[]> emit() {
		return Flux.interval(Duration.of(properties.getCaptureInterval(), ChronoUnit.MILLIS))
				.map(l -> {
					WebcamUtils.capture(webcam, "capturedImage", properties.getImageFormat());
					return WebcamUtils.getImageBytes(webcam, properties.getImageFormat().toLowerCase());
				});
	}

	@Bean
	public Webcam webcam(WebcamSourceProperties properties) {
		Webcam webcam = Webcam.getDefault();
		System.out.println("View size" + webcam.getViewSize().width + " : " + webcam.getViewSize().height);
		webcam.setViewSize(new Dimension(properties.getWidth(), properties.getHeight()));
		return webcam;
	}
}
