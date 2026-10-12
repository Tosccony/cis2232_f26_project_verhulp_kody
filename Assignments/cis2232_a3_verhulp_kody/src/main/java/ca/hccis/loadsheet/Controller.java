package ca.hccis.loadsheet;

import ca.hccis.loadsheet.bo.LoadSheetBO;
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
    //Accumulator: load sheets added by both threads (loadSheetsAdded)
    private static int loadSheetsAdded = 0;

    /**
     * Create the data folder and file if needed, start the console and GUI
     * threads, wait for both to finish, then close the file.
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

        Thread consoleThread = new Thread(() -> runMenu(false));
        Thread guiThread = new Thread(() -> runMenu(true));

        consoleThread.start();
        guiThread.start();

        try {
            consoleThread.join();
            guiThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Could not close the data file.");
        }
    }

    /**
     * Show the menu until the user picks X.
     *
     * @param gui True to use a dialog, false to use the console
     * @since 20261012
     * @author Kody Verhulp
     */
    public static void runMenu(boolean gui) {
        String option;
        do {
            option = CisUtility.getInputString(MENU, gui).toUpperCase();
            switch (option) {
                case "A":
                    processAdd(gui);
                    break;
                case "V":
                    processShow(gui);
                    break;
                case "X":
                    CisUtility.display("Exiting...", gui);
                    break;
                default:
                    CisUtility.display("Invalid option", gui);
            }
        } while (!option.equals("X"));
    }

    /**
     * Create a new load sheet and gets the details, calculates it, saves it,
     * and shows the result and the running total
     *
     * @param gui True to use a dialog, false to use console
     * @since 20260925
     * @author Kody Verhulp
     */
    public static void processAdd(boolean gui) {
        LoadSheet loadSheet = new LoadSheet();
        loadSheet.getInformation(gui);
        LoadSheetBO.calculate(loadSheet);

        try {
            int added = save(loadSheet);
            CisUtility.display("Load sheet " + loadSheet.getLoadSheetId() + " has been saved.", gui);
            CisUtility.display("Load sheets added by both threads: " + added, gui);
            CisUtility.display("Total weight: " + loadSheet.getTotalWeight() + " lb, CG: " + String.format("%.2f", loadSheet.getCentreOfGravity()) + "in, Status: " + loadSheet.getLoadStatus(), gui);
        } catch (IOException e) {
            CisUtility.display("Could not save the load sheet: " + e.getMessage(), gui);
        }
    }

    /**
     * Give the load sheet the next id, write it to the file and add it to the
     * accumulator. Synchronized so only one thread saves at a time, this allows
     * the two threads to not pick the same id and overwrite each other.
     *
     * @param loadSheet Load sheet to save
     * @return Load sheet added by both threads so far
     * @throws IOException if the file can't be written
     * @since 20261012
     * @author Kody Verhulp
     */
    private static synchronized int save(LoadSheet loadSheet) throws IOException {
        loadSheet.setLoadSheetId(readSavedLines().size() + 1);
        fileWriter.write(loadSheet.toJson() + System.lineSeparator());
        fileWriter.flush();
        loadSheetsAdded++;
        return loadSheetsAdded;
    }

    /**
     * Show every load sheet saved in the file, or a message if there are none.
     *
     * @param gui True to use a dialog, false to use console
     * @since 20260925
     * @author Kody Verhulp
     */
    public static void processShow(boolean gui) {
        List<String> lines = readSavedLines();
        if (lines.isEmpty()) {
            CisUtility.display("No load sheets exist.", gui);
            return;
        }

        Gson gson = new Gson();
        StringBuilder output = new StringBuilder();
        for (String line : lines) {
            LoadSheet loadSheet = gson.fromJson(line, LoadSheet.class);
            output.append(loadSheet).append(System.lineSeparator());
        }
        CisUtility.display(output.toString(), gui);
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
