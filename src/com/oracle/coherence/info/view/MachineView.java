/*
 * File: MachineData.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of 
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.coherence.info.view;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.management.MBeanServerConnection;

import com.oracle.coherence.info.helper.VisualVMModel;
import com.oracle.coherence.info.model.Data;
import com.oracle.coherence.info.model.MachineData;

/**
 * A class to hold machine data.
 *
 * @author Tim Middleton
 */
public class MachineView extends AbstractView {
	private static final long serialVersionUID = -4146745462482520312L;

	private List<Entry<Object, Data>> machineData;

	public MachineView(MBeanServerConnection server, VisualVMModel model) {
		super(server, model);
	}

	public void printData() {
		if (machineData != null) {
			System.out.println("Total Machine Count = " + machineData.size() + ",\t\tDate: " + hdf.format(new Date()));
			System.out.println("+-----------------------------------+---------------+---------+---------+----------------+----------+");
			System.out.println("|            Machine Name           |    FreeMem    | LoadAvg | ProcCnt |    TotalMem    | FreeMem% |");
			System.out.println("+-----------------------------------+---------------+---------+---------+----------------+----------+");
			for (Entry<Object, Data> entry : machineData) {
				String machineName = (String)entry.getValue().getColumn(MachineData.MACHINE_NAME);
				long freePhysicalMemory = (Long)entry.getValue().getColumn(MachineData.FREE_PHYSICAL_MEMORY);
				double systemLoadAverage = (Double)entry.getValue().getColumn(MachineData.SYSTEM_LOAD_AVERAGE);
				int processorCount = (Integer)entry.getValue().getColumn(MachineData.PROCESSOR_COUNT);
				long totalPhysicalMemory = (Long)entry.getValue().getColumn(MachineData.TOTAL_PHYSICAL_MEMORY);
				float percentFreeMemory = (Float)entry.getValue().getColumn(MachineData.PERCENT_FREE_MEMORY);
				System.out.format("|%-35s|%15s|%9.2f|%9d|%16s|%10.2f|\n", 
						machineName, 
						NumberFormat.getNumberInstance(Locale.US).format(freePhysicalMemory), 
						systemLoadAverage, 
						processorCount, 
						NumberFormat.getNumberInstance(Locale.US).format(totalPhysicalMemory), 
						percentFreeMemory*100);
			}
			System.out.println("+-----------------------------------+---------------+---------+---------+----------------+----------+");
		} else {
			System.out.println("No machine in Coherence");
		}
	}

	

//					data.setColumn(MachineView.MACHINE_NAME, entry.getKey().getX());
//					data.setColumn(MachineView.FREE_PHYSICAL_MEMORY, (Long) server.getAttribute(objectName, "FreePhysicalMemorySize"));
//					data.setColumn(MachineView.SYSTEM_LOAD_AVERAGE, (Double) server.getAttribute(objectName, "SystemLoadAverage"));
//					data.setColumn(MachineView.PROCESSOR_COUNT, (Integer) server.getAttribute(objectName, "AvailableProcessors"));
//					data.setColumn(MachineView.TOTAL_PHYSICAL_MEMORY, (Long) server.getAttribute(objectName, "TotalPhysicalMemorySize"));
//
//					data.setColumn(MachineView.PERCENT_FREE_MEMORY,
//							((Long) data.getColumn(MachineView.FREE_PHYSICAL_MEMORY) * 1.0f) / (Long) data.getColumn(MachineView.TOTAL_PHYSICAL_MEMORY));

	
	/**
	 * {@inheritDoc}
	 */
	public void updateData() {
		machineData = model.getData(VisualVMModel.DataType.MACHINE);
	}
	
}
