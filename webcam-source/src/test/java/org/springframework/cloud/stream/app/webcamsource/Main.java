package org.springframework.cloud.stream.app.webcamsource;

import com.github.sarxos.webcam.Webcam;

/**
 * @author Christian Tzolov
 */
public class Main {
	public static void main(String[] args) {
		Webcam webcam = Webcam.getDefault();
		System.out.println("View size" + webcam.getViewSize().width + " : " + webcam.getViewSize().height);

	}
}
