import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/*
 * Implementation of Client Interface
 */

public class ClientImplementation extends UnicastRemoteObject implements Client {

	private static final long serialVersionUID = 4302805538742648934L;

	private String clientID;
	private String hostName;
	private Priority initialPriority;
	private Priority currentPriority;
	private String serverHost;
	private Date queueArrival;
	private Date upgradedPriorityArrival;
	private DrawImage app;
	private int totalRunTimes;
	private Vector<Client> copyOfCurrentQueue;
	Coordinator remoteRef;

	/*
	 * The main change here is that the criticalSection() was created in order
	 * to enable testing
	 */

	public ClientImplementation(String serverHost, String id, String initialPriority)
			throws RemoteException, IOException, NotBoundException, InterruptedException {

		System.out.println("Client " + id + " created.");
		System.out.println("Initial priority is " + initialPriority);
		this.serverHost = serverHost;
		this.clientID = id;
		Priority convertedPriority = PriorityConvertor.convertStringToPriority(initialPriority);
		this.currentPriority = convertedPriority;
		this.initialPriority = currentPriority;
		InetAddress hostAddr = InetAddress.getLocalHost();
		this.hostName = hostAddr.getHostName();
		this.totalRunTimes = 0;
		this.upgradedPriorityArrival = new Date();
		printClientInfo();

	}

