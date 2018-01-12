import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Coordinator extends Remote {

	// This implements a non-blocking request
	public boolean request(Client client) throws RemoteException;
	
	// This implements a non-blocking release
	public boolean release(Client client) throws RemoteException;
	
	public void setPrioritizedQueue(Vector<Client> prioritizedQueue) throws RemoteException;
	public Vector<Client> getPrioritizedQueue() throws RemoteException;
	public Boolean ping() throws RemoteException;
	public void close() throws RemoteException;
	public void closeAll() throws RemoteException;
}
