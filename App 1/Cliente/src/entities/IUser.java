package entities;

import java.rmi.*;
import java.security.PublicKey;

public interface IUser extends Remote {
    void notify(String text) throws RemoteException;

    String getName() throws RemoteException;

    String getTelephone() throws RemoteException;

    PublicKey getPublicKey() throws RemoteException;

    void notifyRide(IUser user) throws RemoteException;

    void notifyClient(IUser user) throws RemoteException;
}