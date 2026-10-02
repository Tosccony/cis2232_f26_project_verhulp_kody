package ca.hccis.loadsheet.bo;


import ca.hccis.loadsheet.entity.LoadSheet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadSheetBOTest {

    /**
     * Test 1 following TDD
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
}
