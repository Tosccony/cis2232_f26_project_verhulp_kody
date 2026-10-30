package ca.hccis.loadsheet.dao;

import ca.hccis.loadsheet.entity.LoadSheet;
import ca.hccis.loadsheet.entity.ReportLoadSheet;
import ca.hccis.loadsheet.util.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class to access the load_sheet table using jdbc.
 *
 * @author Kody Verhulp
 * @since 20261030
 */
public class LoadSheetDAO {

    /**
     * SQL with ? placeholders, and the values for them in the same order.
     */
    record SearchQuery(String sql, List<String> values) {
    }

    /**
     * Build the search SQL. Each criterion that was filled in adds one condition; a blank one adds nothing.
     * flightDate is stored as yyyy-MM-dd text, which sorts in date order, so >= and <= work on it.
     * Only fixed SQL text is added here: the user's input only ever goes into the values list.
     *
     * @param criteria Report criteria from the input form
     * @return the SQL and its values
     * @author Kody Verhulp
     * @since 20261030
     */
    static SearchQuery buildSearchQuery(ReportLoadSheet criteria) {
        ArrayList<String> conditions = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        if (!criteria.getTailNumber().isBlank()) {
            conditions.add("tailNumber = ?");
            values.add(criteria.getTailNumber());
        }
        if (!criteria.getPilotName().isBlank()) {
            conditions.add("pilotName LIKE ?");
            values.add("%" + criteria.getPilotName().strip() + "%");
        }
        if (!criteria.getLoadStatus().isBlank()) {
            conditions.add("loadStatus = ?");
            values.add(criteria.getLoadStatus());
        }
        if (!criteria.getStartDate().isBlank()) {
            conditions.add("flightDate >= ?");
            values.add(criteria.getStartDate());
        }
        if (!criteria.getEndDate().isBlank()) {
            conditions.add("flightDate <= ?");
            values.add(criteria.getEndDate());
        }

        String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
        return new SearchQuery("SELECT * FROM load_sheet" + where + " ORDER BY flightDate, loadSheetId", values);
    }

    /**
     * Select the load sheets that match the report criteria, oldest flight first.
     *
     * @param criteria Report criteria from the input form (blank criteria match everything)
     * @return the matching load sheets (empty if none)
     * @throws SQLException if the database can't be reached or read
     * @author Kody Verhulp
     * @since 20261030
     */
    public ArrayList<LoadSheet> selectAllByCriteria(ReportLoadSheet criteria) throws SQLException {
        SearchQuery query = buildSearchQuery(criteria);
        ArrayList<LoadSheet> loadSheets = new ArrayList<>();

        //******************************************************************
        //The ? placeholders send the user's input as values, never as part of
        //the SQL, so typing quotes into the form can't change the query.
        //try-with-resources closes the connection, statement and result set.
        //******************************************************************
        try (Connection conn = DatabaseUtility.getConnection("");
             PreparedStatement stmt = conn.prepareStatement(query.sql())) {
            for (int i = 0; i < query.values().size(); i++) {
                stmt.setString(i + 1, query.values().get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LoadSheet loadSheet = new LoadSheet();
                    loadSheet.setLoadSheetId(rs.getInt("loadSheetId"));
                    loadSheet.setFlightDate(rs.getString("flightDate"));
                    loadSheet.setTailNumber(rs.getString("tailNumber"));
                    loadSheet.setPilotName(rs.getString("pilotName"));
                    loadSheet.setEmptyWeight(rs.getDouble("emptyWeight"));
                    loadSheet.setEmptyArm(rs.getDouble("emptyArm"));
                    loadSheet.setFrontSeatWeight(rs.getDouble("frontSeatWeight"));
                    loadSheet.setRearSeatWeight(rs.getDouble("rearSeatWeight"));
                    loadSheet.setBaggageWeight(rs.getDouble("baggageWeight"));
                    loadSheet.setFuelLoaded(rs.getDouble("fuelLoaded"));
                    loadSheet.setTotalWeight(rs.getDouble("totalWeight"));
                    loadSheet.setCentreOfGravity(rs.getDouble("centreOfGravity"));
                    loadSheet.setLoadStatus(rs.getString("loadStatus"));

                    loadSheets.add(loadSheet);
                }
            }
        }
        return loadSheets;
    }

    /**
     * Select every tail number that has a load sheet, for the aircraft drop-down on the report form.
     *
     * @return the tail numbers in alphabetical order
     * @throws SQLException if the database can't be reached or read
     * @author Kody Verhulp
     * @since 20261030
     */
    public ArrayList<String> selectTailNumbers() throws SQLException {
        ArrayList<String> tailNumbers = new ArrayList<>();

        try (Connection conn = DatabaseUtility.getConnection("");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT DISTINCT tailNumber FROM load_sheet ORDER BY tailNumber");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tailNumbers.add(rs.getString("tailNumber"));
            }
        }
        return tailNumbers;
    }
}
