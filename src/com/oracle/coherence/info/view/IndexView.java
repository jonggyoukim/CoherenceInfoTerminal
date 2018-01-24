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
import java.util.StringTokenizer;

import javax.management.MBeanServerConnection;

import com.oracle.coherence.info.helper.VisualVMModel;
import com.oracle.coherence.info.model.CacheStorageManagerData;
import com.oracle.coherence.info.model.Data;
import com.oracle.coherence.info.model.DataRetriever;
import com.oracle.coherence.info.model.Pair;

public class IndexView extends AbstractView {
	private static final long serialVersionUID = 2602085070795849149L;

	private List<Entry<Object, Data>> cacheData;

	public IndexView(MBeanServerConnection server, VisualVMModel model) {
		super(server, model);
	}

	public void printData() {

		double totalSize = 0;

		for (Entry<Object, Data> cacheEntity : cacheData) {
			Pair<String, String> selectedCache = (Pair<String, String>) cacheEntity.getKey();
			double size = 0.0;

			// System.out.println("key = " + selectedCache);
			model.setSelectedCache(selectedCache);
			// System.out.println("getSelectedCache() = " +
			// model.getSelectedCache());

			DataRetriever cacheStorageManager = model.getDataRetrieverInstance(CacheStorageManagerData.class);
			// System.out.println("------ cacheStorageManager = " +
			// cacheStorageManager);

			List<Entry<Object, Data>> cacheStorageManagerData = cacheStorageManager.getJMXData(server, model);
			if (cacheStorageManagerData != null) {
				int nodecount = 0;
				int indexcount = 0;
				for (Entry<Object, Data> cacheStorageManagerEntity : cacheStorageManagerData) {
					String[] indexInfos = (String[]) cacheStorageManagerEntity.getValue().getColumn(CacheStorageManagerData.INDEX_INFO);
					//System.out.println("-" + cacheStorageManagerEntity.getKey());
					if(indexInfos.length != 0)
						nodecount++;
					for (int i = 0; i < indexInfos.length; i++) {
						//System.out.println(cacheStorageManagerEntity.getKey() + ":" + i + " = [" + indexInfos[i] + "]");
						StringTokenizer st = new StringTokenizer(indexInfos[i], ", ");
						indexcount = 0;
						while (st.hasMoreTokens()) {
							String s = st.nextToken();
							indexcount++;
							if (s.startsWith("Footprint")) {
//								System.out.println("[" + s+"]");
								size += toByte(s.substring(s.indexOf("=") + 1));
							} else if (s.startsWith("Content")) {
								// System.out.println(s);
							} else {

							}
						}
					}
				}// for
				System.out.println("[ " + selectedCache + " ] " + nodecount + "*" + indexcount + " index size = " +  NumberFormat.getNumberInstance(Locale.US).format(size));
				totalSize += size;
			} else {
				System.out.println("[ " + selectedCache + " ] index size = 0");
			}

		}// for
		System.out.println("Total index size = " + fromByte(totalSize));
		System.out.println("====================== " + hdf.format(new Date()) + " ======================");
	}

	@SuppressWarnings("unchecked")
	public void updateData() {
		cacheData = model.getData(VisualVMModel.DataType.CACHE);
	}

}
