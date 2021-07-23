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
        //this.nameService.rebind(client.getTelephone(), UnicastRemoteObject.exportObject(client, 1099));
        client.notify("\nBem-vindo " + client.getName());
    }

    @Override
    public int registerRideInterest(IUser client, String start, String end, String date, byte[] sign)
            throws RemoteException, NotBoundException {

        int rideId = nextRideID++;

        //IUser user = (IUser) this.nameService.lookup(client.getTelephone());

        this.receiver.getExternalMessage(client.getPublicKey(), start + end + date, sign);

        this.travelPassengerList.add(new Travel(rideId, client, start, end, date));

        this.notifyDrivers(rideId);

        return rideId;
    }

    @Override
    public int registerPassengerInterest(IUser client, String start, String end, String date, int numberPassenger,
            byte[] sign) throws RemoteException, NotBoundException {
        int rideId = nextRideID++;

        //IUser user = (IUser) this.nameService.lookup(client.getTelephone());

        this.receiver.getExternalMessage(client.getPublicKey(), start + end + date + numberPassenger, sign);

        this.travelDriverList.add(new Travel(rideId, client, start, end, date, numberPassenger));

        this.notifyPassengers(rideId);

        return rideId;
    }

    @Override
    public String cancelRideInterest(int id, byte[] sign) throws RemoteException {

        int index = -1;

        for (Travel travel: travelPassengerList) {
            if (travel.getID() == id) {
                index = travelPassengerList.indexOf(travel);
                break;
            }
        }

        if (index != -1) {
            travelPassengerList.remove(index);
            return "Removida com sucesso!";
        } else {
            return "Não foi possível encontrar o registro.";
        }
        
    }

    @Override
    public String cancelPassengerInterest(int id, byte[] sign) throws RemoteException {

        int index = -1;

        for (Travel travel: travelDriverList) {
            if (travel.getID() == id) {
                index = travelDriverList.indexOf(travel);
                break;
            }
        }

        if (index != -1) {
            travelDriverList.remove(index);
            return "Removida com sucesso!";
        } else {
            return "Não foi possível encontrar o registro.";
        }

    }

    @Override
    public String consultRide(String start, String end, String date) throws RemoteException {

        String result = "";
        Travel thisTravel = new Travel(-1, null, start, end, date);

        for (Travel travel : this.travelDriverList) {

            if (thisTravel.compare(travel))
                result = result.concat(travel.getUser().getName() + "\n\n");
        }

        if (result.equals(""))
            result = "Não há caronas disponíveis com estes requisitos";

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

        if (newTravel == null) {
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

        if (newTravel == null) {
            return;
        }

        for (Travel travel : this.travelDriverList) {
            if (travel.compare(newTravel)) {

                travel.getUser().notifyClient(newTravel.getUser());

            }
        }

    }
}