package entities;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

import DigitalSignSender;
import DigitalSignReceiver;

public class ServerImpl extends UnicastRemoteObject implements IServer {
    private DigitalSignSender sender = new DigitalSignSender();
    private DigitalSignReceiver receiver = new DigitalSignReceiver();

    private Registry nameService = LocateRegistry.createRegistry(1099);

    public ServerImpl() throws RemoteException {
        super();
        nameService.rebind("Server", this);
    }

    @Override
    public void signUp(IUser client) throws RemoteException {
        nameService.rebind(client.getTelephone(), client);
        client.notify("\nBem-vindo " + client.getName());
    }

    @Override
    public ArrayList<String> consultRide(byte[] start, byte[] end, byte[] Date) throws RemoteException {



    }
}