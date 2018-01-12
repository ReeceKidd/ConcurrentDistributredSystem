import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

public interface Client extends Remote {
	
	public boolean reply() throws RemoteException;
	public void criticalSection() throws RemoteException;
	public String getID() throws RemoteException;
	public String getClientInfo() throws RemoteException;
	public Priority getInitialPriority() throws RemoteException;
	public Priority getCurrentPriority() throws RemoteException;
	public void setPriority(Priority priority) throws RemoteException;
	public void setQueueArrivalTime(Date arrivalTime) throws RemoteException;
	public Date getQueueArrivalTime() throws RemoteException;
	public long getCurrentPriorityWaitingTime(Date date) throws RemoteException;
	public long getTotalWaitingTime(Date date) throws RemoteException;
	public void increaseRunTimes() throws RemoteException;
	public int getTotalRunTimes() throws RemoteException;
	public Vector<Client> getCopyOfCurrentQueue() throws RemoteException;
	public void setCopyOfCurrentQueue(Vector<Client> copyOfCurrentQueue) throws RemoteException;
	public boolean ping() throws RemoteException;
	public void exit() throws RemoteException;
	
}

