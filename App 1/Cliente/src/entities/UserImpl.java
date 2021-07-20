package entities;

import java.rmi.RemoteException;
import java.rmi.server.*;
import java.security.PrivateKey;
import java.security.PublicKey;

import sign.DigitalSignReceiver;
import sign.DigitalSignSender;

public class UserImpl extends UnicastRemoteObject implements IUser {
    private DigitalSignSender sender = new DigitalSignSender();
    private DigitalSignReceiver receiver = new DigitalSignReceiver();

    private IServer serverRef;
    private String name;
    private String telephone;

    private String start;
    private String end;
    private String date;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public UserImpl(String name, String telephone, IServer serverRef) throws RemoteException {
        this.name = name;
        this.telephone = telephone;

        this.setKeyPair();

        this.serverRef = serverRef;
        this.serverRef.signUp(this);
    }

    private void setKeyPair() {
        this.sender.generateDigitalSign(this.telephone);

        this.privateKey = sender.getPrivateKey();
        this.publicKey = sender.getPublicKey();
    }

    @Override
    public void notify(String text) throws RemoteException {
        System.out.println(text);
    }

    public IServer getServerRef() {
        return this.serverRef;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String getTelephone() throws RemoteException {
        return telephone;
    }

    @Override
    public String getPublicKey() throws RemoteException {
        return publicKey;
    }

    @Override
    public void setRideInterest(String start, String end, String date) throws RemoteException {
        this.start = start;
        this.end = end;
        this.date = date;

        this.serverRef.registerInterest(this);
    }

    @Override
    public String getStart() throws RemoteException {
        return this.start;
    }

    @Override
    public String getEnd() throws RemoteException {
        return this.end;
    }

    @Override
    public String getDate() throws RemoteException {
        return this.date;
    }

    @Override
    public void notifyRide(IUser user) throws RemoteException {
        System.out.println("\n\nNotificação de Carona:");
        System.out.println("\tMotorista: " + user.getName());
        System.out.println("\tContato: " + user.getTelephone());
    }

    @Override
    public void notifyClient(IUser user) throws RemoteException {
        System.out.println("\n\nNotificação de Passageiro:");
        System.out.println("\tNome: " + user.getName());
        System.out.println("\tContato: " + user.getTelephone());
    }
}