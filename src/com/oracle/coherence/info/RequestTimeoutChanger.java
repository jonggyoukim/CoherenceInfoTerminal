package com.oracle.coherence.info;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.Attribute;
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
public class RequestTimeoutChanger {

	/**
	 * Connection to JMX MBean server.
	 */
	private static MBeanServerConnection server;

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
		int value = 30000;
		if (args.length == 2) {
			server = connect(args[0]);
			value = Integer.parseInt(args[1]);
		} else {
			System.out.println("Please Input url (ex:service:jmx:rmi://localhost:3000/jndi/rmi://localhost:9000/server) and timeoutValue(3000)");
			System.exit(1);
		}

		ObjectName objName;
		try {
			objName = new ObjectName("Coherence:type=Service,*");
			Set<ObjectName> sSet = server.queryNames(objName, null);
			System.out.println("Service object count = " + sSet.size());
			for (Iterator<ObjectName> cacheNameIter = sSet.iterator(); cacheNameIter.hasNext();) {
				ObjectName serviceObjName = cacheNameIter.next();
				long timeout = (Long)server.getAttribute(serviceObjName, "RequestTimeoutMillis");
				System.out.print(serviceObjName.toString() + " : " + timeout + "->" + value + " .. ");
				if (timeout == 0) {
					System.out.println("ignore");
				} else {
					Attribute att = new Attribute("RequestTimeoutMillis", value);
					server.setAttribute(serviceObjName, att);
					System.out.println("done");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
