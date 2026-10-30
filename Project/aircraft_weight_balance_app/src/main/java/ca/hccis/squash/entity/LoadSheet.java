package ca.hccis.squash.entity;

/**
 * One weight and balance load sheet for a single flight (a row of the load_sheet table).
 *
 * @author Kody Verhulp
 * @since 20261030
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
     * One labelled line per load sheet. Used when the report is written to a file.
     *
     * @return the load sheet as one readable line
     * @author Kody Verhulp
     * @since 20261030
     */
    @Override
    public String toString() {
        return String.format("Flight date: %s | Tail number: %s | Pilot: %s | Front seats: %.1f lb | Rear seats: %.1f lb"
                        + " | Baggage: %.1f lb | Fuel: %.1f US gal | Total weight: %.1f lb | CG: %.2f in | Status: %s",
                flightDate, tailNumber, pilotName, frontSeatWeight, rearSeatWeight,
                baggageWeight, fuelLoaded, totalWeight, centreOfGravity, loadStatus);
    }
}
