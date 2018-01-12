import static org.junit.Assert.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import org.junit.Test;


public class VectorSortTest {
	
	/*
	 * This tests checks to see if the most simple form of reordering the queue by priority works. 
	 */
	
	@Test
	public void checkBasicQueue() throws RemoteException, IOException, NotBoundException, InterruptedException {
		
		Vector<Client> basicTestQueue = new Vector<Client>();
		
		Client clientLow = new ClientImplementation("127.0.0.1", "LOW-CLIENT" , "LOW");
		Client clientMed = new ClientImplementation("127.0.0.1", "MED-CLIENT" , "MEDIUM");
		Client clientHigh = new ClientImplementation("127.0.0.1", "HIGH-CLIENT" , "HIGH");
		Client clientVip = new ClientImplementation("127.0.0.1", "HIGH-CLIENT" , "VIP");
		
		basicTestQueue.add(clientLow);
		basicTestQueue.add(clientMed);
		basicTestQueue.add(clientVip);
		basicTestQueue.add(clientHigh);
		
		Vector<Client> expectedBasicQueue = new Vector<Client>();
		
		expectedBasicQueue.add(clientVip);
		expectedBasicQueue.add(clientHigh);
		expectedBasicQueue.add(clientMed);
		expectedBasicQueue.add(clientLow);
		
		basicTestQueue = VectorSorter.sortByPriority(basicTestQueue);
		assertEquals(expectedBasicQueue.toString(), basicTestQueue.toString());
	}
	
	/*
	 * Check queue with multiple low clients. 
	 */
		
	@Test
	public void checkMultipleLow() throws RemoteException, IOException, NotBoundException, InterruptedException {
		
		Vector<Client> multipleLowTestQueue = new Vector<Client>();
		
		Client clientLow1 = new ClientImplementation("127.0.0.1", "LOW-CLIENT1" , "LOW");
		Client clientLow2 = new ClientImplementation("127.0.0.1", "LOW-CLIENT2" , "LOW");
		Client clientLow3 = new ClientImplementation("127.0.0.1", "LOW_CLIENT3" , "LOW");
		clientLow1.setQueueArrivalTime(new Date());
		clientLow2.setQueueArrivalTime(new Date());
		clientLow3.setQueueArrivalTime(new Date());
		
		Client clientVip = new ClientImplementation("127.0.0.1", "VIP-CLIENT" , "VIP");
		
		multipleLowTestQueue.add(clientLow1);
		multipleLowTestQueue.add(clientLow2);
		multipleLowTestQueue.add(clientVip);
		multipleLowTestQueue.add(clientLow3);
		
		
		Vector<Client> expectedMultipleLowTestQueue = new Vector<Client>();
		
		expectedMultipleLowTestQueue.add(clientVip);
		expectedMultipleLowTestQueue.add(clientLow1);
		expectedMultipleLowTestQueue.add(clientLow2);
		expectedMultipleLowTestQueue.add(clientLow3);
		
		multipleLowTestQueue = VectorSorter.sortByPriority(multipleLowTestQueue);
		
		assertEquals(expectedMultipleLowTestQueue.toString(), multipleLowTestQueue.toString());
	}
	
	/*
	 * This test checks that clients of equal priority will be ordered according to their time of creation. 
	 * Sleep threads had to be included in between to emulate an actual environment. If the sleeeps
	 * are not included the order will be irrevelant and it will be a FIFO design. Which is not
	 * possible to test. 
	 */
	
	@Test
	public void checkTimeOrdering() throws RemoteException, IOException, InterruptedException, NotBoundException {
		
		Vector<Client> multipleLowTimeTestQueue = new Vector<Client>();
		
		Client clientLow1 = new ClientImplementation("127.0.0.1", "LOW-CLIENT1" , "LOW");
		Client clientLow2 = new ClientImplementation("127.0.0.1", "LOW-CLIENT2" , "LOW");
		Client clientLow3 = new ClientImplementation("127.0.0.1", "LOW_CLIENT3" , "LOW");
		Client clientLow4 = new ClientImplementation("127.0.0.1", "LOW_CLIENT4" , "LOW");
		clientLow1.setQueueArrivalTime(new Date());
		Thread.sleep(10);
		clientLow2.setQueueArrivalTime(new Date());
		Thread.sleep(10);
		clientLow3.setQueueArrivalTime(new Date());
		Thread.sleep(10);
		clientLow4.setQueueArrivalTime(new Date());
		Thread.sleep(10);
		
		multipleLowTimeTestQueue.add(clientLow2);
		multipleLowTimeTestQueue.add(clientLow1);
		multipleLowTimeTestQueue.add(clientLow4);
		multipleLowTimeTestQueue.add(clientLow3);
		
		
		Vector<Client> expectedMultipleLowTestQueue = new Vector<Client>();
		
		expectedMultipleLowTestQueue.add(clientLow1);
		expectedMultipleLowTestQueue.add(clientLow2);
		expectedMultipleLowTestQueue.add(clientLow3);
		expectedMultipleLowTestQueue.add(clientLow4);
			
		multipleLowTimeTestQueue = VectorSorter.sortByPriority(multipleLowTimeTestQueue);
		
		assertEquals(expectedMultipleLowTestQueue.toString(), multipleLowTimeTestQueue.toString());
	}
	
