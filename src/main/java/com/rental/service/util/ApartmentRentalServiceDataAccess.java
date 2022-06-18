package com.rental.service.util;

import com.rental.service.bean.ApartmentUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApartmentRentalServiceDataAccess {
    private static final Map<String, ApartmentUnit> apartmentMap = new HashMap<>(20);

    static {
        for (int i = 1; i <= 20; i++) {
            apartmentMap.put(String.valueOf(i),
                    new ApartmentUnit(String.valueOf(i), null, false));
        }
    }

    public boolean blockApartment(String aptNo, String renterName) {
        ApartmentUnit apt = apartmentMap.get(aptNo);
        if (apt != null && !apt.isRented()) {
            apt.setRented(true);
            apt.setRenterName(renterName);
            return true;
        } else {
            return false;
        }
    }

    public List<ApartmentUnit> fetchAvailableApartments() {
        return apartmentMap.values().stream()
                .filter(apt -> !apt.isRented())
                .collect(Collectors.toList());
    }

    public List<ApartmentUnit> listRentedApartments() {
        return apartmentMap.values().stream()
                .filter(ApartmentUnit::isRented)
                .collect(Collectors.toList());
    }
}
