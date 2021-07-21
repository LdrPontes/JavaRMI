package entities;

import java.rmi.*;
import java.security.PublicKey;

public interface IUser extends Remote {
    public void notify(String text) throws RemoteException;

    public String getName() throws RemoteException;

    public String getTelephone() throws RemoteException;

    public PublicKey getPublicKey() throws RemoteException;

    public void notifyRide(IUser user) throws RemoteException;

    public void notifyClient(IUser user) throws RemoteException;
}