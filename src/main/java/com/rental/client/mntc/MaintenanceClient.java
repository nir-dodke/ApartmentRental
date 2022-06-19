package com.rental.client.mntc;

import com.rental.maintenance.grpc.ApartmentMaintenanceGrpc;
import com.rental.maintenance.grpc.JobDoneResponse;
import com.rental.maintenance.grpc.WorkOrder;
import com.rental.maintenance.grpc.WorkOrders;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MaintenanceClient {
    private Logger logger = LoggerFactory.getLogger(MaintenanceClient.class);

    public static void main(String[] args) {
        MaintenanceClient client = new MaintenanceClient();
        ApartmentMaintenanceGrpc.ApartmentMaintenanceBlockingStub mntcStub = client.getMaintenanceServiceNonBlockingStub();
        client.provideMaintenance(mntcStub, client.buildMaintenanceList());
        client.shutdown(mntcStub);
    }

    private void provideMaintenance(ApartmentMaintenanceGrpc.ApartmentMaintenanceBlockingStub mntcStub, Map<String, String> mntcMap) {
        List<WorkOrder> ordersList = new ArrayList<>();
        mntcMap.forEach((aptNo, orderDesc) -> ordersList.add(WorkOrder.newBuilder().setAptNo(aptNo).setWorkDescription(orderDesc).build()));
        WorkOrders orders = WorkOrders.newBuilder().addAllOrder(ordersList).build();
        Iterator<JobDoneResponse> mntcResponse = mntcStub.provideMaintenance(orders);
        mntcResponse.forEachRemaining(resp -> {
            if (resp.getJobDone()) {
                logger.info("Maintenance provided in apartment {}", resp.getAptNo());
            } else {
                logger.info("Maintenance can not be provided in apartment {}", resp.getAptNo());
            }
        });
    }

    private Map<String, String> buildMaintenanceList() {
        Map<String, String> mntcMap = new HashMap<>();
        mntcMap.put("4", "Fix Burner");
        mntcMap.put("8", "Replace AC filter");
        mntcMap.put("11", "Washer issue");
        return mntcMap;
    }

    private ApartmentMaintenanceGrpc.ApartmentMaintenanceBlockingStub getMaintenanceServiceNonBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        return ApartmentMaintenanceGrpc.newBlockingStub(channel);
    }

    private void shutdown(ApartmentMaintenanceGrpc.ApartmentMaintenanceBlockingStub mntcStub) {
        logger.info("Shutting down channel");
        ManagedChannel channel = (ManagedChannel) mntcStub.getChannel();
        channel.shutdown();
    }
}
