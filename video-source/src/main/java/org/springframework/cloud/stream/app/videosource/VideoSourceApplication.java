package org.springframework.cloud.stream.app.videosource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.video.xuggle.XuggleVideo;
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
@EnableConfigurationProperties(VideoSourceProperties.class)
@SpringBootApplication
public class VideoSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoSourceApplication.class, args);
	}

	@Autowired
	private XuggleVideo video;

	@Autowired
	private VideoSourceProperties properties;

	@StreamEmitter
	@Output(Source.OUTPUT)
	public Flux<byte[]> emit() {
		return Flux.interval(Duration.of(properties.getFrameWaitInterval(), ChronoUnit.MILLIS))
				.map(l -> {
					MBFImage frame = video.getNextFrame();
					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						ImageUtilities.write(frame, properties.getImageFormat(), baos);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					return baos.toByteArray();
				});
	}

	@Bean
	public XuggleVideo video(VideoSourceProperties properties) throws IOException {
		return new XuggleVideo(properties.getLocation().getInputStream());
	}
}
