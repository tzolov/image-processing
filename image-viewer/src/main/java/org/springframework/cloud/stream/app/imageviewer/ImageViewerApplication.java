package org.springframework.cloud.stream.app.imageviewer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@EnableAutoConfiguration
@SpringBootApplication
public class ImageViewerApplication {

	private static final Log logger = LogFactory.getLog(ImageViewerApplication.class);

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ImageViewerApplication.class);
		builder.headless(false).run(args);
	}

	final JFrame jFrame;
	final DisplayUtilities.ImageComponent imageComponent;

	public ImageViewerApplication() {
		jFrame = DisplayUtilities.makeFrame("Spring Cloud Data Flow Viewer");
		imageComponent = new DisplayUtilities.ImageComponent();
		jFrame.add(imageComponent);
	}

	@StreamListener(Sink.INPUT)
	public void processVote(byte[] imageBytes) {
		try {
			MBFImage image = ImageUtilities.readMBF(new ByteArrayInputStream(imageBytes));
			BufferedImage bi = ImageUtilities.createBufferedImageForDisplay(image);
			imageComponent.setImage(bi);
			imageComponent.setOriginalImage(image);
			imageComponent.setSize(bi.getWidth(), bi.getHeight());
			imageComponent.setPreferredSize(new Dimension(imageComponent.getWidth(), imageComponent.getHeight()));


			jFrame.pack();
			jFrame.setVisible(true);
		}
		catch (IOException e) {
			logger.error(e);
		}
	}
}
