package uo.ri.cws.application.persistence.recommendation.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class RecommendationGatewayImpl implements RecommendationGateway {

	private final RecommendationRecordAssembler assembler = new RecommendationRecordAssembler();

	@Override
	public void add(RecommendationRecord recommendation) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_ADD"))) {
			pst.setString(1, recommendation.id);
			pst.setString(2, recommendation.sponsorId);
			pst.setString(3, recommendation.recommendedId);
			pst.setBoolean(4, recommendation.usedForVoucher);
			pst.setLong(5, recommendation.version);
			pst.setTimestamp(6, Timestamp.valueOf(recommendation.createdAt));
			pst.setTimestamp(7, Timestamp.valueOf(recommendation.updatedAt));
			pst.setString(8, recommendation.entityState);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void remove(String id) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_REMOVE"))) {
			pst.setString(1, id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void update(RecommendationRecord recommendation) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_UPDATE"))) {
			pst.setBoolean(1, recommendation.usedForVoucher);
			pst.setTimestamp(2, Timestamp.valueOf(recommendation.updatedAt));
			pst.setString(3, recommendation.id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<RecommendationRecord> findById(String id) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_FINDBYID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				return assembler.toOptionalRecord(rs);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<RecommendationRecord> findAll() {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_FINDALL"));
				ResultSet rs = pst.executeQuery()) {
			return assembler.toRecordList(rs);

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<RecommendationRecord> findByClientId(String clientId) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_FINDBYCLIENTID"))) {
			pst.setString(1, clientId);
			pst.setString(2, clientId);
			try (ResultSet rs = pst.executeQuery()) {
				return assembler.toRecordList(rs);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<RecommendationRecord> findNotUsedBySponsorId(String sponsorId) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_FINDNOTUSEDBYSPONSORID"))) {
			pst.setString(1, sponsorId);
			try (ResultSet rs = pst.executeQuery()) {
				return assembler.toRecordList(rs);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

}
