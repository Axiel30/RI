package uo.ri.cws.application.persistence.intervention.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class InterventionGatewayImpl implements InterventionGateway {

    private final InterventionRecordAssembler assembler = new InterventionRecordAssembler();

    @Override
    public void add(InterventionRecord intervention) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TINTERVENTIONS_ADD"))) {
            pst.setString(1, intervention.id);
            pst.setTimestamp(2, Timestamp.valueOf(intervention.date));
            pst.setInt(3, intervention.minutes);
            pst.setString(4, intervention.mechanicId);
            pst.setString(5, intervention.workOrderId);
            pst.setLong(6, intervention.version);
            pst.setTimestamp(7, Timestamp.valueOf(intervention.createdAt));
            pst.setTimestamp(8, Timestamp.valueOf(intervention.updatedAt));
            pst.setString(9, intervention.entityState);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TINTERVENTIONS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(InterventionRecord intervention) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TINTERVENTIONS_UPDATE"))) {
            pst.setTimestamp(1, Timestamp.valueOf(intervention.date));
            pst.setInt(2, intervention.minutes);
            pst.setString(3, intervention.mechanicId);
            pst.setString(4, intervention.workOrderId);
            pst.setTimestamp(5, Timestamp.valueOf(intervention.updatedAt));
            pst.setString(6, intervention.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<InterventionRecord> findById(String id) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TINTERVENTIONS_FINDBYID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toOptionalRecord(rs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<InterventionRecord> findAll() {
        try (
            PreparedStatement pst = Jdbc.getCurrentConnection()
                .prepareStatement(Queries.getSQLSentence("TINTERVENTIONS_FINDALL"));
            ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean existsByMechanicId(String mechanicId) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TINTERVENTIONS_EXISTSBYMECHANICID"))) {
            pst.setString(1, mechanicId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
