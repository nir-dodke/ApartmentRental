package com.rental.client.renting;

import com.rental.renting.grpc.Void;
import com.rental.renting.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

public class RentingClient {

    public static void main(String[] args) {
        RentingClient rentingClient = new RentingClient();
        RentingServiceGrpc.RentingServiceBlockingStub rentingStub = rentingClient.getRentingServiceBlockingStub();

        rentingClient.listAvailableApartments(rentingStub);

        rentingClient.rentApartment(rentingStub, "4", "Super Man");
        rentingClient.rentApartment(rentingStub, "8", "Iron Man");
        rentingClient.rentApartment(rentingStub, "12", "Spider Man");
        rentingClient.rentApartment(rentingStub, "16", "The Wasp");
        rentingClient.rentApartment(rentingStub, "20", "Incredible Hulk");

        rentingClient.listAvailableApartments(rentingStub);

        rentingClient.shutdown(rentingStub);
    }

    private void rentApartment(RentingServiceGrpc.RentingServiceBlockingStub rentingStub, String aptNo, String renterName) {
        System.out.println("Renting Apartment " + aptNo);
        RentingResponse rentResult = rentingStub.rentApartment(Apartment.newBuilder().setAptNo(aptNo).setRenterName(renterName).build());
        if (rentResult.getIsRented()) {
            System.out.println("Apartment " + aptNo + " rented to " + renterName);
        } else {
            System.out.println("Apartment " + aptNo + " is not available ");
        }
    }

    private void listAvailableApartments(RentingServiceGrpc.RentingServiceBlockingStub client) {
        Apartments avlApts = client.listAvailableApartments(Void.newBuilder().build());
        List<Apartment> aptList = avlApts.getApartmentList();
        System.out.println("\n***** Welcome to Apartment Renting ***** \n List of Available Apartments:");
        aptList.forEach(apt -> System.out.print("|| Apt No: " + apt.getAptNo() + " "));
        System.out.println("\nTotal Apartments available :" + aptList.size() + "\n");
    }

    private RentingServiceGrpc.RentingServiceBlockingStub getRentingServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        return RentingServiceGrpc.newBlockingStub(channel);
    }

    private void shutdown(RentingServiceGrpc.RentingServiceBlockingStub rentingStub) {
        System.out.println("Shutting down channel");
        ManagedChannel channel = (ManagedChannel) rentingStub.getChannel();
        channel.shutdown();
    }


}
