package com.oracle.coherence.info;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.oracle.coherence.info.helper.VisualVMModel;
import com.oracle.coherence.info.view.AbstractView;
import com.oracle.coherence.info.view.CacheView;
import com.oracle.coherence.info.view.IndexView;
import com.oracle.coherence.info.view.MachineView;
import com.oracle.coherence.info.view.MemberView;

public class CoherenceInfo {
	private static MBeanServerConnection server;
	private static VisualVMModel model = VisualVMModel.getInstance();

	public static void main(String args[]) throws IOException {
		System.out.println("\n\r");
		System.out.println("+---------------------------------------------------------------------+");
		System.out.println("|   Coherence Information by JongGyou Kim (jonggyou.kim@oracle.com)   |");
		System.out.println("+---------------------------------------------------------------------+");
		if (args.length == 0) {
			System.out.println("| Use default JMX_URL :                                               |");
			System.out.println("| service:jmx:rmi://localhost:3000/jndi/rmi://localhost:9000/server   |");
			server = connect("service:jmx:rmi://localhost:3000/jndi/rmi://localhost:9000/server");
		} else if (args.length == 1) {
			System.out.println("| Use JMX_URL :                                                       |");
			System.out.format("|%-70s|\n", args[0]);
			server = connect(args[0]);
		} else {
			System.out.println("Please input parameter {jmx_url}");
			System.out.println("Usage : CoherenceInfo service:jmx:rmi://localhost:3000/jndi/rmi://localhost:9000/server");
			System.exit(1);
		}
		System.out.println("+---------------------------------------------------------------------+");

		final HashSet<AbstractView> viewSet = new HashSet<AbstractView>();

		String str = null;
		int select = 0;
		int interval = 0;

		Scanner scan = new Scanner(System.in);
		while (true) {

			System.out.println("");
			System.out.println("1. See member info");
			System.out.println("2. See cache info");
			System.out.println("3. See index info");
			System.out.println("4. See machine info");
			System.out.println("Q. Quit");
			System.out.print("\nSelect : ");
			try {
				str = scan.next();
				if (str.equalsIgnoreCase("Q"))
					break;
				select = Integer.parseInt(str);
			} catch (Exception e) {
				// e.printStackTrace();
				continue;
			}

			if (select > 0 && select <= 4) {
				System.out.print("Interval(sec) : ");
				try {
					str = scan.next();
					interval = Integer.parseInt(str);
				} catch (Exception e) {
					// e.printStackTrace();
					continue;
				}
				viewSet.clear();

				if (select == 1) {
					viewSet.add(new MemberView(server, model));
				} else if (select == 2) {
					viewSet.add(new CacheView(server, model));
				} else if (select == 3) {
					viewSet.add(new IndexView(server, model));
				} else if (select == 4) {
					viewSet.add(new MachineView(server, model));
				}

				if (viewSet.size() > 0) {
					TimerTask timerTask = new TimerTask() {
						@Override
						public void run() {

							model.refreshJMXStatistics(server);
							// update
							for (AbstractView view : viewSet) {
								view.updateData();
							}
							// print
							for (AbstractView view : viewSet) {
								view.printData();
							}
							// System.out.println("==========================================");
							System.out.println("Quit (q + Enter)");
							System.out.println("\n\n\n");

						}
					};
					Timer timer = new Timer("Timer thread");
					timer.schedule(timerTask, 0, interval * 1000);

					while (true) {
						if (scan.next().equalsIgnoreCase("q")) {
							timer.cancel();
							break;
						}
					}
				}

			}
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
