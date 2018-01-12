import java.rmi.RemoteException;
import java.util.Date;

/*
 * This is used to constantly ping the server to ensure it is acive. 
 */
public class CoordinatorActiveThread extends Thread {
	
	private Coordinator coordinator;
	private Client client;

	public CoordinatorActiveThread(Coordinator coordinator, Client client){
		this.coordinator = coordinator;
		this.client = client;
	}
	
	public void run() {
		try {
			isCoordinatorActive();
		} catch (RemoteException | InterruptedException e) {
			try {
				System.out.println("Client " + client.getID() + " could not connect with the Coordinator disconnected at: " + new Date());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			System.exit(1);
		}
	}
	public void isCoordinatorActive() throws RemoteException, InterruptedException{
		
		while(coordinator.ping()) {
			Thread.sleep(1000);
		}
		
	}
	
}
