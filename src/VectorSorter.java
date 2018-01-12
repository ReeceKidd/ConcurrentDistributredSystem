import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class VectorSorter {
	
	public static Vector<Client> sortByPriority(Vector<Client> queue) throws RemoteException {
		Collections.sort(queue, new Comparator<Client>() {
			@Override
			public int compare(Client client1, Client client2) {
				try {
					if (client1.getCurrentPriority() == client2.getCurrentPriority()) { 
						return client1.getQueueArrivalTime().compareTo(client2.getQueueArrivalTime());
					} else {
						return client1.getCurrentPriority().compareTo(client2.getCurrentPriority());
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
		return queue;
	}


}
