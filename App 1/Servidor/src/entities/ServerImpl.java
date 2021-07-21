package entities;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;

import sign.DigitalSignReceiver;
import entities.IServer;

public class ServerImpl extends UnicastRemoteObject implements IServer {
    private final DigitalSignReceiver receiver = new DigitalSignReceiver();

    private final ArrayList<Travel> travelDriverList = new ArrayList<Travel>();
    private final ArrayList<Travel> travelPassengerList = new ArrayList<Travel>();

    private final Registry nameService = LocateRegistry.createRegistry(1099);

    private int nextRideID = 1;

    public ServerImpl() throws RemoteException {
        super();
        this.nameService.rebind("Server", this);
    }

    @Override
    public void signUp(IUser client) throws RemoteException {
        this.nameService.rebind(client.getTelephone(), UnicastRemoteObject.exportObject(client, 1099));
        client.notify("\nBem-vindo " + client.getName());
    }

    @Override
    public int registerRideInterest(IUser client, String start, String end, String date, byte[] sign)
            throws RemoteException, NotBoundException {

        int rideId = nextRideID++;
        
        IUser user = (IUser) this.nameService.lookup(client.getTelephone());
 
        this.receiver.getExternalMessage(user.getPublicKey(), start + end + date, sign);

        this.travelPassengerList.add(new Travel(rideId, user, start, end, date));

        this.notifyDrivers(rideId);

        return rideId;
    }

    @Override
    public int registerPassengerInterest(IUser client, String start, String end, String date, int numberPassenger,
            byte[] sign) throws RemoteException, NotBoundException {
        int rideId = nextRideID++;

        IUser user = (IUser) this.nameService.lookup(client.getTelephone());

        this.receiver.getExternalMessage(user.getPublicKey(), start + end + date + numberPassenger, sign);

        this.travelPassengerList.add(new Travel(rideId, user, start, end, date, numberPassenger));

        this.notifyPassengers(rideId);

        return rideId;
    }

    @Override
    public void cancelRideInterest(int id, byte[] sign) throws RemoteException {

    }

    @Override
    public void cancelPassengerInterest(int id, byte[] sign) throws RemoteException {

    }

    @Override
    public String consultRide(String start, String end, String date) throws RemoteException{

        String result = "";
        Travel thisTravel = new Travel(-1, null, start, end, date);

        for (Travel travel: this.travelDriverList) {
            
            if (thisTravel.compare(travel))
                result = result.concat(travel.getUser().getName() + "\n\n");
        }

        return result;

    }

    public void notifyPassengers(int idTravel) throws RemoteException {
        
        Travel newTravel = null;
        
        for (Travel travel : this.travelDriverList) {
            if (travel.getID() == idTravel) {
                newTravel = travel;
                break;
            }
        }

        if(newTravel == null) {
            return;
        }

        for (Travel travel : this.travelPassengerList) {
            if (travel.compare(newTravel)) {
                
                travel.getUser().notifyRide(newTravel.getUser());

            }
        }
    }

    public void notifyDrivers(int idTravel) throws RemoteException {

         
        Travel newTravel = null;
        
        for (Travel travel : this.travelPassengerList) {
            if (travel.getID() == idTravel) {
                newTravel = travel;
                break;
            }
        }

        if(newTravel == null) {
            return;
        }

        for (Travel travel : this.travelDriverList) {
            if (travel.compare(newTravel)) {
                
                travel.getUser().notifyClient(newTravel.getUser());
                
            }
        }

    }
}