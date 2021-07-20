package entities;

import java.rmi.*;

public interface IUser extends Remote {
    public void notify(String text) throws RemoteException;

    public String getName() throws RemoteException;

    public String getTelephone() throws RemoteException;

    public String getPublicKey() throws RemoteException;
    
    public void setRideInterest(String start, String end, String date) throws RemoteException;

    public void notifyRide(IUser user) throws RemoteException;

    public void notifyClient(IUser user) throws RemoteException;
}