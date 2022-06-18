package com.rental.server;

import com.rental.service.mntc.MaintenanceService;
import com.rental.service.renting.RentingService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ApartmentRentalServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new RentingService())
                .addService(new MaintenanceService())
                .build();
        server.start();
        System.out.println("Apartment Rental Server Started...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apartment Rental Shutdown initiated...");
            server.shutdown();
        }));
        server.awaitTermination();
    }
}
