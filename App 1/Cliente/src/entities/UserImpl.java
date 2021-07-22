package entities;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.security.PublicKey;

import sign.DigitalSignSender;

public class UserImpl extends UnicastRemoteObject implements IUser {
    private DigitalSignSender sender = new DigitalSignSender();

    private IServer serverRef;
    private String name;
    private String telephone;

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
    public PublicKey getPublicKey() throws RemoteException {
        return publicKey;
    }

    public void setRideInterest(String start, String end, String date) throws RemoteException, NotBoundException {
        byte[] sign = this.sender.generateDigitalSign(start + end + date);
        int id =  this.serverRef.registerRideInterest(this, start, end, date, sign);

        System.out.println("\n\nSeu id de interesse de carona é: " + id);
    }

    public void cancelRideInterest(int id) throws RemoteException {
        byte[] sign = this.sender.generateDigitalSign(String.valueOf(id));

        this.serverRef.cancelRideInterest(id, sign);
        System.out.println("\n\nSeu interesse de carona id " + id + " foi cancelado");
    }

    public void setPassengerInterest(String start, String end, String date, int passengerNumber) throws RemoteException, NotBoundException {
        byte[] sign = this.sender.generateDigitalSign(start + end + date + passengerNumber);
        int id =  this.serverRef.registerPassengerInterest(this, start, end, date, passengerNumber, sign);

        System.out.println("\n\nSeu id de interesse de passageiros é: " + id);
    }

    public void cancelPassengerInterest(int id) throws RemoteException {
        byte[] sign = this.sender.generateDigitalSign(String.valueOf(id));

        this.serverRef.cancelPassengerInterest(id, sign);
        System.out.println("\n\nSeu interesse de passageiros id " + id + " foi cancelado");
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