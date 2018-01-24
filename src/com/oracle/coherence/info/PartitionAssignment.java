package com.oracle.coherence.info;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * Test driver for panels.
 * 
 * @author Tim Middleton.
 */
public class PartitionAssignment {

	/**
	 * Connection to JMX MBean server.
	 */
	private static MBeanServerConnection server;
	private static String objectNameStr;

	/**
	 * Main entry point to test the plugin. This will create a panel with the
	 * tabs to display the various statistics outside of JVisualVM. Provide a
	 * hostname and port to connect to. The default is localhost and 10001.<br>
	 * 
	 * Note: You must also supply the jvisualvm dependencies (identified in
	 * pom.xml), on the classpath to run this.
	 * 
	 * @param args
	 *            arguments to main hostname and port
	 * 
	 * @throws Exception
	 *             if timer is interrupted
	 */
	public static void main(String[] args) throws Exception {
		int interval = 1000;
		if (args.length == 2) {
			if (args[0].equals("default"))
				server = connect("service:jmx:rmi://localhost:3000/jndi/rmi://localhost:9000/server");
			else
				server = connect(args[0]);
			objectNameStr = args[1];

		} else if (args.length == 3) {
			server = connect(args[0]);
			interval = Integer.parseInt(args[1]);
			objectNameStr = args[2];
		} else {
			System.out.println("Please Input url (ex:service:jmx:rmi://localhost:3000/jndi/rmi://localhost:9000/server)");
			System.exit(1);
		}
		
		
		// A timer task to update GUI per each interval
		TimerTask timerTask = new TimerTask() {
			SimpleDateFormat hdf = new SimpleDateFormat("hh:mm:ss.SSS");
			SimpleDateFormat df = new SimpleDateFormat("mm:ss.SSS");
			long startt = 0;
			long endt = 0;
			long loop = 0;
			
			@Override
			public void run() {
				ObjectName objName;
				try {
					objName = new ObjectName(objectNameStr);
					Integer count = (Integer) server.getAttribute(objName, "RemainingDistributionCount");
					System.out.println(hdf.format(new Date()) + " RemainingDistributionCount = " + count);
					if(count != 0){
						if(loop == 0){
							startt = System.currentTimeMillis();
						}
						loop++;
					} else {
						if(loop != 0){
							endt = System.currentTimeMillis();
							System.out.println("--------> : " + df.format(endt - startt));
							startt = 0;
							endt = 0;
						}
						loop = 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		// refresh every 15 seconds
		Timer timer = new Timer("TestPanel Sampling thread");
		timer.schedule(timerTask, 0, interval);

	}

	private static MBeanServerConnection connect(String urlPath) {
		MBeanServerConnection server = null;

		try {
			JMXServiceURL url = new JMXServiceURL(urlPath);
			JMXConnector jmxc = JMXConnectorFactory.connect(url);

			server = jmxc.getMBeanServerConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("\nCommunication error: " + e.getMessage());
			System.exit(1);
		}

		return server;
	}
}
