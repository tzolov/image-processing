package org.springframework.cloud.stream.app.qrreader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;

@EnableBinding(Processor.class)
@EnableAutoConfiguration
@SpringBootApplication
public class QrReaderApplication {

	private static final Log logger = LogFactory.getLog(QrReaderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(QrReaderApplication.class, args);
	}

	@StreamListener
	@Output(Processor.OUTPUT)
	public Flux<String> receive(@Input(Processor.INPUT) Flux<byte[]> imageInBytes) {
		return imageInBytes.map(imgBytes -> decodeQrImage(imgBytes));
	}

	private String decodeQrImage(byte[] imageBytes) {
		try (InputStream in = new ByteArrayInputStream(imageBytes)) {
			BufferedImage bufferedImage = ImageIO.read(in);
			LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Result result = new MultiFormatReader().decodeWithState(bitmap);

			if (result != null) {
				return result.getText();
			}
		}
		catch (NotFoundException e) {
			// No QR found in the input image. Do Nothing!
		}
		catch (IOException e) {
			logger.warn("QR decoding failed to read the input byte array!", e);
		}

		// Null mend that QR image is found and not output message will be send.
		return null;
	}
}
