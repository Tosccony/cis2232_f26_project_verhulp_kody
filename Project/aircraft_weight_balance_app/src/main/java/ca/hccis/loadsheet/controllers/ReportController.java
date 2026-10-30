package ca.hccis.loadsheet.controllers;

import ca.hccis.loadsheet.dao.LoadSheetDAO;
import ca.hccis.loadsheet.entity.LoadSheet;
import ca.hccis.loadsheet.entity.ReportLoadSheet;
import ca.hccis.loadsheet.util.CisUtilityFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controller to administer reports of the project.
 *
 * @author BJM
 * @since 20251009
 */
@Controller
@RequestMapping("/report")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    /**
     * Send the user to list of reports view.
     *
     * @return To the appropriate view
     * @author BJM
     * @since 20251009
     */
    @RequestMapping("")
    public String home() {
        logger.info("Running the reports controller base method");
        return "report/list";
    }

    /**
     * Send the user to the aircraft loading history report input view.
     *
     * @param model
     * @return view for the report
     * @throws SQLException if the aircraft list can't be read (shown on the error page)
     * @author Kody Verhulp
     * @since 20261030
     */
    @RequestMapping("/loadsheet/history")
    public String reportLoadingHistory(Model model) throws SQLException {
        logger.info("Running the reports controller loading history method");
        model.addAttribute("reportInput", new ReportLoadSheet());
        model.addAttribute("tailNumbers", new LoadSheetDAO().selectTailNumbers());
        return "report/reportLoadingHistory";
    }

    /**
     * Process the aircraft loading history report. The load sheets matching the criteria are
     * selected using jdbc, written to a file, and shown on the same view as the input form.
     *
     * @param model
     * @param reportInput Object containing the criteria for the report
     * @return view to show the report
     * @throws SQLException if the database can't be read (shown on the error page)
     * @author Kody Verhulp
     * @since 20261030
     */
    @RequestMapping("/loadsheet/history/submit")
    public String reportLoadingHistorySubmit(Model model, @ModelAttribute("reportInput") ReportLoadSheet reportInput)
            throws SQLException {
        logger.info(reportInput.getCriteriaSummary());

        LoadSheetDAO loadSheetDAO = new LoadSheetDAO();
        model.addAttribute("tailNumbers", loadSheetDAO.selectTailNumbers());

        //A From date after the To date can't match anything: say so instead of searching
        if (!reportInput.isDateRangeValid()) {
            model.addAttribute("dateRangeError", true);
            return "report/reportLoadingHistory";
        }

        ArrayList<LoadSheet> loadSheets = loadSheetDAO.selectAllByCriteria(reportInput);
        reportInput.setLoadSheets(loadSheets);

        //Also write the report to a file: the criteria on the first line, then one line per load sheet.
        //The page shows where it was saved: the file name and folder separately, and the full path to copy.
        if (!loadSheets.isEmpty()) {
            ArrayList<Object> reportLines = new ArrayList<>();
            reportLines.add(reportInput.getCriteriaSummary());
            reportLines.addAll(loadSheets);
            String reportFile = CisUtilityFile.writeReportToFile("AircraftLoadingHistory", reportLines);
            model.addAttribute("reportSaved", reportFile != null);
            if (reportFile != null) {
                Path path = Paths.get(reportFile);
                model.addAttribute("reportFile", reportFile);
                model.addAttribute("reportFileName", path.getFileName().toString());
                model.addAttribute("reportFolder", path.getParent().toString());
            }
        }

        //@ModelAttribute has already put reportInput in the model for the view
        return "report/reportLoadingHistory";
    }

}
