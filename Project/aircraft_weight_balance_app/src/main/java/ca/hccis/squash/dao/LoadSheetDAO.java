package ca.hccis.squash.dao;

import ca.hccis.squash.entity.LoadSheet;
import ca.hccis.squash.util.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO class to access the load_sheet table using jdbc.
 *
 * @author Kody Verhulp
 * @since 20261030
 */
public class LoadSheetDAO {

    /**
     * Select the load sheets for one aircraft between two dates (both included), oldest flight first.
     * flightDate is stored as yyyy-MM-dd text, which sorts in date order, so BETWEEN works on it.
     *
     * @param tailNumber Aircraft registration (e.g. CGKDV)
     * @param startDate First flight date (yyyy-MM-dd)
     * @param endDate Last flight date (yyyy-MM-dd)
     * @return the matching load sheets (empty if none)
     * @throws SQLException if the database can't be reached or read
     * @author Kody Verhulp
     * @since 20261030
     */
    public ArrayList<LoadSheet> selectAllByTailNumberAndDates(String tailNumber, String startDate, String endDate)
            throws SQLException {
        String sqlStatement = "SELECT * FROM load_sheet WHERE tailNumber = ? AND flightDate BETWEEN ? AND ? "
                + "ORDER BY flightDate, loadSheetId";
        ArrayList<LoadSheet> loadSheets = new ArrayList<>();

        //******************************************************************
        //The ? placeholders send the user's input as values, never as part of
        //the SQL, so typing quotes into the form can't change the query.
        //try-with-resources closes the connection, statement and result set.
        //******************************************************************
        try (Connection conn = DatabaseUtility.getConnection("");
             PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {
            stmt.setString(1, tailNumber);
            stmt.setString(2, startDate);
            stmt.setString(3, endDate);

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
}
