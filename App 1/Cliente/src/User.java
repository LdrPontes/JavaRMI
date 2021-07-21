import java.rmi.*;
import java.rmi.registry.*;

import entities.*;

import java.util.Scanner;

public class User {
    public static void main(String[] args) throws Exception {
        Registry namesService = LocateRegistry.getRegistry(1099);
        IServer server = (IServer) namesService.lookup("Server");

        Scanner scan = new Scanner(System.in);

        System.out.print("\n\nInforme seu nome: ");
        String name = scan.nextLine();

        System.out.print("Informe seu telefone: ");
        String telephone = scan.nextLine();

        UserImpl userImpl = new UserImpl(name, telephone, server);

        // clearScreen();

        System.out.println("\n\nEscolha uma opção: ");
        System.out.println("\t1 - Receber caronas");
        System.out.println("\t2 - Oferecer carona");

        int clientType = scan.nextInt();
        scan.nextLine();

        if (clientType == 1) {
            while (true) {
                // clearScreen();
                System.out.println("\n\nEscolha uma opção: ");
                System.out.println("\t1 - Consultar caronas");
                System.out.println("\t2 - Registrar interesse em eventos de caronas");
                System.out.println("\t3 - Sair \n");

                int nextOption = scan.nextInt();
                scan.nextLine();

                switch (nextOption) {
                    case 1: {
                        System.out.print("\n\nInforme o destino de origem: ");
                        String start = scan.nextLine();
                        System.out.print("Informe o destino final: ");
                        String end = scan.nextLine();
                        System.out.print("Informe a data (dd/mm/yyyy): ");
                        String date = scan.nextLine();

                        System.out.println("\n" + userImpl.getServerRef().consultRide(start, end, date));
                        break;
                    }
                    case 2: {
                        System.out.print("\n\nInforme o destino de origem: ");
                        String start = scan.nextLine();
                        System.out.print("Informe o destino final: ");
                        String end = scan.nextLine();
                        System.out.print("Informe a data (dd/mm/yyyy): ");
                        String date = scan.nextLine();

                        userImpl.setRideInterest(start, end, date);
                        break;
                    }

                    default:
                        return;
                }
            }
        } else {
            while (true) {
                clearScreen();
                System.out.println("\n\nEscolha uma opção: ");
                System.out.println("\t1 - Registrar interesse em eventos de passageiros");
                System.out.println("\t2 - Sair ");

                int nextOption = scan.nextInt();
                scan.nextLine();

                if (nextOption == 1) {
                    System.out.print("\n\nInforme o destino de origem: ");
                    String start = scan.nextLine();
                    System.out.print("\nInforme o destino final: ");
                    String end = scan.nextLine();
                    System.out.print("\nInforme a data (dd/mm/yyyy): ");
                    String date = scan.nextLine();
                    System.out.print("\nInforme o número de passageiros: ");
                    int passengerNumber = scan.nextInt();
                    scan.nextLine();

                    userImpl.setPassengerInterest(start, end, date, passengerNumber);
                } else {
                    return;
                }
            }
        }

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
