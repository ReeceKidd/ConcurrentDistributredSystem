import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

import org.junit.Test;

import static org.junit.Assert.*;

/*
 * The following tests are to see if the method that updates Client priorities based on waiting time is working. 
 * Some of the following tests take some time to run because of the required sleep time to emulate real world application. 
 */

public class PriorityUpdaterTest {
	
	/*
	 * The following three tests are here to check that the individual priority conditions are correct. 
	 * It checks a client that should not have been upgraded and a client that should have been upgraded in a certain time. 
	 */
	
	@Test
	public void checkLowPriority() throws RemoteException, IOException, InterruptedException, NotBoundException {
		
		Vector<Client> queue = new Vector<Client>();
		Client changePriority = new ClientImplementation("127.00.001", "LOW-Change priority client", "LOW");
		Client samePriority = new ClientImplementation("127.00.001", "LOW-Same priority client", "LOW");
		changePriority.setQueueArrivalTime(new Date());
		Thread.sleep(42000);
		samePriority.setQueueArrivalTime(new Date());
		queue.add(changePriority);
		queue.add(samePriority);
		PriorityUpdater.updatePriorities(queue);
		assertEquals(Priority.MEDIUM, changePriority.getCurrentPriority());
		assertEquals(Priority.LOW, samePriority.getCurrentPriority());
	}
	
	@Test
	public void checkMediumPriority() throws RemoteException, IOException, InterruptedException, NotBoundException {
		
		Vector<Client> queue = new Vector<Client>();
		Client changePriority = new ClientImplementation("127.00.001", "MEDIUM-Change priority client", "MEDIUM");
		Client samePriority = new ClientImplementation("127.00.001", "MEDIUM-Same priority client", "MEDIUM");
		changePriority.setQueueArrivalTime(new Date());
		Thread.sleep(26000);
		samePriority.setQueueArrivalTime(new Date());
		queue.add(samePriority);
		queue.add(changePriority);
		PriorityUpdater.updatePriorities(queue);
		assertEquals(Priority.HIGH, changePriority.getCurrentPriority());
		assertEquals(Priority.MEDIUM, samePriority.getCurrentPriority());
	}
	
	@Test
	public void checkHighPriority() throws RemoteException, IOException, InterruptedException, NotBoundException {
		
		Vector<Client> queue = new Vector<Client>();
		Client changePriority = new ClientImplementation("127.00.001", "HIGH-Change priority client", "HIGH");
		Client samePriority = new ClientImplementation("127.00.001", "HIGH-Same priority client", "HIGH");
		changePriority.setQueueArrivalTime(new Date());
		Thread.sleep(11000);
		samePriority.setQueueArrivalTime(new Date());
		Thread.sleep(3000);
		queue.add(changePriority);
		queue.add(samePriority);
		PriorityUpdater.updatePriorities(queue);
		assertEquals(Priority.VIP, changePriority.getCurrentPriority());
		assertEquals(Priority.HIGH, samePriority.getCurrentPriority());
	}
	
	/*
	 * The following test is to ensure the VIP client is not being upgraded or downgraded after a certain time. 
	 */
	
	@Test
	public void checkVIPPriority() throws RemoteException, IOException, InterruptedException, NotBoundException {
		
		Vector<Client> queue = new Vector<Client>();
		Client vipClient = new ClientImplementation("127.00.001", "VIP-Change priority client", "VIP");
		vipClient.setQueueArrivalTime(new Date());
		Thread.sleep(20000);
		queue.add(vipClient);
		PriorityUpdater.updatePriorities(queue);
		assertEquals(Priority.VIP, vipClient.getCurrentPriority());
	
	}

	/*
	 * This checks if low priority values as a representation for the same priorities are being handled correctly. 3 clients
	 * are created over 4 seconds which should result in two clients being
	 * upgraded to MEDIUM priority.
	 */

	@Test
	public void checkIfSamePrioritiesAreBeingUpdated() throws RemoteException, IOException, InterruptedException, NotBoundException {

		Vector<Client> lowPriorityQueue = new Vector<Client>();

		Client clientLow1 = new ClientImplementation("127.0.0.1", "LOW-CLIENT" , "LOW");
		Client clientLow2 = new ClientImplementation("127.0.0.1", "LOW-CLIENT" , "LOW");
		Client clientLow3 = new ClientImplementation("127.0.0.1", "LOW-CLIENT" , "LOW");
		clientLow1.setQueueArrivalTime(new Date());
		Thread.sleep(10000);
		clientLow2.setQueueArrivalTime(new Date());
		Thread.sleep(20000);
		clientLow3.setQueueArrivalTime(new Date());
		Thread.sleep(12000);
		lowPriorityQueue.add(clientLow1);
		lowPriorityQueue.add(clientLow2);
		lowPriorityQueue.add(clientLow3);
		lowPriorityQueue = PriorityUpdater.updatePriorities(lowPriorityQueue);
		assertEquals(Priority.MEDIUM, clientLow1.getCurrentPriority());
		assertEquals(Priority.LOW, clientLow2.getCurrentPriority());
		assertEquals(Priority.LOW, clientLow3.getCurrentPriority());
	}

	@Test
	public void mixedPriorityUpgrades() throws RemoteException, IOException, InterruptedException, NotBoundException {
		
		Vector<Client> mixedPriorityQueue = new Vector<Client>();

		Client clientMedium1 = new ClientImplementation("127.0.0.1", "MEDIUM-CLIENT1", "MEDIUM");
		Client clientHigh1 = new ClientImplementation("127.0.0.1", "HIGH-CLIENT1", "HIGH");
		Client clientLow1 = new ClientImplementation("127.0.0.1", "LOW-CLIENT1", "LOW");
		Client clientMedium2 = new ClientImplementation("127.0.0.1", "MEDIUM-CLIENT2", "MEDIUM");
		Client clientLow2 = new ClientImplementation("127.0.0.1", "LOW-CLIENT2", "LOW");
		Client clientVip1 = new ClientImplementation("127.0.0.1", "VIP_CLIENT1", "VIP");
		
		clientMedium1.setQueueArrivalTime(new Date());
		Thread.sleep(10000);
		clientLow1.setQueueArrivalTime(new Date());
		Thread.sleep(15000);
		clientHigh1.setQueueArrivalTime(new Date());
		Thread.sleep(5000);
		clientMedium2.setQueueArrivalTime(new Date());
		clientLow2.setQueueArrivalTime(new Date());
		Thread.sleep(5000);
		clientVip1.setQueueArrivalTime(new Date());
		Thread.sleep(10000);
		
		mixedPriorityQueue.add(clientMedium1);
		mixedPriorityQueue.add(clientLow1);
		mixedPriorityQueue.add(clientHigh1);
		mixedPriorityQueue.add(clientMedium2);
		mixedPriorityQueue.add(clientLow2);
		mixedPriorityQueue.add(clientVip1);
		
		mixedPriorityQueue = PriorityUpdater.updatePriorities(mixedPriorityQueue);
		
		assertEquals(Priority.HIGH, clientMedium1.getCurrentPriority());
		assertEquals(Priority.VIP, clientHigh1.getCurrentPriority());
		assertEquals(Priority.LOW, clientLow1.getCurrentPriority());
		assertEquals(Priority.MEDIUM, clientMedium2.getCurrentPriority());
		assertEquals(Priority.LOW, clientLow2.getCurrentPriority());
		assertEquals(Priority.VIP, clientVip1.getCurrentPriority());
	}

}
