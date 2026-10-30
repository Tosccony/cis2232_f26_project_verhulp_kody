package ca.hccis.squash.entity;

import java.util.ArrayList;

/**
 * Entity class to hold the inputs and results of the aircraft loading history report.
 *
 * @author Kody Verhulp
 * @since 20261030
 */
public class ReportLoadSheet {
    //Default to blank so a submit without a field still runs (and finds nothing)
    private String tailNumber = "";
    private String startDate = "";
    private String endDate = "";
    private ArrayList<LoadSheet> loadSheets;

    public String getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
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
}
