package com.oracle.coherence.info.view;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.management.MBeanServerConnection;

import com.oracle.coherence.info.helper.VisualVMModel;

public abstract class AbstractView {
	SimpleDateFormat hdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");
	SimpleDateFormat df = new SimpleDateFormat("mm:ss.SSS");

	MBeanServerConnection server;
	VisualVMModel model;

	public AbstractView(MBeanServerConnection server, VisualVMModel model) {
		this.server = server;
		this.model = model;
	}

	public double toByte(String s) {
		// System.out.println("size = [" + s + "]");
		double size = 0.0;
		if (s.endsWith("KB")) {
			size = Double.parseDouble(s.substring(0, s.indexOf("KB"))) * 1024L;
		} else if (s.endsWith("MB")) {
			size = Double.parseDouble(s.substring(0, s.indexOf("MB"))) * 1024L * 1024L;
		} else if (s.endsWith("GB")) {
			size = Double.parseDouble(s.substring(0, s.indexOf("GB"))) * 1024L * 1024L * 1024L;
		} else {
			size = Double.parseDouble(s);
			//throw new IllegalArgumentException("size is wrong : " + s);
		}
		return size;
	}

	public String fromByte(double size) {

		if (size > (1024L * 1024L * 1024L)) {
			double s = size / (1024L * 1024L * 1024L);
			return String.format("%5.2f GB (%s)", s , NumberFormat.getNumberInstance(Locale.US).format(size));
			//return s + " GB (" + NumberFormat.getNumberInstance(Locale.US).format(size) + ")";
		} else if (size > (1024L * 1024L)) {
			double s = size / (1024L * 1024L);
			return String.format("%5.2f MB (%s)", s , NumberFormat.getNumberInstance(Locale.US).format(size));
			//return s + " MB (" + NumberFormat.getNumberInstance(Locale.US).format(size) + ")";
		} else if (size > 1024L) {
			double s = size / 1024L;
			return String.format("%5.2f KB (%s)", s , NumberFormat.getNumberInstance(Locale.US).format(size));
			//return s + " KB (" + NumberFormat.getNumberInstance(Locale.US).format(size) + ")";
		} else {
			double s = size;
			return String.format("%5.2f B", s);
		}
	}

	public abstract void updateData();

	public abstract void printData();
}
