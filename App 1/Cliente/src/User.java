import java.rmi.*;
import java.rmi.registry.*;

import entities.*;

import java.util.Scanner;

public class User {
    public static void main(String[] args) throws Exception {
        Registry NamesService = LocateRegistry.getRegistry("localhost", 1099);
        IServer server = (IServer) NamesService.lookup("Server");

        Scanner scan = new Scanner(System.in);
        
        System.out.print("\n\nInforme seu nome: ");
        String name = scan.nextLine();

        System.out.print("Informe seu telefone: ");
        String telephone = scan.nextLine();

        UserImpl userImpl = new UserImpl(name, telephone, server); 
        
    }
}
