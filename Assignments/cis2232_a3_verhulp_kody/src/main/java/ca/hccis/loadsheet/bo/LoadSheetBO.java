package ca.hccis.loadsheet.bo;

import ca.hccis.loadsheet.entity.LoadSheet;

/**
 * Business logic for a weight and balance load sheet.
 *
 * @author Kody Verhulp
 * @since 20261002
 */
public class LoadSheetBO {

    public static final double ARM_FRONT_SEATS = 37.0;
    public static final double ARM_REAR_SEATS = 73.0;
    public static final double ARM_BAGGAGE = 95.0;
    public static final double ARM_FUEL = 48.0;
    public static final double FUEL_WEIGHT_PER_GALLON = 6.0;
    public static final double MAX_GROSS_WEIGHT = 2550;
    public static final double MAX_BAGGAGE_WEIGHT = 120;
    public static final double AFT_CG_LIMIT = 47.3;
    public static final double FORWARD_CG_LIMIT_LIGHT = 35.0;
    public static final double FORWARD_CG_LIMIT_AT_MAX_GROSS = 41.0;
    public static final double FORWARD_LIMIT_SLOPE_START_WEIGHT = 1950;
    public static final String STATUS_PASS = "PASS";
    public static final String STATUS_FAIL = "FAIL";

    /**
     * Calculate the loaded weight and centre of gravity of the aircraft and
     * check them against the Cessna 172S limits. The total weight, centre of
     * gravity and load status are stored on the load sheet.
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

        double centreOfGravity = totalMoment / totalWeight;

        // Forward limit is 35.0 in up to 1950 lb, then slopes up to 41.0 in at 2550 lb
        double forwardLimit = FORWARD_CG_LIMIT_LIGHT;
        if (totalWeight > FORWARD_LIMIT_SLOPE_START_WEIGHT) {
            forwardLimit += (totalWeight - FORWARD_LIMIT_SLOPE_START_WEIGHT)
                    * (FORWARD_CG_LIMIT_AT_MAX_GROSS - FORWARD_CG_LIMIT_LIGHT)
                    / (MAX_GROSS_WEIGHT - FORWARD_LIMIT_SLOPE_START_WEIGHT);
        }

        loadSheet.setTotalWeight(totalWeight);
        loadSheet.setCentreOfGravity(centreOfGravity);

        if (totalWeight <= MAX_GROSS_WEIGHT
                && loadSheet.getBaggageWeight() <= MAX_BAGGAGE_WEIGHT
                && centreOfGravity >= forwardLimit
                && centreOfGravity <= AFT_CG_LIMIT) {
            loadSheet.setLoadStatus(STATUS_PASS);
        } else {
            loadSheet.setLoadStatus(STATUS_FAIL);
        }

        return centreOfGravity;
    }
}
