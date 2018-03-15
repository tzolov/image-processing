package org.springframework.cloud.stream.app.imageviewer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.general.GeneralFont;
import org.openimaj.math.geometry.shape.Rectangle;

import org.springframework.tuple.JsonStringToTupleConverter;
import org.springframework.tuple.Tuple;

/**
 * @author Christian Tzolov
 */
public class Main2 {
	public static void main(String[] args) throws IOException {
		String fileName = "/Users/ctzolov/Dev/projects/scdf/tmp/image-recognition2/tourists.jpg";
		BufferedImage originalImage = ImageIO.read(new File(fileName));

		Graphics2D g = originalImage.createGraphics();
		g.setColor(Color.green);
		g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString("Person", 100, 100);
		g.drawRoundRect(95, 95, 100, 100, 2, 2);


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( originalImage, "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();

		MBFImage image = ImageUtilities.readMBF(new ByteArrayInputStream(imageInByte));

//		float t1 = 0.038780525f;
//		float t2 = 0.20172822f;
//		float t3 = 0.9464298f;
//		float t4 = 0.39004815f;

		float t1 = 0.0f;
		float t2 = 0.3940161f;
		float t3 = 0.9465165f;
		float t4 = 0.5592592f;

		float y1 = t1 * (float) image.getHeight();
		float x1 = t2 * (float) image.getWidth();
		float y2 = t3 * (float) image.getHeight();
		float x2 = t4 * (float) image.getWidth();

		org.openimaj.math.geometry.shape.Rectangle rect = new Rectangle(x1, y1, (x2 - x1), (y2 - y1));
		image.drawShape(rect, RGBColour.RED);

		GeneralFont font = new GeneralFont("Arial", Font.PLAIN);
		int fontSize = 12;
		String text = "person 3";
		image.drawText(text, (int) x1 + 2, (int) y1 + fontSize, font, fontSize, RGBColour.RED);



		JFrame jFrame = DisplayUtilities.makeFrame("Cool!");
		DisplayUtilities.ImageComponent imageComponent = new DisplayUtilities.ImageComponent();
		jFrame.add(imageComponent);


		BufferedImage bi = ImageUtilities.createBufferedImageForDisplay(image);
		imageComponent.setImage(bi);
		imageComponent.setOriginalImage(image);
		imageComponent.setSize(bi.getWidth(), bi.getHeight());
		imageComponent.setPreferredSize(new Dimension(imageComponent.getWidth(), imageComponent.getHeight()));


		jFrame.pack();
		jFrame.setVisible(true);
	}
}
