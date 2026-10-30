package ca.hccis.loadsheet.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the report criteria.
 *
 * @author Kody Verhulp
 * @since 20261030
 */
class ReportLoadSheetTest {

    /**
     * Only a From date after the To date is refused; blank dates and a one-day range are fine.
     */
    @Test
    void onlyFromAfterToIsAnInvalidDateRange() {
        ReportLoadSheet criteria = new ReportLoadSheet();
        assertTrue(criteria.isDateRangeValid());

        criteria.setStartDate("2026-09-30");
        assertTrue(criteria.isDateRangeValid());

        criteria.setEndDate("2026-09-30");
        assertTrue(criteria.isDateRangeValid());

        criteria.setEndDate("2026-09-01");
        assertFalse(criteria.isDateRangeValid());
    }
}
