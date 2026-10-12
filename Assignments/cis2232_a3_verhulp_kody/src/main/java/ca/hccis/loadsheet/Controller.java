package ca.hccis.loadsheet;

import ca.hccis.loadsheet.entity.LoadSheet;
import ca.hccis.loadsheet.util.CisUtility;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Controls the overall flow of the program.
 *
 * @author Kody Verhulp
 * @since 2026-09-25
 */
public class Controller {

    public static final String MENU = "A) Add" + System.lineSeparator() +
            "V) View" + System.lineSeparator() + "X) eXit";

    public static final String PATH = "c:/cis2232/";
    public static final String FILE_NAME = "data_verhulp_kody.json";
    private static final Path FILE_PATH =Paths.get(PATH + FILE_NAME);
    private static FileWriter fileWriter;

    /**
     * Create the data folder and file if needed, show the menu until the
     * user picks X, then close the file.
     *
     * @param args Not used
     * @since 20260925
     * @author Kody Verhulp
     */
    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(PATH));
            fileWriter = new FileWriter(PATH + FILE_NAME, true);
        } catch (IOException e) {
            System.out.println("Could not open the data file: " + e.getMessage());
            return;
        }

        String option;
        do {
            option = CisUtility.getInputString(MENU).toUpperCase();
            switch (option) {
                case "A":
                    processAdd();
                    break;
                case "V":
                    processShow();
                    break;
                case "X":
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        } while (!option.equals("X"));

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Could not close the data file.");
        }
    }

    /**
     * Create a new load sheet with the next id, get its details from the
     * user and save it to the file right away.
     *
     * @since 20260925
     * @author Kody Verhulp
     */
    public static void processAdd() {
        LoadSheet loadSheet = new LoadSheet();
        loadSheet.setLoadSheetId(readSavedLines().size() + 1);
        loadSheet.getInformation();

        try {
            fileWriter.write(loadSheet.toJson() + System.lineSeparator());
            fileWriter.flush();
            System.out.println("Load sheet " + loadSheet.getLoadSheetId() + " has been saved.");
        } catch (IOException e) {
            System.out.println("Could not save the load sheet: " + e.getMessage());
        }
    }

    /**
     * Show every load sheet saved in the file, or a message if there are none.
     *
     * @since 20260925
     * @author Kody Verhulp
     */
    public static void processShow() {
        List<String> lines = readSavedLines();
        if (lines.isEmpty()) {
            System.out.println("No load sheets exist.");
            return;
        }

        Gson gson = new Gson();
        for (String line : lines) {
            LoadSheet loadSheet = gson.fromJson(line, LoadSheet.class);
            System.out.println(loadSheet);
        }
    }

    /**
     * Read the saved load sheets from the file.
     *
     * @return One JSON line per load sheet, or an empty list if the file can't be read
     * @since 20260925
     * @author Kody Verhulp
     */
    private static List<String> readSavedLines() {
        try {
            return Files.readAllLines(FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error reading data file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
