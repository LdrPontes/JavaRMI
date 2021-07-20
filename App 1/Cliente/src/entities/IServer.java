package entities;

import java.rmi.*;
import java.util.ArrayList;

public interface IServer extends Remote {
    private ArrayList<IUser> clients;

    public void signUp(IUser client) throws RemoteException;

    public int registerInterest(IUser client, String start, String end, String date) throws RemoteException;

    public void cancelInterest(int id) throws RemoteException;

    public ArrayList<String> consultRide(String start, String end, String date) throws RemoteException;
}