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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.management.MBeanServerConnection;

import com.oracle.coherence.info.helper.VisualVMModel;
import com.oracle.coherence.info.model.CacheData;
import com.oracle.coherence.info.model.Data;

/**
 * An implementation of an {@link AbstractCoherencePanel} to view various
 * overview graphs for a Coherence cluster.
 * 
 * @author Tim Middleton
 */
public class CacheDetailView extends AbstractView {
	private static final long serialVersionUID = 2602085070795849149L;

	private List<Entry<Object, Data>> cacheData;

	public CacheDetailView(MBeanServerConnection server, VisualVMModel model) {
		super(server, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void printData() {
		if (cacheData != null) {
			
			// float cTotalCacheSize = 0.0f;
			// for (Entry<Object, Data> entry : cacheData)
			// {
			// cTotalCacheSize += new Float((Integer)
			// entry.getValue().getColumn(CacheData.MEMORY_USAGE_MB));
			// }

			long cTotalCacheSize = 0;
			//System.out.println("- Memory Usage Bytes ---------------------------------");
			for (Entry<Object, Data> entry : cacheData) {
				long size = (Long) entry.getValue().getColumn(CacheData.MEMORY_USAGE_BYTES);
				cTotalCacheSize += size;
				//System.out.format("%-40s = %5s\n", entry.getValue().getColumn(CacheData.CACHE_NAME), super.fromByte(size));
			}
			System.out.println("Total Cache Count = " + cacheData.size() + ",\t\tDate: " + hdf.format(new Date()));
			System.out.println("Total Used Cache Memory Size = " + super.fromByte(cTotalCacheSize));
			System.out.println("+-----------------------------------------------------------+--------------+--------------+");
			System.out.println("|                           Cache Name                      |      Size    | Size/Total % |");
			System.out.println("+-----------------------------------------------------------+--------------+--------------+");
			for (Entry<Object, Data> entry : cacheData) {
				long size = (Long) entry.getValue().getColumn(CacheData.MEMORY_USAGE_BYTES);
				System.out.format("|%-59s|%14s|%12.2f%s|\n", 
						entry.getValue().getColumn(CacheData.CACHE_NAME), 
						super.fromByte(size), 
						((double) size * 100) / cTotalCacheSize, " %");
			}
			System.out.println("+-----------------------------------------------------------+--------------+--------------+");
		} else {
			System.out.println("No cache in Coherence");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateData() {
		cacheData = model.getData(VisualVMModel.DataType.CACHE);
	}

}
