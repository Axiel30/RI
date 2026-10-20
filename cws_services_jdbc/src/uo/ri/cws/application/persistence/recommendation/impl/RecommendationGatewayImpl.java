package uo.ri.cws.application.persistence.recommendation.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class RecommendationGatewayImpl implements RecommendationGateway {

	@Override
	public void add(RecommendationRecord t) throws PersistenceException {
	}

	@Override
	public void remove(String id) throws PersistenceException {
	}

	@Override
	public void update(RecommendationRecord t) throws PersistenceException {
	}

	@Override
	public Optional<RecommendationRecord> findById(String id) throws PersistenceException {
		return Optional.empty();
	}

	@Override
	public List<RecommendationRecord> findAll() throws PersistenceException {
		return List.of();
	}

	@Override
	public List<RecommendationRecord> findNotUsedBySponsorId(String sponsorId) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		List<RecommendationRecord> list = new ArrayList<>();
		try (PreparedStatement pst = c
				.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_FINDNOTUSEDBYSPONSORID"))) {
			pst.setString(1, sponsorId);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					RecommendationRecord r = new RecommendationRecord();
					r.id = rs.getString("id");
					r.version = rs.getLong("version");
					r.recommender_id = rs.getString("recommender_id");
					r.recommended_id = rs.getString("recommended_id");
					r.usedForVoucher = rs.getBoolean("usedForVoucher");
					list.add(r);
				}
			}
			return list;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void markAsUsed(String id) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_MARKASUSED"))) {
			pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
			pst.setString(2, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}