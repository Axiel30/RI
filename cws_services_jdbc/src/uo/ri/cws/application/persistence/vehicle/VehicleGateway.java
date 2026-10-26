package uo.ri.cws.application.persistence.vehicle;

import uo.ri.cws.application.persistence.Gateway;

public interface VehicleGateway extends Gateway<VehicleGateway.VehicleRecord> {

    boolean existsByClientId(String clientId);

    class VehicleRecord extends Gateway.BaseRecord {
        public String licensePlate;
        public String make;
        public String model;
        public String clientId;
        public String vehicleTypeId;
    }

}
