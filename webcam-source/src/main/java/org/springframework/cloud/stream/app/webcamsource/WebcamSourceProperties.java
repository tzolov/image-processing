package org.springframework.cloud.stream.app.webcamsource;

import com.github.sarxos.webcam.util.ImageUtils;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties("webcam")
public class WebcamSourceProperties {

	private long captureInterval = 100; //ms

	private String imageFormat = ImageUtils.FORMAT_JPG;

	// W x H: [176x144] [320x240] [640x480]
	private int width = 176;

	private int height = 144;

	public long getCaptureInterval() {
		return captureInterval;
	}

	public void setCaptureInterval(long captureInterval) {
		this.captureInterval = captureInterval;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
