package ca.hccis.loadsheet.dao;

import ca.hccis.loadsheet.entity.ReportLoadSheet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the report search SQL. No database needed: only the SQL text and its values are checked.
 *
 * @author Kody Verhulp
 * @since 20261030
 */
class LoadSheetDAOTest {

    /**
     * A blank form lists every load sheet.
     */
    @Test
    void noCriteriaSelectsEveryLoadSheet() {
        LoadSheetDAO.SearchQuery query = LoadSheetDAO.buildSearchQuery(new ReportLoadSheet());

        assertEquals("SELECT * FROM load_sheet ORDER BY flightDate, loadSheetId", query.sql());
        assertTrue(query.values().isEmpty());
    }

    /**
     * Each criterion adds one ? condition, and its value is in the same position.
     */
    @Test
    void everyCriterionAddsOnePlaceholderInOrder() {
        ReportLoadSheet criteria = new ReportLoadSheet();
        criteria.setTailNumber("CGKDV");
        criteria.setPilotName(" alex ");
        criteria.setLoadStatus("FAIL");
        criteria.setStartDate("2026-09-01");
        criteria.setEndDate("2026-09-30");

        LoadSheetDAO.SearchQuery query = LoadSheetDAO.buildSearchQuery(criteria);

        assertEquals("SELECT * FROM load_sheet WHERE tailNumber = ? AND pilotName LIKE ? AND loadStatus = ?"
                + " AND flightDate >= ? AND flightDate <= ? ORDER BY flightDate, loadSheetId", query.sql());
        assertEquals(List.of("CGKDV", "%alex%", "FAIL", "2026-09-01", "2026-09-30"), query.values());
    }
}
