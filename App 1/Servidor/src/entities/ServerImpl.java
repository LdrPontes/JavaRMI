package entities;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;

import sign.DigitalSignSender;
import sign.DigitalSignReceiver;

public class ServerImpl extends UnicastRemoteObject implements IServer {
    private DigitalSignSender sender = new DigitalSignSender();
    private DigitalSignReceiver receiver = new DigitalSignReceiver();

    private ArrayList<Travel> travelDriverList = new ArrayList<Travel>();
    private ArrayList<Travel> travelPassengerList = new ArrayList<Travel>();

    private Registry nameService = LocateRegistry.createRegistry(1099);

    private int nextRideID = 1;

    public ServerImpl() throws RemoteException {
        super();
        this.nameService.rebind("Server", this);
    }

    @Override
    public void signUp(IUser client) throws RemoteException {
        this.nameService.rebind(client.getTelephone(), client);
        client.notify("\nBem-vindo " + client.getName());
    }

    @Override
    public int registerRideInterest(IUser client, String start, String end, String date, byte[] sign)
            throws RemoteException {

        int rideId = nextRideID++;
        
        IUser user = (IUser) this.nameService.lookup(client.getTelephone());
 
        this.receiver.getExternalMessage(user.getPublicKey(), start + end + date, sign);

        this.travelPassengerList.add(new Travel(rideId, user, start, end, date));

        this.notifyDrivers(rideId);

        return rideId;
    }

    @Override
    public int registerPassengerInterest(IUser client, String start, String end, String date, int numberPassenger,
            byte[] sign) throws RemoteException {
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
                result.concat(travel.getUser().getName() + "\n\n");
        }

        return result;

    }

    public void notifyPassengers(int idTravel) {
        
        Travel newTravel;
        
        for (Travel travel : this.travelDriverList) {
            if (travel.getID() == idTravel) {
                newTravel = travel;
                break;
            }
        }

        for (Travel travel : this.travelPassengerList) {
            if (travel.compare(newTravel)) {
                
                travel.getUser().notifyRide(newTravel.getUser());

            }
        }
    }

    public void notifyDrivers(int newInterest) {

         
        Travel newTravel;
        
        for (Travel travel : this.travelPassengerList) {
            if (travel.getID() == idTravel) {
                newTravel = travel;
                break;
            }
        }

        for (Travel travel : this.travelDriverList) {
            if (travel.compare(newTravel)) {
                
                travel.getUser().notifyPassengers(newTravel.getUser());
                
            }
        }

    }
}