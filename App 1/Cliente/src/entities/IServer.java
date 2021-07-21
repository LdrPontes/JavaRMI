package entities;

import java.rmi.*;

public interface IServer extends Remote {
	public void signUp(IUser client) throws RemoteException;

	public int registerRideInterest(IUser client, String start, String end, String date, byte[] sign)
			throws RemoteException;

	public int registerPassengerInterest(IUser client, String start, String end, String date, int numberPassenger,
			byte[] sign) throws RemoteException;

	public void cancelRideInterest(int id, byte[] sign) throws RemoteException;

	public void cancelPassengerInterest(int id, byte[] sign) throws RemoteException;

	public String consultRide(String start, String end, String date) throws RemoteException;
}