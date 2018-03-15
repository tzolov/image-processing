package org.springframework.cloud.stream.app.objectbboxannotation;


import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.tuple.JsonStringToTupleConverter;
import org.springframework.tuple.Tuple;

@EnableBinding(Processor.class)
@EnableAutoConfiguration
@SpringBootApplication
public class ObjectBboxAnnotationApplication {


	private static final Log logger = LogFactory.getLog(ObjectBboxAnnotationApplication.class);

	public static final String IMAGE_FORMAT = "jpg";
	public static final String OBJECT_LABELS_HEADER_NAME = "result";

	private final Color textColor = Color.BLACK;
	private final Color bgColor = new Color(167, 252, 0);
	private final float lineThickness = 2;

	public static void main(String[] args) {
		SpringApplication.run(ObjectBboxAnnotationApplication.class, args);
	}

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Message<?> imageBoundingBoxAnnotation(Message<?> message) {
		if (!(message.getPayload() instanceof byte[])) {
			return message;
		}

		Object result = message.getHeaders().get(OBJECT_LABELS_HEADER_NAME);
		if (result == null) {
			return message;
		}

		byte[] augmentedImage = drawBoundingBoxAnnotation((byte[]) message.getPayload(), result);

		return MessageBuilder.fromMessage(message).withPayload(augmentedImage).build();
	}

	private byte[] drawBoundingBoxAnnotation(byte[] imageBytes, Object result) {
		try {
			if (result != null) {
				BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));


				Graphics2D g = originalImage.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

				Tuple resultTuple = new JsonStringToTupleConverter().convert(result.toString());
				logger.info(resultTuple.toString());
				ArrayList<Tuple> labels = (ArrayList) resultTuple.getValues().get(0);

				for (Tuple l : labels) {

					float y1 = l.getFloat(1) * (float) originalImage.getHeight();
					float x1 = l.getFloat(2) * (float) originalImage.getWidth();
					float y2 = l.getFloat(3) * (float) originalImage.getHeight();
					float x2 = l.getFloat(4) * (float) originalImage.getWidth();

					g.setColor(bgColor);

					Stroke oldStroke = g.getStroke();
					g.setStroke(new BasicStroke(lineThickness));
					g.drawRect((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
					g.setStroke(oldStroke);

					String labelName = l.getFieldNames().get(0);
					int probability = (int) (100 * l.getFloat(0));
					String title = labelName + ": " + probability + "%";

					FontMetrics fm = g.getFontMetrics();
					Rectangle2D rect = fm.getStringBounds(title, g);

					g.setColor(bgColor);
					g.fillRect((int) x1, (int) y1 - fm.getAscent(),
							(int) rect.getWidth() + 6, (int) rect.getHeight());

					g.setColor(textColor);
					g.drawString(title, (int) x1 + 3, (int) y1);
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(originalImage, IMAGE_FORMAT, baos);
				baos.flush();
				imageBytes = baos.toByteArray();
				baos.close();
			}
		}
		catch (IOException e) {
			logger.error(e);
		}

		// Null mend that QR image is found and not output message will be send.
		return imageBytes;
	}

}
