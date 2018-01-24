package com.oracle.coherence.info.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.oracle.coherence.info.helper.VisualVMModel;
import com.oracle.coherence.info.model.Data;
import com.oracle.coherence.info.model.Pair;
import com.oracle.coherence.info.model.PartitionAssignmentData;

public class PartitionAssignmentView extends AbstractView {
	List<Entry<Object, Data>> partitionAssignmentData;
	long startt = 0;
	long endt = 0;
	long loop = 0;

	public PartitionAssignmentView(MBeanServerConnection server, VisualVMModel model) {
		super(server, model);
	}

	@Override
	public void updateData() {
		partitionAssignmentData = getJMXData();

	}
	
	private List<Map.Entry<Object, Data>> getJMXData() {
		SortedMap<Object, Data> mapData = new TreeMap<Object, Data>();

		try {
			Set<ObjectName> paSet = server.queryNames(new ObjectName("Coherence:type=PartitionAssignment,*"), null);
//System.out.println("paSet = " + paSet.size());
			for (Iterator<ObjectName> cacheNameIter = paSet.iterator(); cacheNameIter.hasNext();) {
				PartitionAssignmentData data = new PartitionAssignmentData();

				ObjectName clusterObjName = cacheNameIter.next();
//System.out.println("clusterObjName = " + clusterObjName.toString());
				data.setColumn(0, server.getAttribute(clusterObjName, "RemainingDistributionCount"));
//System.out.println("data=" + data);

				String name = clusterObjName.toString();
				name = name.substring(name.indexOf("service="), name.indexOf(",responsibility="));
				Pair<String, String> key = new Pair<String, String>("PartitionAssignment", name);
				mapData.put(key, data);
			}

			return new ArrayList<Map.Entry<Object, Data>>(mapData.entrySet());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	@Override
	public void printData() {
		if (partitionAssignmentData != null && partitionAssignmentData.size() != 0) {
			for (Entry<Object, Data> entry : partitionAssignmentData) {
				Pair keys = (Pair)entry.getKey();
				PartitionAssignmentData data= (PartitionAssignmentData)entry.getValue();
				Integer count = (Integer) data.getColumn(0);

				if (count != 0) {
					if (!data.isLoop()) {
						data.setTstart(System.currentTimeMillis());
					}
					data.setLoop(true);
					System.out.println("["+ keys.getY() +"] RemainingDistributionCount = " + count);
				} else {
					if (data.isLoop()) {
						data.setTend(System.currentTimeMillis());
						System.out.println("["+ keys.getY() + "] --------> : " + df.format(data.getTend() - data.getTstart()));
					}
					data.setLoop(false);
				}
			}// for
		} else {// if
			System.out.println("Partition Assignment Data is not exist");
		}
		System.out.println("====================== " + hdf.format(new Date()) + " ======================");
	
	}
}
