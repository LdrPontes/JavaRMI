package entities;

import java.rmi.*;

public interface IServer extends Remote {
    void signUp(IUser client) throws RemoteException;

    int registerRideInterest(IUser client, String start, String end, String date, byte[] sign)
            throws RemoteException, NotBoundException;

    int registerPassengerInterest(IUser client, String start, String end, String date, int numberPassenger,
            byte[] sign) throws RemoteException, NotBoundException;

    String cancelRideInterest(int id, byte[] sign) throws RemoteException;

    String cancelPassengerInterest(int id, byte[] sign) throws RemoteException;

    String consultRide(String start, String end, String date) throws RemoteException;
}