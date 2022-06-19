package com.rental.server;

import com.rental.service.mntc.MaintenanceService;
import com.rental.service.renting.RentingService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ApartmentRentalServer {
    private static Logger logger = LoggerFactory.getLogger(ApartmentRentalServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new RentingService())
                .addService(new MaintenanceService())
                .build();
        server.start();
        logger.info("Apartment Rental Server Started...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Apartment Rental Shutdown initiated...");
            server.shutdown();
        }));
        server.awaitTermination();
    }
}
