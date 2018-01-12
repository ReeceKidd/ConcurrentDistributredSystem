import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

@SuppressWarnings("serial")
public class CoordinatorImplementation extends UnicastRemoteObject implements Coordinator {

	private Vector<Client> prioritizedQueue;

	public CoordinatorImplementation() throws RemoteException {
		System.out.println("Coordinator launched.");
		prioritizedQueue = new Vector<Client>();
		System.setSecurityManager(new SecurityManager());
	}
	
	public CoordinatorImplementation(Vector<Client> queue) throws RemoteException {
		System.out.println("Coordinator launched.");
		this.prioritizedQueue = queue;
		System.setSecurityManager(new SecurityManager());
	}

	public synchronized boolean request(Client client) throws RemoteException {

		
		Date clientEntryTime = new Date();
		System.out.println("Client: " + client.getID() + " entered coordinator request at "  + clientEntryTime);
		client.setQueueArrivalTime(clientEntryTime);
		
		prioritizedQueue.addElement(client);
		
		System.out.println("Client: " +client.getID() + " enqued at " + new Date());

		if(prioritizedQueue.size() == 1){
			ReplyThread replyThread = new ReplyThread(client, this);
			replyThread.start();
		}
		
		System.out.println("Current queue size: " + prioritizedQueue.size());
		System.out.println("Client: " + client.getID() + " request processed");

		return true;
	}

	public synchronized boolean release(Client client) throws RemoteException {
		System.out.println("Entered release");
		
		Client clientRef;
		
		PriorityReseter.resetPriority(prioritizedQueue.get(0));
		prioritizedQueue.get(0).increaseRunTimes();
		prioritizedQueue.removeElementAt(0);
		
		System.out.println("Coordinator released " + client.getID() + ". Queue size = " + prioritizedQueue.size());
		
		System.out.println("--Unordered Queue--");
		printPriorityQueue(prioritizedQueue);
		
		System.out.println("--Updating priorities of Clients in queue");
		PriorityUpdater.updatePriorities(prioritizedQueue);
		System.out.println("--Sorting queue based on updated priorities");
		VectorSorter.sortByPriority(prioritizedQueue);
		
		System.out.println("--Ordered queue--");
		printPriorityQueue(prioritizedQueue);
		
		/*
		 * Check to see if the queue is now empty in which case the client will need to be repeated. 
		 */
		
		if(prioritizedQueue.size() == 0){
			System.out.println("Coordinator only has one Client: " + client.getID());
			System.out.println("Restarting Client: " + client.getID() + " until another Client joins");
			ReplyThread replyThread = new ReplyThread(client, this);
			replyThread.start();
			return true;
		}
		
		clientRef = prioritizedQueue.get(0);
		
		//Outstanding clients to be serviced. 
		if (prioritizedQueue.size() >= 1) {
			ReplyThread replyThread = new ReplyThread(clientRef, this);
			replyThread.start();
		}
		return true;
	}
	
	public Vector<Client> getPrioritizedQueue() throws RemoteException {
		return prioritizedQueue;
	}
	
	public Boolean ping() throws RemoteException {
		return true;
	}
	
	public void close() throws RemoteException {
		System.out.println("Coordinator forced to shut down");
		System.exit(1);
	}
	
	public void closeAll() throws RemoteException {
		System.out.println("Coordinator will shut down all clients due to queue corruption");
		System.exit(1);
	}

	public void setPrioritizedQueue(Vector<Client> prioritizedQueue) throws RemoteException {
		System.out.println("Prioritized queue changed from: ");
		printPriorityQueue(prioritizedQueue);
		System.out.println("To:");
		this.prioritizedQueue = prioritizedQueue;
		printPriorityQueue(prioritizedQueue);
	}

	public void printPriorityQueue(Vector<Client> queue) throws RemoteException {
		for(int i = 0; i<prioritizedQueue.size(); i++){
			System.out.println("Client: " + prioritizedQueue.get(i).getID() + 
					" --Priority: " + prioritizedQueue.get(i).getCurrentPriority() + 
					" --Waiting time at current priority: " + prioritizedQueue.get(i).getCurrentPriorityWaitingTime(new Date()) + 
					" --Total runs: " + prioritizedQueue.get(i).getTotalRunTimes() +
					" --Total waiting time since last run: " + prioritizedQueue.get(i).getTotalWaitingTime(new Date()));
		}
	}

	public static void main(String args[]) {
		try {
			Coordinator server = new CoordinatorImplementation();
			Naming.rebind("//127.0.0.1/Coordinator", server);
			System.out.println("Server bound.");
		} catch (ConnectException e) {
			System.out.println("---ERROR---");
			System.out.println("Coordinator refused to connect to: " + "//127.0.0.1/Coordinator");
			System.out.println("RMI needs to be launched before Coordinator can be run.");
		} catch(Exception e){
			System.err.println(e);
			System.exit(1);
		}
	}

}
