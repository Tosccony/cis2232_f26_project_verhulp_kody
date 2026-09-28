#For localhost
DROP DATABASE IF EXISTS cis2232_aircraft_weight_balance;
CREATE DATABASE cis2232_aircraft_weight_balance;
use cis2232_aircraft_weight_balance;

-- ------------------------------------------------------------------------------
-- One row per flight.  totalWeight, centreOfGravity and loadStatus are
-- calculated by the application when the sheet is saved.
-- ------------------------------------------------------------------------------

CREATE TABLE load_sheet (
                            loadSheetId      INT            NOT NULL AUTO_INCREMENT,
                            flightDate       VARCHAR(10)    NOT NULL COMMENT 'yyyy-MM-dd',
                            tailNumber       VARCHAR(10)    NOT NULL COMMENT 'Aircraft registration, e.g. CGABC',
                            pilotName        VARCHAR(100)   NOT NULL,
                            emptyWeight      DOUBLE         NOT NULL COMMENT 'lb',
                            emptyArm         DOUBLE         NOT NULL COMMENT 'inches aft of datum',
                            frontSeatWeight  DOUBLE         NOT NULL DEFAULT 0 COMMENT 'lb',
                            rearSeatWeight   DOUBLE         NOT NULL DEFAULT 0 COMMENT 'lb',
                            baggageWeight    DOUBLE         NOT NULL DEFAULT 0 COMMENT 'lb',
                            fuelLoaded       DOUBLE         NOT NULL DEFAULT 0 COMMENT 'US gallons',
                            totalWeight      DOUBLE         NOT NULL COMMENT 'lb, calculated',
                            centreOfGravity  DOUBLE         NOT NULL COMMENT 'inches aft of datum, calculated',
                            loadStatus       VARCHAR(4)     NOT NULL COMMENT 'PASS or FAIL, calculated',
                            PRIMARY KEY (loadSheetId)
);

INSERT INTO load_sheet
(flightDate, tailNumber, pilotName,
 emptyWeight, emptyArm,
 frontSeatWeight, rearSeatWeight, baggageWeight, fuelLoaded,
 totalWeight, centreOfGravity, loadStatus)
VALUES
-- worked example from the topic document
('2026-09-01', 'CGKDV', 'Alex Martin',   1680, 39.1, 340, 190, 45,  40, 2495.0, 43.26, 'PASS'),
-- solo, full fuel
('2026-09-03', 'CGKDV', 'Priya Shah',    1680, 39.1, 180, 0,   0,   53, 2178.0, 40.23, 'PASS'),
('2026-09-06', 'CFWBA', 'Sam Arsenault', 1702, 40.2, 360, 0,   30,  45, 2362.0, 41.30, 'PASS'),
-- over maximum gross weight (2550 lb)
('2026-09-10', 'CFWBA', 'Jordan Lee',    1702, 40.2, 380, 340, 60,  53, 2800.0, 45.81, 'FAIL'),
-- baggage over 120 lb
('2026-09-12', 'CGKDV', 'Alex Martin',   1680, 39.1, 190, 0,   150, 30, 2200.0, 43.46, 'FAIL'),
-- forward of the CG limit (39.88 in at 2438 lb)
('2026-09-15', 'CGPEI', 'Priya Shah',    1680, 38.0, 440, 0,   0,   53, 2438.0, 39.12, 'FAIL'),
-- aft of the CG limit (47.3 in)
('2026-09-18', 'CGPEI', 'Sam Arsenault', 1680, 39.1, 170, 400, 120, 20, 2490.0, 47.53, 'FAIL');
