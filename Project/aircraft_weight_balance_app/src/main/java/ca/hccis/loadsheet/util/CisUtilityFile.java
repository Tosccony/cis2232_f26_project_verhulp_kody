package ca.hccis.loadsheet.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CisUtilityFile {

    //Forward slashes work on Windows (c:\cis2232\). On a Mac this is a c:/cis2232/ folder
    //inside the project, which .gitignore leaves out.
    public static final String FOLDER_NAME = "c:/cis2232/";

    public static void writeReportToFile(String fileName, ArrayList theObjects) {

        String fullName = FOLDER_NAME + fileName + CisUtility.getCurrentDate("_yyyyMMddHHmmss") + ".txt";
        Path path = Paths.get(FOLDER_NAME);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.out.println("BJM-CisUtilityFile-Error creating directories");
        }

        //try-with-resources closes the writer (the old finally crashed when the file couldn't be opened)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullName, true))) {
            for (Object current : theObjects) {
                writer.write(current.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("CisUtilityFile exception with file: " + fullName);
        }

    }
}