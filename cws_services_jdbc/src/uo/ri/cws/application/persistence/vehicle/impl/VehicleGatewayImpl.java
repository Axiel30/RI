package uo.ri.cws.application.persistence.vehicle.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway;
import uo.ri.util.exception.PersistenceException;

public class VehicleGatewayImpl implements VehicleGateway {

    private final VehicleRecordAssembler assembler = new VehicleRecordAssembler();

    @Override
    public void add(VehicleRecord vehicle) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TVEHICLES_ADD"))) {
            pst.setString(1, vehicle.id);
            pst.setString(2, vehicle.licensePlate);
            pst.setString(3, vehicle.make);
            pst.setString(4, vehicle.model);
            pst.setString(5, vehicle.clientId);
            pst.setString(6, vehicle.vehicleTypeId);
            pst.setLong(7, vehicle.version);
            pst.setTimestamp(8, Timestamp.valueOf(vehicle.createdAt));
            pst.setTimestamp(9, Timestamp.valueOf(vehicle.updatedAt));
            pst.setString(10, vehicle.entityState);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TVEHICLES_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(VehicleRecord vehicle) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TVEHICLES_UPDATE"))) {
            pst.setString(1, vehicle.licensePlate);
            pst.setString(2, vehicle.make);
            pst.setString(3, vehicle.model);
            pst.setString(4, vehicle.clientId);
            pst.setString(5, vehicle.vehicleTypeId);
            pst.setTimestamp(6, Timestamp.valueOf(vehicle.updatedAt));
            pst.setString(7, vehicle.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<VehicleRecord> findById(String id) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TVEHICLES_FINDBYID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toOptionalRecord(rs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<VehicleRecord> findAll() {
        try (
            PreparedStatement pst = Jdbc.getCurrentConnection()
                .prepareStatement(Queries.getSQLSentence("TVEHICLES_FINDALL"));
            ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean existsByClientId(String clientId) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TVEHICLES_EXISTSBYCLIENTID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
