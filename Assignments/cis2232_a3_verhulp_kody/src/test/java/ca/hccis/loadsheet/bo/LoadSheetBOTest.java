package ca.hccis.loadsheet.bo;


import ca.hccis.loadsheet.entity.LoadSheet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LoadSheetBOTest {

    /**
     * Test 1 following TDD - Based off repo example
     * @since 20261002
     * @author Kody Verhulp
     */
    @Test
    void testCalculateExample() {

        LoadSheet loadSheet = new LoadSheet();

        loadSheet.setEmptyWeight(1680);
        loadSheet.setEmptyArm(39.1);
        loadSheet.setFrontSeatWeight(340);
        loadSheet.setRearSeatWeight(190);
        loadSheet.setBaggageWeight(45);
        loadSheet.setFuelLoaded(40);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(43.26, actual, 0.01);

    }

    /**
     * Test 2 following TDD
     * @since 20261002
     * @author Kody Verhulp
     */
    @Test
    void testCalculateOverGross() {

        LoadSheet loadSheet = new LoadSheet();

        loadSheet.setEmptyWeight(1680);
        loadSheet.setEmptyArm(39.1);
        loadSheet.setFrontSeatWeight(340);
        loadSheet.setRearSeatWeight(190);
        loadSheet.setBaggageWeight(45);
        loadSheet.setFuelLoaded(53);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(43.40, actual, 0.01);
        assertEquals(2573, loadSheet.getTotalWeight(), 0.01);
    }

    /**
     * Test 3 following TDD
     * @since 20261002
     * @author Kody Verhulp
     */
    @Test
    void testCalculateStatusGrossWeight() {

        LoadSheet underGross = new LoadSheet();
        underGross.setEmptyWeight(1680);
        underGross.setEmptyArm(39.1);
        underGross.setFrontSeatWeight(340);
        underGross.setRearSeatWeight(190);
        underGross.setBaggageWeight(45);
        underGross.setFuelLoaded(40);

        LoadSheet overGross = new LoadSheet();
        overGross.setEmptyWeight(1680);
        overGross.setEmptyArm(39.1);
        overGross.setFrontSeatWeight(340);
        overGross.setRearSeatWeight(190);
        overGross.setBaggageWeight(45);
        overGross.setFuelLoaded(53);

        LoadSheetBO.calculate(underGross);
        LoadSheetBO.calculate(overGross);

        assertTrue("PASS".equals(underGross.getLoadStatus()));
        assertFalse("PASS".equals(overGross.getLoadStatus()));
    }


    //****************************************************************************
    //The following unit tests were created by AI from the README requirements
    //and the LoadSheet entity class
    //****************************************************************************

    private LoadSheet buildLoadSheet(double emptyWeight, double emptyArm, double frontSeatWeight,
                                     double rearSeatWeight, double baggageWeight, double fuelLoaded) {
        LoadSheet loadSheet = new LoadSheet();
        loadSheet.setEmptyWeight(emptyWeight);
        loadSheet.setEmptyArm(emptyArm);
        loadSheet.setFrontSeatWeight(frontSeatWeight);
        loadSheet.setRearSeatWeight(rearSeatWeight);
        loadSheet.setBaggageWeight(baggageWeight);
        loadSheet.setFuelLoaded(fuelLoaded);
        return loadSheet;
    }

    // Happy path: the worked example sets all three calculated fields on the load sheet
    @Test
    void calculate_workedExampleSetsAllCalculatedFields() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 45, 40);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertAll(
                () -> assertEquals(2495, loadSheet.getTotalWeight(), 0.01),
                () -> assertEquals(43.26, loadSheet.getCentreOfGravity(), 0.01),
                () -> assertEquals("PASS", loadSheet.getLoadStatus()),
                () -> assertEquals(actual, loadSheet.getCentreOfGravity(), 0.0001)
        );
    }

    // No fuel loaded adds no weight or moment
    @Test
    void calculate_noFuelAddsNoWeight() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 45, 0);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(2255, loadSheet.getTotalWeight(), 0.01);
        assertEquals(42.76, actual, 0.01);
    }

    // Boundary: exactly the 2550 lb maximum gross weight is allowed
    @Test
    void calculate_exactlyMaxGrossWeightPasses() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 100, 40);

        LoadSheetBO.calculate(loadSheet);

        assertEquals(2550, loadSheet.getTotalWeight(), 0.01);
        assertEquals("PASS", loadSheet.getLoadStatus());
    }

    // Boundary: 1 lb over the maximum gross weight fails
    @Test
    void calculate_oneOverMaxGrossWeightFails() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 101, 40);

        LoadSheetBO.calculate(loadSheet);

        assertEquals(2551, loadSheet.getTotalWeight(), 0.01);
        assertEquals("FAIL", loadSheet.getLoadStatus());
    }

    // Boundary: exactly the 120 lb maximum baggage is allowed
    @Test
    void calculate_exactlyMaxBaggagePasses() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 120, 27.5);

        LoadSheetBO.calculate(loadSheet);

        assertEquals("PASS", loadSheet.getLoadStatus());
    }

    // Baggage over 120 lb fails even though the weight and CG are within limits
    @Test
    void calculate_baggageOverMaxFails() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 121, 27.5);

        LoadSheetBO.calculate(loadSheet);

        assertTrue(loadSheet.getTotalWeight() <= 2550);
        assertEquals("FAIL", loadSheet.getLoadStatus());
    }

    // CG behind the 47.3 in aft limit fails even though the aircraft is under gross weight
    @Test
    void calculate_cgAftOfLimitFails() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 170, 400, 120, 20);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(47.53, actual, 0.01);
        assertTrue(loadSheet.getTotalWeight() <= 2550);
        assertEquals("FAIL", loadSheet.getLoadStatus());
    }

    // Less weight in the rear seats brings the CG back inside the aft limit
    @Test
    void calculate_cgInsideAftLimitPasses() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 170, 300, 120, 20);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(46.46, actual, 0.01);
        assertEquals("PASS", loadSheet.getLoadStatus());
    }

    // At or below 1950 lb the forward limit is 35.0 in, so a CG of 34.43 in fails
    @Test
    void calculate_cgForwardOfLimitAtLightWeightFails() {
        LoadSheet loadSheet = buildLoadSheet(1500, 33.0, 200, 0, 0, 20);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(1820, loadSheet.getTotalWeight(), 0.01);
        assertEquals(34.43, actual, 0.01);
        assertEquals("FAIL", loadSheet.getLoadStatus());
    }

    // At or below 1950 lb a CG just behind 35.0 in passes
    @Test
    void calculate_cgInsideForwardLimitAtLightWeightPasses() {
        LoadSheet loadSheet = buildLoadSheet(1500, 34.5, 200, 0, 0, 20);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(35.66, actual, 0.01);
        assertEquals("PASS", loadSheet.getLoadStatus());
    }

    // Above 1950 lb the forward limit slopes up: at 2250 lb it is 38.0 in,
    // so a CG of 37.36 in fails even though it is behind 35.0 in
    @Test
    void calculate_cgForwardOfSlopedLimitFails() {
        LoadSheet loadSheet = buildLoadSheet(1700, 36.5, 400, 0, 0, 25);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(2250, loadSheet.getTotalWeight(), 0.01);
        assertEquals(37.36, actual, 0.01);
        assertTrue(actual > 35.0);
        assertEquals("FAIL", loadSheet.getLoadStatus());
    }

    // At 2250 lb a CG of 38.11 in is just behind the 38.0 in sloped limit and passes
    @Test
    void calculate_cgInsideSlopedForwardLimitPasses() {
        LoadSheet loadSheet = buildLoadSheet(1700, 37.5, 400, 0, 0, 25);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(38.11, actual, 0.01);
        assertEquals("PASS", loadSheet.getLoadStatus());
    }

    // Recalculating after the pilot removes fuel replaces the old FAIL with PASS
    @Test
    void calculate_recalculateAfterChangeUpdatesStatus() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 45, 53);

        LoadSheetBO.calculate(loadSheet);
        assertEquals("FAIL", loadSheet.getLoadStatus());

        loadSheet.setFuelLoaded(40);
        LoadSheetBO.calculate(loadSheet);

        assertNotNull(loadSheet.getLoadStatus());
        assertEquals(2495, loadSheet.getTotalWeight(), 0.01);
        assertEquals("PASS", loadSheet.getLoadStatus());
    }

    // Each loading station uses its own arm: 100 lb (or 20 gal of fuel) added to one station at a time
    @ParameterizedTest(name = "{0}")
    @CsvSource(textBlock = """
            # station,     front, rear, baggage, fuel, expectedTotalWeight, expectedCg
            'front seats', 100,   0,    0,       0,    1600,                39.8125
            'rear seats',  0,     100,  0,       0,    1600,                42.0625
            'baggage',     0,     0,    100,     0,    1600,                43.4375
            'fuel 20 gal', 0,     0,    0,       20,   1620,                40.5926
            """)
    void calculate_eachStationUsesItsOwnArm(String station, double frontSeatWeight, double rearSeatWeight,
                                            double baggageWeight, double fuelLoaded,
                                            double expectedTotalWeight, double expectedCg) {
        LoadSheet loadSheet = buildLoadSheet(1500, 40.0, frontSeatWeight, rearSeatWeight, baggageWeight, fuelLoaded);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertEquals(expectedTotalWeight, loadSheet.getTotalWeight(), 0.0001);
        assertEquals(expectedCg, actual, 0.0001);
    }

    // Every CG limit tested exactly on the line (PASS) and just past it (FAIL).
    // The numbers are chosen so the CG lands exactly on each limit.
    @ParameterizedTest(name = "{0}")
    @CsvSource(textBlock = """
            # case,                                         emptyWeight, emptyArm, front, rear, baggage, fuel, expectedStatus
            'CG exactly on the 47.3 aft limit',              1600,        45.375,   200,   200,  0,       0,    PASS
            'CG just aft of 47.3',                           1600,        45.375,   200,   201,  0,       0,    FAIL
            'CG exactly on the 35.0 forward limit',          1500,        34.5,     375,   0,    0,       0,    PASS
            'CG just forward of 35.0',                       1500,        34.5,     374,   0,    0,       0,    FAIL
            'CG 35.0 at 1950 lb, slope not started',         1560,        34.5,     390,   0,    0,       0,    PASS
            'CG 35.001 at 1951 lb, limit has moved to 35.01', 1560,       34.5,     391,   0,    0,       0,    FAIL
            'CG exactly on the 41.0 forward limit at 2550',  1700,        37.0,     400,   210,  0,       40,   PASS
            'CG just forward of 41.0 at 2550',               1700,        37.0,     401,   209,  0,       40,   FAIL
            """)
    void calculate_cgLimitBoundaries(String description, double emptyWeight, double emptyArm,
                                     double frontSeatWeight, double rearSeatWeight, double baggageWeight,
                                     double fuelLoaded, String expectedStatus) {
        LoadSheet loadSheet = buildLoadSheet(emptyWeight, emptyArm, frontSeatWeight, rearSeatWeight,
                baggageWeight, fuelLoaded);

        LoadSheetBO.calculate(loadSheet);

        assertEquals(expectedStatus, loadSheet.getLoadStatus());
    }

    // Every limit broken at once still gives a single FAIL
    @Test
    void calculate_allLimitsExceededFails() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 170, 400, 150, 53);

        double actual = LoadSheetBO.calculate(loadSheet);

        assertTrue(loadSheet.getTotalWeight() > 2550);
        assertTrue(actual > 47.3);
        assertEquals("FAIL", loadSheet.getLoadStatus());
    }

    // The pilot's entries are not changed by the calculation
    @Test
    void calculate_doesNotChangeEnteredValues() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 45, 40);

        LoadSheetBO.calculate(loadSheet);

        assertAll(
                () -> assertEquals(1680, loadSheet.getEmptyWeight()),
                () -> assertEquals(39.1, loadSheet.getEmptyArm()),
                () -> assertEquals(340, loadSheet.getFrontSeatWeight()),
                () -> assertEquals(190, loadSheet.getRearSeatWeight()),
                () -> assertEquals(45, loadSheet.getBaggageWeight()),
                () -> assertEquals(40, loadSheet.getFuelLoaded())
        );
    }

    // Calculating the same sheet twice gives the same answer, so nothing builds up between calls
    @Test
    void calculate_twiceGivesSameResult() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 45, 40);

        double first = LoadSheetBO.calculate(loadSheet);
        double second = LoadSheetBO.calculate(loadSheet);

        assertEquals(first, second);
        assertEquals(2495, loadSheet.getTotalWeight(), 0.01);
    }

    // Old calculated values already on the sheet are replaced, not reused
    @Test
    void calculate_replacesOldCalculatedValues() {
        LoadSheet loadSheet = buildLoadSheet(1680, 39.1, 340, 190, 45, 53);
        loadSheet.setTotalWeight(9999);
        loadSheet.setCentreOfGravity(99);
        loadSheet.setLoadStatus("PASS");

        LoadSheetBO.calculate(loadSheet);

        assertEquals(2573, loadSheet.getTotalWeight(), 0.01);
        assertEquals(43.40, loadSheet.getCentreOfGravity(), 0.01);
        assertEquals("FAIL", loadSheet.getLoadStatus());
    }

    // A missing load sheet is a programming error, so calculate refuses it instead of returning a value
    @Test
    void calculate_nullLoadSheetThrows() {
        assertThrows(NullPointerException.class, () -> LoadSheetBO.calculate(null));
    }
}
