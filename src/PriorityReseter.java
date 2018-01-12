import java.rmi.RemoteException;

/*
 * This class resets the priority of a client to its initial priority. It makes use of the priority convertor class
 * to convert from the Priority ENUM type to a string. Again because of the arguments that had to be passed to the run instances. 
 * Also for useability as users will likely forget to add Priority. 
 */

public class PriorityReseter {
	
	public static Client resetPriority (Client client) throws RemoteException {
		System.out.println("Client: " + client.getID() + " priority level reset from: " + client.getCurrentPriority() + " to: " + client.getInitialPriority());
		client.setPriority((client.getInitialPriority()));
		return client;	
	}
}
