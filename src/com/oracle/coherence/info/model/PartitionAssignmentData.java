package com.oracle.coherence.info.model;

import java.util.List;
import java.util.Map.Entry;

import javax.management.MBeanServerConnection;

import com.oracle.coherence.info.helper.VisualVMModel;

public class PartitionAssignmentData extends AbstractData {
	private boolean loop;
	private long tstart;
	private long tend;
	

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public long getTstart() {
		return tstart;
	}

	public void setTstart(long tstart) {
		this.tstart = tstart;
	}

	public long getTend() {
		return tend;
	}

	public void setTend(long tend) {
		this.tend = tend;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8328747962881466185L;

	public PartitionAssignmentData() {
		super(1);
	}

	@Override
	public String getReporterReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entry<Object, Data>> getJMXData(MBeanServerConnection server, VisualVMModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data processReporterData(Object[] aoColumns, VisualVMModel model) {
		// TODO Auto-generated method stub
		return null;
	}



}
