package org.springframework.cloud.stream.app.imageviewer;

import java.util.ArrayList;

import org.springframework.tuple.JsonStringToTupleConverter;
import org.springframework.tuple.Tuple;

/**
 * @author Christian Tzolov
 */
public class Main {
	public static void main(String[] args) {
		Tuple tuple = new JsonStringToTupleConverter().convert(
		"{\"labels\":[" +
				"{\"person\":0.9996774,\"x1\":0.0,\"y1\":0.3940161,\"x2\":0.9465165,\"y2\":0.5592592}," +
				"{\"person\":0.9996604,\"x1\":0.047891676,\"y1\":0.03169123,\"x2\":0.941098,\"y2\":0.2085562}," +
				"{\"person\":0.9994741,\"x1\":0.038780525,\"y1\":0.20172822,\"x2\":0.9464298,\"y2\":0.39004815}," +
				"{\"person\":0.99946576,\"x1\":0.0632496,\"y1\":0.79281414,\"x2\":0.96308815,\"y2\":0.99012446}," +
				"{\"person\":0.99867463,\"x1\":0.051441744,\"y1\":0.57940257,\"x2\":0.9477767,\"y2\":0.8259268}," +
				"{\"backpack\":0.96534747,\"x1\":0.15588468,\"y1\":0.85957795,\"x2\":0.5091308,\"y2\":0.9908878}," +
				"{\"backpack\":0.963343,\"x1\":0.1273736,\"y1\":0.57658505,\"x2\":0.47765,\"y2\":0.6986431}," +
				"{\"backpack\":0.9294457,\"x1\":0.14560387,\"y1\":0.022079457,\"x2\":0.53702116,\"y2\":0.113561034}," +
				"{\"backpack\":0.6643621,\"x1\":0.53017575,\"y1\":0.31962925,\"x2\":0.77074707,\"y2\":0.4030401}," +
				"{\"backpack\":0.57413465,\"x1\":0.1278874,\"y1\":0.457999,\"x2\":0.3998378,\"y2\":0.5622766}]}");
		ArrayList<Tuple> lables = (ArrayList) tuple.getValues().get(0);

		for (Tuple l: lables) {
			System.out.println(l);
			System.out.println(l.getFieldNames().get(0));
			System.out.println(l.getValues().get(1));
			System.out.println(l.getFloat("x1"));
		}

	}
}