	/*
	 * This test is to check if the algorithm can handle multiple clients with varying levels of priorities, with different
	 * creation times being added in a strange order. Again sleeps are added to simulate the various clients being 
	 * launched. 
	 */
	
	@Test
	public void checkComplicatedOrdering() throws RemoteException, IOException, InterruptedException, NotBoundException {
		
		Vector<Client> complicatedTestQueue = new Vector<Client>();
		
		Client clientLow1 = new ClientImplementation("127.0.0.1", "LOW-CLIENT1" , "LOW");
		Client clientLow2 = new ClientImplementation("127.0.0.1", "LOW-CLIENT2" , "LOW");
		Client clientLow3 = new ClientImplementation("127.0.0.1", "LOW_CLIENT3" , "LOW");
		Client clientMedium1 = new ClientImplementation("127.0.0.1", "MEDIUM-CLIENT1", "MEDIUM");
		Client clientMedium2 = new ClientImplementation("127.0.0.1", "MEDIUM-CLIENT2", "MEDIUM");
		Client clientMedium3 = new ClientImplementation("127.0.0.1", "MEDIUM-CLIENT3", "MEDIUM");
		Client clientHigh1 = new ClientImplementation("127.0.0.1", "HIGH-CLIENT1", "HIGH");
		Client clientHigh2 = new ClientImplementation("127.0.0.1", "HIGH-CLIENT2", "HIGH");
		Client clientHigh3 = new ClientImplementation("127.0.0.1", "HIGH-CLIENT3", "HIGH");
		Client clientVIP1 = new ClientImplementation("127.0.0.1", "VIP-CLIENT1", "VIP");
		Client clientVIP2 = new ClientImplementation("127.0.0.1", "VIP-CLIENT2", "VIP");
		
		clientLow1.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientMedium1.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientHigh1.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientHigh2.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientVIP1.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientLow2.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientLow3.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientMedium2.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientMedium3.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientHigh3.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		clientVIP2.setQueueArrivalTime(new Date());
		Thread.sleep(100);
		
		complicatedTestQueue.add(clientLow1);
		complicatedTestQueue.add(clientMedium1);
		complicatedTestQueue.add(clientHigh1);
		complicatedTestQueue.add(clientHigh2);
		complicatedTestQueue.add(clientVIP1);
		complicatedTestQueue.add(clientLow2);
		complicatedTestQueue.add(clientLow3);
		complicatedTestQueue.add(clientMedium2);
		complicatedTestQueue.add(clientMedium3);
		complicatedTestQueue.add(clientHigh3);
		complicatedTestQueue.add(clientVIP2);
		
		
		Vector<Client> expectedMultipleLowTestQueue = new Vector<Client>();
		
		expectedMultipleLowTestQueue.add(clientVIP1);
		expectedMultipleLowTestQueue.add(clientVIP2);
		expectedMultipleLowTestQueue.add(clientHigh1);
		expectedMultipleLowTestQueue.add(clientHigh2);
		expectedMultipleLowTestQueue.add(clientHigh3);
		expectedMultipleLowTestQueue.add(clientMedium1);
		expectedMultipleLowTestQueue.add(clientMedium2);
		expectedMultipleLowTestQueue.add(clientMedium3);
		expectedMultipleLowTestQueue.add(clientLow1);
		expectedMultipleLowTestQueue.add(clientLow2);
		expectedMultipleLowTestQueue.add(clientLow3);
			
		complicatedTestQueue = VectorSorter.sortByPriority(complicatedTestQueue);
		
		assertEquals(expectedMultipleLowTestQueue.toString(), complicatedTestQueue.toString());
	}
}
