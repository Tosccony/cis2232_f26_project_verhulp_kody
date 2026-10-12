package ca.hccis.loadsheet.entity;

import com.google.gson.Gson;
import ca.hccis.loadsheet.util.CisUtility;

import java.util.Locale;
import java.util.function.DoubleConsumer;
/**
 * One weight and balance load sheet for a single flight.
 *
 * @author Kody Verhulp
 * @since 2026-09-25
 */
public class LoadSheet {

    private int loadSheetId;
    private String flightDate;
    private String tailNumber;
    private String pilotName;
    private double emptyWeight;
    private double emptyArm;
    private double frontSeatWeight;
    private double rearSeatWeight;
    private double baggageWeight;
    private double fuelLoaded;
    private double totalWeight;
    private double centreOfGravity;
    private String loadStatus;

    /**
     * One numeric question for the user: the prompt label, the allowed range,
     * and the setter that stores the answer.
     *
     * @param label Prompt shown to the user
     * @param min Lowest value allowed
     * @param max Highest value allowed (Double.MAX_VALUE for no limit)
     * @param setter Setter that stores the answer in its field
     * @since 20260925
     * @author Kody Verhulp
     */
    private record NumberPrompt(String label, double min, double max, DoubleConsumer setter) {}

    /**
     * Ask the user for the load sheet details. Bad input is asked again.
     * The id is set by the Controller and the calculated fields are left
     * for the calculation assignment.
     *
     * @since 20260925
     * @author Kody Verhulp
     */
    public void getInformation() {
        flightDate = CisUtility.getInputDate("Flight date (yyyy-MM-dd, blank = today's date)");
        tailNumber = CisUtility.getInputString("Tail number (ex: C-GABC)", 1, 10).toUpperCase().replace("-", "");
        pilotName = CisUtility.getInputString("Pilot Name", 1, 50);

        // Each prompt carries the setter for its field, so one loop can ask all of them
        NumberPrompt[] numberPrompts = {
                new NumberPrompt("Empty weight (lb)", 1, 2550, this::setEmptyWeight),
                new NumberPrompt("Empty arm (inches, from W&B record)", 1, Double.MAX_VALUE, this::setEmptyArm),
                new NumberPrompt("Front seat weight, includes pilot & passenger (lb)", 1, Double.MAX_VALUE, this::setFrontSeatWeight),
                new NumberPrompt("Rear seat weight, includes all rear passengers (lb)", 0, Double.MAX_VALUE, this::setRearSeatWeight),
                new NumberPrompt("Baggage weight (lb)", 0, Double.MAX_VALUE, this::setBaggageWeight),
                new NumberPrompt("Fuel loaded (US gallon - Maximum = 53)", 0, 53, this::setFuelLoaded)
        };

        for (NumberPrompt prompt : numberPrompts) {
            double value = CisUtility.getInputDouble(prompt.label(), prompt.min(), prompt.max());
            prompt.setter().accept(value);
        }
    }

    public int getLoadSheetId() {
        return loadSheetId;
    }

    public void setLoadSheetId(int loadSheetId) {
        this.loadSheetId = loadSheetId;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

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

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public double getEmptyArm() {
        return emptyArm;
    }

    public void setEmptyArm(double emptyArm) {
        this.emptyArm = emptyArm;
    }

    public double getFrontSeatWeight() {
        return frontSeatWeight;
    }

    public void setFrontSeatWeight(double frontSeatWeight) {
        this.frontSeatWeight = frontSeatWeight;
    }

    public double getRearSeatWeight() {
        return rearSeatWeight;
    }

    public void setRearSeatWeight(double rearSeatWeight) {
        this.rearSeatWeight = rearSeatWeight;
    }

    public double getBaggageWeight() {
        return baggageWeight;
    }

    public void setBaggageWeight(double baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    public double getFuelLoaded() {
        return fuelLoaded;
    }

    public void setFuelLoaded(double fuelLoaded) {
        this.fuelLoaded = fuelLoaded;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getCentreOfGravity() {
        return centreOfGravity;
    }

    public void setCentreOfGravity(double centreOfGravity) {
        this.centreOfGravity = centreOfGravity;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    /**
     * Convert the load sheet to a JSON string so we can save it to the file
     *
     * @return JSON output of the load sheet
     * @since 20260925
     * @author Kody Verhulp
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "LoadSheet{" +
                "loadSheetId=" + loadSheetId +
                ", flightDate='" + flightDate + '\'' +
                ", tailNumber='" + tailNumber + '\'' +
                ", pilotName='" + pilotName + '\'' +
                ", emptyWeight=" + emptyWeight +
                ", emptyArm=" + emptyArm +
                ", frontSeatWeight=" + frontSeatWeight +
                ", rearSeatWeight=" + rearSeatWeight +
                ", baggageWeight=" + baggageWeight +
                ", fuelLoaded=" + fuelLoaded +
                ", totalWeight=" + totalWeight +
                ", centreOfGravity=" + centreOfGravity +
                ", loadStatus='" + loadStatus + '\'' +
                '}';
    }
}