	public void criticalSection() throws RemoteException {
		String coordinatorAddress = "//" + serverHost + "/Coordinator";
		try {
			remoteRef = (Coordinator) Naming.lookup(coordinatorAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}

		try {

			app = new DrawImage();
			app.setTitle("Client: " + clientID + " -- Inititial priority: " + initialPriority
					+ " -- Current priority level is " + currentPriority);
			app.pack();
			app.setLocation(10, 20);
			app.setVisible(true);
			System.out.println("Client " + hostName + " finished window.");

			synchronized (this) {

				while (true) {
					// REQUEST

					try {
						boolean req = false;
						while (!req) {
							System.out.println("Client " + clientID + " calling (remote) Request.");

							req = remoteRef.request(this);

							if (req)
								System.out.println("Client " + clientID + " Request successful.");
							else
								System.out.println("Client " + clientID + " Request failed.");
						}
					} catch (Exception e) {
						System.out.println("Client: " + clientID + "failed during request section");
						remoteRef.closeAll();
					}

					/*
					 * The coordinator active thread, creates a Thread to
					 * that pings the Coordinator to check it is still active.
					 */
					CoordinatorActiveThread coordinatorActiveThread = new CoordinatorActiveThread(remoteRef, this);
					coordinatorActiveThread.start();
					/*
					 * Client timer thread is used to determine if a thread has
					 * timed out during the critical section.
					 */
					ClientTimeout clientTimeout = new ClientTimeout(remoteRef, this);
					clientTimeout.start();

					wait();

					try {
						// WRITE TO FILE
						FileWriter fw = new FileWriter("ClientOutput.txt", true);
						PrintWriter pw = new PrintWriter(fw, true);
						Date date = new Date();
						String timeStamp = date.toString();
						System.out.println("Client : " + clientID + " isW witing to file: ClientOutput.txt");
						pw.println("Record from " + hostName + " is " + timeStamp + ". ClientID is: " + clientID);
						pw.println(getClientInfo());
						pw.close();
						fw.close();

						// DRAW IMAGE
						System.out.println("Client: " + clientID + hostName + " now drawing.");
						app.startDrawing();

						// RELEASE
						System.out.println("Client " + clientID + " calling (remote) Release.");

					} catch (RemoteException e) {
						System.out.println("Client failed during Critical section");
						remoteRef.closeAll();
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						boolean rel = false;
						while (!rel) {

							rel = remoteRef.release(this);

							if (rel)
								System.out.println("Client " + clientID + " Release successful.");
							else
								System.out.println("Client " + clientID + " Release failed.");
						}
					} catch (RemoteException e) {
						System.out.println("Client " + clientID + "failed during request");
						remoteRef.closeAll();
					}
				}
			}
		} catch (UnmarshalException e) {
			System.out.println("Client " + clientID + " diconnected during critical section");
			remoteRef.closeAll();
		} catch (RemoteException e) {
			System.out.println("Client " + clientID + "failed critical section exiting");
			remoteRef.closeAll();
		} catch (InterruptedException e1) {
			remoteRef.closeAll();
			e1.printStackTrace();
		}
	}

	public synchronized boolean reply() throws RemoteException {
		System.out.println("Client " + clientID + " reply has been invoked by Coordinator.");
		try {
			notify();
		} catch (IllegalMonitorStateException e) {
			System.out.println("Client " + clientID + "failed to notify");
			exit();
		}

		return true;
	}

	public void printClientInfo() {
		System.out.println("Client " + clientID + " host is " + hostName);
		System.out.println("Client priority level: #" + currentPriority);
		System.out.println("Server host is " + serverHost);
		System.out.println("Client " + hostName + " starting window.");
	}

	public String getClientInfo() throws RemoteException {
		String clientInfo = "Client " + clientID + " host is " + hostName + " --Client current priority level: # "
				+ currentPriority + " --Client initial priority " + initialPriority + " --Total run times " + totalRunTimes
				+ "Priority waiting times " + getCurrentPriorityWaitingTime(new Date()) + " --Total waiting time: "
				+ getTotalWaitingTime(new Date());
		return clientInfo;
	}

	public String getID() {
		return clientID;
	}

	public Priority getInitialPriority() throws RemoteException {
		return initialPriority;
	}

	public Priority getCurrentPriority() throws RemoteException {
		return currentPriority;
	}

	/*
	 * The queueArrival time needs to be reset here in order to ensure that the
	 * VIP client with the longest wait time is run first, instead of someone
	 * that has only been upgraded to VIP.
	 */

	public void setPriority(Priority newPriority) throws RemoteException {
		System.out.println("Client: " + clientID + " priority updated from " + currentPriority + " to " + newPriority);
		this.currentPriority = newPriority;
		upgradedPriorityArrival = new Date();
		try {
			app.setTitle("Client: " + clientID + " -- Inititial priority: " + initialPriority
					+ " -- Current priority level is " + currentPriority);
		} catch (NullPointerException e) {
			System.out.println("This is added to accommodate for the tests that access this method");
		}

	}

	public Date getQueueArrivalTime() throws RemoteException {
		return queueArrival;
	}

	public void setQueueArrivalTime(Date arrival) throws RemoteException {
		this.queueArrival = arrival;
		this.upgradedPriorityArrival = queueArrival;
	}

	public void increaseRunTimes() throws RemoteException {
		this.totalRunTimes++;
	}

	public int getTotalRunTimes() throws RemoteException {
		return totalRunTimes;
	}

	/*
	 * The two following methods return a time difference based on queue arrival
	 * times.
	 */

	public long getCurrentPriorityWaitingTime(Date date) throws RemoteException {
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		long diffInMillies = date.getTime() - upgradedPriorityArrival.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public long getTotalWaitingTime(Date date) throws RemoteException {
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		long diffInMillies = date.getTime() - queueArrival.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public Vector<Client> getCopyOfCurrentQueue() throws RemoteException {
		return copyOfCurrentQueue;
	}

	public void setCopyOfCurrentQueue(Vector<Client> copyOfCurrentQueue) throws RemoteException {
		this.copyOfCurrentQueue = copyOfCurrentQueue;
	}

	/*
	 * This is used to check if client is alive.
	 */

	public boolean ping() {
		return true;
	}

	public void exit() {
		System.exit(1);
	}

	public static void main(String[] args) throws NotBoundException, InterruptedException, RemoteException {
		try {
			Client client = new ClientImplementation(args[0], args[1], args[2]);
			client.criticalSection();
		} catch (ConnectException e) {
			System.out.println("---ERROR---");
			System.out.println("Client refused to connect to: Coordinator");
			System.out.println("Coordinator needs to be launched before Client");
		} catch (MalformedURLException e) {
			System.out.println("URL is not correct please use: 127.0.0.1");
		} catch (RemoteException e) {
			System.out.println("Client closed at " + new Date());
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}