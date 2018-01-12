import java.rmi.ConnectException;
import java.rmi.RemoteException;

/*
 * This is used to constantly ping the server to ensure it is active. 
 */
public class ClientTimeout extends Thread {
	
	private Coordinator coordinator;
	private Client client;

	public ClientTimeout(Coordinator coordinator, Client client){
		this.coordinator = coordinator;
		this.client = client;
	}
	
	public void run() {
		try {
			isClientFrozen();
		} catch (RemoteException | InterruptedException e) {
			try {
				System.out.println("Client " + client.getID() + "stuck in critical section");
				coordinator.closeAll();
			} catch (RemoteException e1) {
				try {
					coordinator.release(client);
				} catch (ConnectException x){
					System.out.println("Client exited during critical section");
				}
				catch (RemoteException e2) {
					System.out.println("Client exiting");
					e2.printStackTrace();
				}
			}
		}
	}
	public void isClientFrozen() throws RemoteException, InterruptedException{

		while(true) {
			Thread.sleep(20000);
			if(coordinator.getPrioritizedQueue().get(0).getTotalRunTimes() == client.getTotalRunTimes() && coordinator.getPrioritizedQueue().get(0).getID() == client.getID()) {
				coordinator.closeAll();
			};
		}
		
		
	}
	
}

