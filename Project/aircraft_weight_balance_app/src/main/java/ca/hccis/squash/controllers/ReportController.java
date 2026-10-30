package ca.hccis.squash.controllers;

import ca.hccis.squash.dao.LoadSheetDAO;
import ca.hccis.squash.entity.LoadSheet;
import ca.hccis.squash.entity.ReportLoadSheet;
import ca.hccis.squash.util.CisUtilityFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
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
     * @param model
     * @param session
     * @return To the appropriate view
     * @author BJM
     * @since 20251009
     */
    @RequestMapping("")
    public String home(Model model, HttpSession session) {

        //BJM 20200602 Issue#1 Set the current date in the session
        logger.info("Running the reports controller base method");
        return "report/list";
    }

    /**
     * Send the user to the aircraft loading history report input view.
     *
     * @param model
     * @return view for the report
     * @author Kody Verhulp
     * @since 20261030
     */
    @RequestMapping("/loadsheet/history")
    public String reportLoadingHistory(Model model) {
        logger.info("Running the reports controller loading history method");
        model.addAttribute("reportInput", new ReportLoadSheet());
        return "report/reportLoadingHistory";
    }

    /**
     * Process the aircraft loading history report. The load sheets are selected using jdbc,
     * written to a file, and shown on the same view as the input form.
     *
     * @param reportInput Object containing the inputs for the report
     * @return view to show the report
     * @throws SQLException if the database can't be read (shown on the error page)
     * @author Kody Verhulp
     * @since 20261030
     */
    @RequestMapping("/loadsheet/history/submit")
    public String reportLoadingHistorySubmit(@ModelAttribute("reportInput") ReportLoadSheet reportInput)
            throws SQLException {

        //Tail numbers are stored in upper case without the dash (C-GKDV is saved as CGKDV)
        reportInput.setTailNumber(reportInput.getTailNumber().replace("-", "").strip().toUpperCase());
        logger.info("Loading history report for " + reportInput.getTailNumber());

        LoadSheetDAO loadSheetDAO = new LoadSheetDAO();
        ArrayList<LoadSheet> loadSheets = loadSheetDAO.selectAllByTailNumberAndDates(
                reportInput.getTailNumber(), reportInput.getStartDate(), reportInput.getEndDate());
        reportInput.setLoadSheets(loadSheets);

        //Also write the report to a file
        if (!loadSheets.isEmpty()) {
            CisUtilityFile.writeReportToFile("AircraftLoadingHistory", loadSheets);
        }

        //@ModelAttribute has already put reportInput in the model for the view
        return "report/reportLoadingHistory";
    }

}
