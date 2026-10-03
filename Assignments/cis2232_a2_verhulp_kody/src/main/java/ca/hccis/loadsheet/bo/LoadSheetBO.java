package ca.hccis.loadsheet.bo;

import ca.hccis.loadsheet.entity.LoadSheet;

public class LoadSheetBO {

    public static final double ARM_FRONT_SEATS = 37.0;
    public static final double ARM_REAR_SEATS = 73.0;
    public static final double ARM_BAGGAGE = 95.0;
    public static final double ARM_FUEL = 48.0;
    public static final double FUEL_WEIGHT_PER_GALLON = 6.0;
    public static final double MAX_GROSS_WEIGHT = 2550;
    public static final String STATUS_PASS = "PASS";
    public static final String STATUS_FAIL = "FAIL";

    /**
     * Calculate the loaded weight and center of gravity of the aircraft
     * The total weight is stored on the load sheet.
     *
     * @param loadSheet the load sheet with the pilot's entries
     * @return The centre of gravity in inches aft of the datum
     * @since 20261002
     * @author Kody Verhulp
     */
    public static double calculate(LoadSheet loadSheet) {
        double fuelWeight = loadSheet.getFuelLoaded() * FUEL_WEIGHT_PER_GALLON;

        double totalWeight = loadSheet.getEmptyWeight()
                + loadSheet.getFrontSeatWeight()
                + loadSheet.getRearSeatWeight()
                + loadSheet.getBaggageWeight()
                + fuelWeight;

        double totalMoment = loadSheet.getEmptyWeight() * loadSheet.getEmptyArm()
                + loadSheet.getFrontSeatWeight() * ARM_FRONT_SEATS
                + loadSheet.getRearSeatWeight() * ARM_REAR_SEATS
                + loadSheet.getBaggageWeight() * ARM_BAGGAGE
                + fuelWeight * ARM_FUEL;

        loadSheet.setTotalWeight(totalWeight);

        if (totalWeight <= MAX_GROSS_WEIGHT) {
            loadSheet.setLoadStatus(STATUS_PASS);
        } else {
            loadSheet.setLoadStatus(STATUS_FAIL);
        }
        return totalMoment / totalWeight;
    }
}
