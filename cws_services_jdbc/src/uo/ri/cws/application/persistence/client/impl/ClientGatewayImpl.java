package uo.ri.cws.application.persistence.client.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class ClientGatewayImpl implements ClientGateway {

    private final ClientRecordAssembler assembler = new ClientRecordAssembler();

    @Override
    public void add(ClientRecord client) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCLIENTS_ADD"))) {
            pst.setString(1, client.id);
            pst.setString(2, client.nif);
            pst.setString(3, client.name);
            pst.setString(4, client.surname);
            pst.setString(5, client.email);
            pst.setString(6, client.phone);
            pst.setString(7, client.street);
            pst.setString(8, client.city);
            pst.setString(9, client.postalCode);
            pst.setString(10, client.country);
            pst.setLong(11, client.version);
            pst.setTimestamp(12, Timestamp.valueOf(client.createdAt));
            pst.setTimestamp(13, Timestamp.valueOf(client.updatedAt));
            pst.setString(14, client.entityState);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCLIENTS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(ClientRecord client) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCLIENTS_UPDATE"))) {
            pst.setString(1, client.nif);
            pst.setString(2, client.name);
            pst.setString(3, client.surname);
            pst.setString(4, client.email);
            pst.setString(5, client.phone);
            pst.setString(6, client.street);
            pst.setString(7, client.city);
            pst.setString(8, client.postalCode);
            pst.setString(9, client.country);
            pst.setTimestamp(10, Timestamp.valueOf(client.updatedAt));
            pst.setString(11, client.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ClientRecord> findById(String id) {
        return findOne("TCLIENTS_FINDBYID", id);
    }

    @Override
    public Optional<ClientRecord> findByNif(String nif) {
        return findOne("TCLIENTS_FINDBYNIF", nif);
    }

    @Override
    public List<ClientRecord> findAll() {
        try (
            PreparedStatement pst = Jdbc.getCurrentConnection()
                .prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDALL"));
            ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<ClientRecord> findRecommendedByNif(String recommenderNif) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDRECOMMENDEDBYNIF"))) {
            pst.setString(1, recommenderNif);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<ClientRecord> findWithInvoicesInState(String state) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDWITHINVOICESTATE"))) {
            pst.setString(1, state);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Optional<ClientRecord> findOne(String query, String arg) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection().prepareStatement(Queries.getSQLSentence(query))) {
            pst.setString(1, arg);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toOptionalRecord(rs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
