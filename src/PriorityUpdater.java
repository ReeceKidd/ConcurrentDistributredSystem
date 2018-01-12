import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class PriorityUpdater {

	public static Vector<Client> updatePriorities(Vector<Client> prioritizedQueue) throws RemoteException {

		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		
		/*
		 * This algorithm works by having acceptable wait times for each of the different priority levels. 
		 * LOW priority clients have a wait time of 40 seconds before being upgraded to a MEDIUM priority client. 
		 * MEDIUM priority clients have a wait time of 25 seconds before being upgraded to a HIGH priority client. 
		 * HIGH priority clients have a wait time of 10 seconds before being upgraded to a VIP priority client
		 * VIP clients do not get upgraded or down-graded. 
		 */
		
		long lowPriorityWaitingTime = timeUnit.convert((long) 40000, TimeUnit.MILLISECONDS); 
		long mediumPriorityWaitingTime = timeUnit.convert((long) 25000, TimeUnit.MILLISECONDS); 
		long highPriorityWaitingTime = timeUnit.convert((long) 10000, TimeUnit.MILLISECONDS); 

		Client currentClient = null;

		for (int i = 0; i < prioritizedQueue.size(); i++) {

			currentClient = prioritizedQueue.get(i);

			if (currentClient.getCurrentPriority().equals(Priority.LOW)) {
				
				if(currentClient.getCurrentPriorityWaitingTime(new Date())> lowPriorityWaitingTime){
					System.out.println("Client: " + currentClient.getID() + " priority is changing from " + currentClient.getCurrentPriority().toString() + " to MEDIUM");
					System.out.println("----current client " + currentClient.getID() + "priority is " + currentClient.getCurrentPriority());
					currentClient.setPriority(Priority.MEDIUM);
				}
				
				
			}

			else if (currentClient.getCurrentPriority().equals(Priority.MEDIUM)) {
				
				if(currentClient.getCurrentPriorityWaitingTime(new Date())> mediumPriorityWaitingTime){
					System.out.println("Client: " + currentClient.getID() + " priority is changing from " + currentClient.getCurrentPriority().toString() + " to HIGH");
					currentClient.setPriority(Priority.HIGH);
				
				}
				
				
			}

			else if (currentClient.getCurrentPriority().equals(Priority.HIGH)) {
				
				if(currentClient.getCurrentPriorityWaitingTime(new Date())> highPriorityWaitingTime){
					System.out.println("Client: " + currentClient.getID() + " priority is changing from " + currentClient.getCurrentPriority().toString() + " to VIP");
					currentClient.setPriority(Priority.VIP);
				}
				
				
			}

			else if (currentClient.getCurrentPriority().equals(Priority.VIP)) {
				System.out.println("VIP Client " + currentClient.getID() + " can not be upgraded further");
			}

		}
		return prioritizedQueue;

	}

	

}
