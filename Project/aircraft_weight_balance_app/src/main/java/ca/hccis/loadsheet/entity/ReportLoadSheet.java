package ca.hccis.loadsheet.entity;

import java.util.ArrayList;

/**
 * Entity class to hold the search criteria and results of the aircraft loading history report.
 * Every criterion is optional; a blank one means "any".
 *
 * @author Kody Verhulp
 * @since 20261030
 */
public class ReportLoadSheet {
    //Default to blank so a criterion left out of the form means "any"
    private String tailNumber = "";
    private String pilotName = "";
    private String loadStatus = "";
    private String startDate = "";
    private String endDate = "";
    private ArrayList<LoadSheet> loadSheets;

    public String getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<LoadSheet> getLoadSheets() {
        return loadSheets;
    }

    public void setLoadSheets(ArrayList<LoadSheet> loadSheets) {
        this.loadSheets = loadSheets;
    }

    /**
     * The aircraft searched for, for the results heading.
     *
     * @return the tail number, or "All aircraft" when none was chosen
     * @author Kody Verhulp
     * @since 20261030
     */
    public String getAircraftLabel() {
        return tailNumber.isBlank() ? "All aircraft" : tailNumber;
    }

    /**
     * The other criteria in words, e.g. Pilot contains "alex" · FAIL only · 2026-09-01 to 2026-09-30
     *
     * @return the pilot, status and date criteria, joined with " · "
     * @author Kody Verhulp
     * @since 20261030
     */
    public String getFilterSummary() {
        ArrayList<String> parts = new ArrayList<>();
        if (!pilotName.isBlank()) {
            parts.add("Pilot contains \"" + pilotName.strip() + "\"");
        }
        if (!loadStatus.isBlank()) {
            parts.add(loadStatus + " only");
        }
        if (!startDate.isBlank() && !endDate.isBlank()) {
            parts.add(startDate + " to " + endDate);
        } else if (!startDate.isBlank()) {
            parts.add("From " + startDate);
        } else if (!endDate.isBlank()) {
            parts.add("Up to " + endDate);
        } else {
            parts.add("All dates");
        }
        return String.join(" · ", parts);
    }

    /**
     * A From date after the To date can't match any load sheet.
     * The dates are yyyy-MM-dd text, which sorts in date order, so compareTo works.
     *
     * @return false only when both dates are filled in and From is after To
     * @author Kody Verhulp
     * @since 20261030
     */
    public boolean isDateRangeValid() {
        return startDate.isBlank() || endDate.isBlank() || startDate.compareTo(endDate) <= 0;
    }

    /**
     * Every criterion in one line. Written as the first line of the report file.
     *
     * @return the aircraft and the other criteria
     * @author Kody Verhulp
     * @since 20261030
     */
    public String getCriteriaSummary() {
        return "Aircraft Loading History: " + getAircraftLabel() + " · " + getFilterSummary();
    }
}
