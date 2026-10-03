package ca.hccis.loadsheet.bo;


import ca.hccis.loadsheet.entity.LoadSheet;
import org.junit.jupiter.api.Test;

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
        assertFalse("PASS".equals(overGross.getLoadStatus()));underGross.setEmptyWeight(1680);
    }
}
