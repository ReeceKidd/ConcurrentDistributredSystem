
import java.rmi.RemoteException;
//Reply thread is the communication between a client and a coordinator. 

public class ReplyThread extends Thread {

	private Client client;
	private Coordinator coordinator;

	public ReplyThread(Client client, Coordinator coordinator) {
	
		this.client = client;
		this.coordinator = coordinator;
	}

	public void run() {

		System.out.println("Coordinator calling (remote) Reply.");
		try{
			if(client.reply())
				System.out.println("Reply successful.");
			else
				System.out.println("Reply failed.");
		} catch (RemoteException e){
			try {
				System.out.println("Client " + client.getID() + "failed to call Reply method-- Shutting down");
				client.exit();
				coordinator.closeAll();
			} catch (RemoteException e1) {
				System.out.println("Client failure");
				e1.printStackTrace();
			}
		}

	}

}
