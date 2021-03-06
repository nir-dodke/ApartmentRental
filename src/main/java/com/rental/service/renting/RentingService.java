package com.rental.service.renting;

import com.rental.renting.grpc.Void;
import com.rental.renting.grpc.*;
import com.rental.service.bean.ApartmentUnit;
import com.rental.service.util.ApartmentRentalServiceDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RentingService extends RentingServiceGrpc.RentingServiceImplBase {

    private Logger logger = LoggerFactory.getLogger(RentingService.class);
    private final ApartmentRentalServiceDataAccess rentingDao = new ApartmentRentalServiceDataAccess();

    @Override
    public void rentApartment(Apartment request, io.grpc.stub.StreamObserver<RentingResponse> responseObserver) {
        boolean result = rentingDao.blockApartment(request.getAptNo(), request.getRenterName());
        RentingResponse response = RentingResponse.newBuilder().setIsRented(result).build();
        logger.info("Apartment No: {} - Rented: {}", request.getAptNo(), result);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listAvailableApartments(Void request, io.grpc.stub.StreamObserver<Apartments> responseObserver) {
        List<ApartmentUnit> aptUnitList = rentingDao.fetchAvailableApartments();
        List<Apartment> aptList = new ArrayList<>();
        aptUnitList.forEach(apt -> aptList.add(Apartment.newBuilder()
                .setAptNo(apt.getAptNo())
                .setRented(apt.isRented())
                .build()));
        Apartments apartments = Apartments.newBuilder().addAllApartment(aptList).build();
        responseObserver.onNext(apartments);
        responseObserver.onCompleted();
    }
}
