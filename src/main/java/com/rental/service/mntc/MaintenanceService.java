package com.rental.service.mntc;

import com.rental.maintenance.grpc.ApartmentMaintenanceGrpc;
import com.rental.maintenance.grpc.JobDoneResponse;
import com.rental.maintenance.grpc.WorkOrder;
import com.rental.maintenance.grpc.WorkOrders;
import com.rental.service.bean.ApartmentUnit;
import com.rental.service.util.ApartmentRentalServiceDataAccess;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MaintenanceService extends ApartmentMaintenanceGrpc.ApartmentMaintenanceImplBase {

    ApartmentRentalServiceDataAccess maintenanceDao = new ApartmentRentalServiceDataAccess();
    private Logger logger = LoggerFactory.getLogger(MaintenanceService.class);

    @Override
    public void provideMaintenance(WorkOrders request, StreamObserver<JobDoneResponse> responseObserver) {
        List<WorkOrder> orderList = request.getOrderList();
        orderList.forEach(order -> {
            if (validateRentedApartment(order.getAptNo())) {
                responseObserver.onNext(JobDoneResponse.newBuilder().setJobDone(true).setAptNo(order.getAptNo()).build());
                logger.info("Work order completed for apartment {}  | Work Description: {}", order.getAptNo(), order.getWorkDescription());
            } else {
                logger.info("Apartment {} is unoccupied or invalid apartment number is provided", order.getAptNo());
                responseObserver.onNext(JobDoneResponse.newBuilder().setJobDone(false).setAptNo(order.getAptNo()).build());
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error("Exception occurred {}", e.getMessage());
                Thread.currentThread().interrupt();
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
