package com.rental.service.mntc;

import com.rental.maintenance.grpc.ApartmentMaintenanceGrpc;
import com.rental.maintenance.grpc.JobDoneResponse;
import com.rental.maintenance.grpc.WorkOrder;
import com.rental.maintenance.grpc.WorkOrders;
import com.rental.service.bean.ApartmentUnit;
import com.rental.service.util.ApartmentRentalServiceDataAccess;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class MaintenanceService extends ApartmentMaintenanceGrpc.ApartmentMaintenanceImplBase {

    ApartmentRentalServiceDataAccess maintenanceDao = new ApartmentRentalServiceDataAccess();

    @Override
    public void provideMaintenance(WorkOrders request, StreamObserver<JobDoneResponse> responseObserver) {
        List<WorkOrder> orderList = request.getOrderList();
        System.out.println("Inside provideMaintenance");
        orderList.forEach(order -> {
            if (validateRentedApartment(order.getAptNo())) {
                responseObserver.onNext(JobDoneResponse.newBuilder().setJobDone(true).build());
                System.out.println("Work order complete - " + order.getWorkDescription());
            } else {
                responseObserver.onNext(JobDoneResponse.newBuilder().setJobDone(false).build());
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        responseObserver.onCompleted();
    }

    private boolean validateRentedApartment(String aptNo) {
        List<ApartmentUnit> rentedAptsList = maintenanceDao.listRentedApartments();
        for (ApartmentUnit unit : rentedAptsList) {
            if (unit.getAptNo().equals(aptNo)) {
                return true;
            }
        }
        System.out.println("Apartment " + aptNo + " Not rented");
        return false;
    }
}
