import static org.junit.Assert.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.junit.Test;

public class PriorityReseterTest {

	@Test
	public void resetClientAfterOneChange() throws RemoteException, IOException, NotBoundException, InterruptedException {
		
		Client client = new ClientImplementation("127.0.0.1", "Client", "LOW");
		client.setPriority(Priority.HIGH);
		assertEquals(Priority.HIGH, client.getCurrentPriority());
		PriorityReseter.resetPriority(client);
		assertEquals(Priority.LOW, client.getCurrentPriority());
		
	}
	
	@Test
	public void resetClientAfterMultipleChanges() throws RemoteException, IOException, NotBoundException, InterruptedException {
		Client client = new ClientImplementation("127.0.0.1", "Client", "LOW");
		client.setPriority(Priority.HIGH);
		assertEquals(Priority.HIGH, client.getCurrentPriority());
		client.setPriority(Priority.MEDIUM);
		assertEquals(Priority.MEDIUM, client.getCurrentPriority());
		client.setPriority(Priority.VIP);
		assertEquals(Priority.VIP, client.getCurrentPriority());
		PriorityReseter.resetPriority(client);
		assertEquals(Priority.LOW, client.getCurrentPriority());
	}

}
