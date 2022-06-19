package com.rental.client.renting;

import com.rental.renting.grpc.Void;
import com.rental.renting.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RentingClient {

    private Logger logger = LoggerFactory.getLogger(RentingClient.class);

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
        logger.info("Renting Apartment {} ...", aptNo);
        RentingResponse rentResult = rentingStub.rentApartment(Apartment.newBuilder().setAptNo(aptNo).setRenterName(renterName).build());
        if (rentResult.getIsRented()) {
            logger.info("\tApartment {} rented to {} ", aptNo, renterName);
        } else {
            logger.info("\tApartment {} is not available ", aptNo);
        }
    }

    private void listAvailableApartments(RentingServiceGrpc.RentingServiceBlockingStub client) {
        Apartments avlApts = client.listAvailableApartments(Void.newBuilder().build());
        List<Apartment> aptList = avlApts.getApartmentList();
        logger.info("\n***** Welcome to Apartment Renting ***** \nList of Available Apartments:");
        StringBuilder sb = new StringBuilder("| ");
        aptList.forEach(apt -> sb.append(apt.getAptNo()).append(" | "));
        if(logger.isInfoEnabled()) {
            logger.info(sb.toString());
        }
        logger.info("Total Apartments available :{} \n", aptList.size());
    }

    private RentingServiceGrpc.RentingServiceBlockingStub getRentingServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        return RentingServiceGrpc.newBlockingStub(channel);
    }

    private void shutdown(RentingServiceGrpc.RentingServiceBlockingStub rentingStub) {
        logger.info("Shutting down channel");
        ManagedChannel channel = (ManagedChannel) rentingStub.getChannel();
        channel.shutdown();
    }


}
