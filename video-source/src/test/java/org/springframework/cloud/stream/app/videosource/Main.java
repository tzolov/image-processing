package org.springframework.cloud.stream.app.videosource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 * @author Christian Tzolov
 */
public class Main {

	public static void main(String[] args) throws IOException {
		XuggleVideo video = new XuggleVideo(new File("/Users/ctzolov/Downloads/SCDF-ComputerVision.mp4"));

//		MBFImage image = video.getNextFrame();
//		for (int i = 0; i < 1000; i++) {
//			image = video.getNextFrame();
//			System.out.print(".");
//		}
//		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		try {
//			ImageUtilities.write(frame, "jpg", baos);
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		byte[] imageBytes = baos.toByteArray();
//
//
//		MBFImage image = ImageUtilities.readMBF(new ByteArrayInputStream(imageBytes));


		JFrame jFrame = DisplayUtilities.makeFrame("Cool!");
		DisplayUtilities.ImageComponent imageComponent = new DisplayUtilities.ImageComponent();
		jFrame.add(imageComponent);



		jFrame.pack();
		jFrame.setVisible(true);

		for (MBFImage img : video) {
			play(img, imageComponent);
			jFrame.pack();

		}


	}

	private static void play(MBFImage image, DisplayUtilities.ImageComponent imageComponent) {
		BufferedImage bi = ImageUtilities.createBufferedImageForDisplay(image);
		imageComponent.setImage(bi);
		imageComponent.setOriginalImage(image);
		imageComponent.setSize(bi.getWidth(), bi.getHeight());
		imageComponent.setPreferredSize(new Dimension(imageComponent.getWidth(), imageComponent.getHeight()));

	}
}
