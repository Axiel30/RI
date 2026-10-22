package uo.ri.cws.application.persistence.util.assembler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway.BaseRecord;

/**
 * Converts the rows of a result set into records. Subclasses decide which
 * record to instantiate for the current row and read their own columns; the
 * columns shared by every table are read here.
 */
public abstract class BaseRecordAssembler<T extends BaseRecord> {

	/** @return a new, empty record for the current row */
	protected abstract T newRecord(ResultSet rs) throws SQLException;

	/** Reads from the current row the columns specific to the record */
	protected abstract void fill(T record, ResultSet rs) throws SQLException;

	/** Converts the current row, the cursor is not moved */
	public T toRecord(ResultSet rs) throws SQLException {
		T record = newRecord(rs);
		record.id = rs.getString("id");
		record.version = rs.getLong("version");
		record.entityState = rs.getString("entityState");
		record.createdAt = toLocalDateTime(rs.getTimestamp("createdAt"));
		record.updatedAt = toLocalDateTime(rs.getTimestamp("updatedAt"));
		fill(record, rs);
		return record;
	}

	/** Moves the cursor to the first row, if any, and converts it */
	public Optional<T> toOptionalRecord(ResultSet rs) throws SQLException {
		return rs.next() ? Optional.of(toRecord(rs)) : Optional.empty();
	}

	/** Converts all the remaining rows */
	public List<T> toRecordList(ResultSet rs) throws SQLException {
		List<T> records = new ArrayList<>();
		while (rs.next()) {
			records.add(toRecord(rs));
		}
		return records;
	}

	private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}

}
