package org.springframework.cloud.stream.app.videosource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties("video")
public class VideoSourceProperties {

	/**
	 * Path to the video file
	 */
	private Resource location;

	/**
	 * Indicates whether the video should be looped or not
	 */
	private boolean loop = false;

	/**
	 * Time to wait before sending the next frame in [ms];
	 */
	private long frameWaitInterval = 1;

	/**
	 * Image format
	 */
	private String imageFormat = "jpg";

	public Resource getLocation() {
		return location;
	}

	public void setLocation(Resource location) {
		this.location = location;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public long getFrameWaitInterval() {
		return frameWaitInterval;
	}

	public void setFrameWaitInterval(long frameWaitInterval) {
		this.frameWaitInterval = frameWaitInterval;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
}
