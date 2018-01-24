/*
 * File: CoherenceClusterOverviewPanel.java
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.management.MBeanServerConnection;

import com.oracle.coherence.info.helper.VisualVMModel;
import com.oracle.coherence.info.model.Data;
import com.oracle.coherence.info.model.MemberData;

/**
 * An implementation of an {@link AbstractCoherencePanel} to view various
 * overview graphs for a Coherence cluster.
 * 
 * @author Tim Middleton
 */
public class MemberView extends AbstractView {
	private static final long serialVersionUID = 2602085070795849149L;

	private List<Entry<Object, Data>> memberData;

	public MemberView(MBeanServerConnection server, VisualVMModel model) {
		super(server, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void printData() {
		int cTotalMemory = 0;
		int cTotalMemoryUsed = 0;
		int countCacheServer = 0;
		int countMember = 0;
		// get the min /max values for publisher and receiver success rates
		if (memberData != null) {
			System.out.println("Total Member Count = " + memberData.size() + ",\t\tDate: " + hdf.format(new Date()));
			System.out.println("+----+-------------------------------------------------------------------+--------+--------+--------+");
			System.out.println("| ID |                              Role Name                            |  USED  |  TOTAL | USED % |");
			System.out.println("+----+-------------------------------------------------------------------+--------+--------+--------+");
			
			for (Entry<Object, Data> entry : memberData) {
				if (entry.getValue().getColumn(MemberData.ROLE_NAME).equals("CoherenceServer") || entry.getValue().getColumn(MemberData.ROLE_NAME).equals("KrCoKtcsWhoWhoGridCacheServer")) {
					int totalM = (Integer) entry.getValue().getColumn(MemberData.MAX_MEMORY);
					cTotalMemory += totalM;

					int usedM = (Integer) entry.getValue().getColumn(MemberData.USED_MEMORY);
					cTotalMemoryUsed += usedM;

					// System.out.print("#" + count++);
					System.out.format("|%4d|%-67s|%8s|%8s|%7s%%|\n", 
							entry.getValue().getColumn(MemberData.NODE_ID), 
							entry.getValue().getColumn(MemberData.ROLE_NAME) + "(" + entry.getValue().getColumn(MemberData.ADDRESS) + ":" + entry.getValue().getColumn(MemberData.PORT) + ")", 
							NumberFormat.getNumberInstance(Locale.US).format(usedM),
							NumberFormat.getNumberInstance(Locale.US).format(totalM),
							usedM * 100l / totalM );
					countCacheServer++;
				}else{
					System.out.format("|%4d|%-67s|%8s|%8s|%8s|\n", 
							entry.getValue().getColumn(MemberData.NODE_ID), 
							entry.getValue().getColumn(MemberData.ROLE_NAME) + "(" + entry.getValue().getColumn(MemberData.ADDRESS) + ":" + entry.getValue().getColumn(MemberData.PORT) + ")",
							"N/A", "N/A", "N/A");
					
					countMember++;
				}
					
			}// for
			System.out.println("+----+-------------------------------------------------------------------+--------+--------+--------+");
			
		}
		// update the memory graph
		if (cTotalMemory != 0) {
			System.out.format("| %-98s|\n", "CoherenceServer (Total Memory Used / Total Memory) = " + NumberFormat.getNumberInstance(Locale.US).format(cTotalMemoryUsed) + "/"
					+ NumberFormat.getNumberInstance(Locale.US).format(cTotalMemory) + " (" + cTotalMemoryUsed * 100L / cTotalMemory + "%)");
		} else {
			System.out.format("| %-98s|\n", "No storage in Coherence");
		}
		System.out.format("| %-98s|\n", "Total Member Count(" + memberData.size() + ") = CacheServer Count(" + countCacheServer + ") + Others Count(" + countMember
				+ ")");
		System.out.println("+---------------------------------------------------------------------------------------------------+");

	}

	/**
	 * {@inheritDoc}
	 */
	public void updateData() {
		memberData = model.getData(VisualVMModel.DataType.MEMBER);
	}

}
