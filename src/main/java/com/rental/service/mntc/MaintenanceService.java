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
        orderList.forEach(order -> {
            if (validateRentedApartment(order.getAptNo())) {
                responseObserver.onNext(JobDoneResponse.newBuilder().setJobDone(true).setAptNo(order.getAptNo()).build());
                System.out.println("Work order completed for apartment "+order.getAptNo() + " | Work Description: " +order.getWorkDescription());
            } else {
                System.out.println("Apartment "+order.getAptNo()+" is unoccupied or invalid apartment number is provided");
                responseObserver.onNext(JobDoneResponse.newBuilder().setJobDone(false).setAptNo(order.getAptNo()).build());
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
        return false;
    }
}
