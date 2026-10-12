package ca.hccis.loadsheet.util;

import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * Has some useful methods to be used in our programs.
 *
 * @author bjmaclean
 * @since Oct 19, 2021
 */
public class CisUtility {

    private static Scanner input = new Scanner(System.in);

    /**
     * Return the default currency String value of the double passed in as a
     * parameter.
     *
     * @param inputDouble double to be formatted
     * @return String in default currency format
     *
     * @since 20211020
     * @author BJM
     */
    public static String toCurrency(double inputDouble) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(inputDouble);
    }

    /**
     * Get input from the user using the console
     *
     * @param prompt Prompt for the user
     * @return String entered by the user
     * @since 20211020
     * @author BJM
     */
    public static String getInputString(String prompt) {

        System.out.println(prompt + " -->");
        String output = input.nextLine();
        return output;
    }

    /**
     * Get input from the user using the console
     *
     * @param prompt Prompt for the user
     * @return String entered by the user
     * @since 20211020
     * @author BJM
     */
    public static String getInputString(String prompt, int minLength, int maxLength) {

        System.out.println(prompt+ " ("+minLength+" to "+ maxLength+" characters" + " -->");
        String output = input.nextLine();

        while(output.length() < minLength || output.length() > maxLength) {
            System.out.println(prompt + " -->");
            output = input.nextLine();
        }

        return output;
    }


    /**
     * Get input from the user using the console
     *
     * @param prompt Prompt for the user
     * @return The double entered by the user
     * @since 20211020
     * @author BJM
     */
    public static double getInputDouble(String prompt) {

        String inputString = getInputString(prompt);
        double output = Double.parseDouble(inputString);
        return output;
    }



    /**
     * Get a number from the user while asking again until
     * it is valid and in range.
     *
     * @param prompt Prompt for the user
     * @param min Lowest value allowed by the system
     * @param max Highest value allowed by the system (Double.MAX_VALUE for no limit)
     * @since 20260925
     * @author Kody Verhulp
     */
    public static double getInputDouble(String prompt, double min, double max) {
        while (true) {
            String inputString = getInputString(prompt);
            try {
                double value = Double.parseDouble(inputString);
                if (value >= min && value <= max) {
                    return value;
                }
                if (max == Double.MAX_VALUE) {
                    System.out.println("Must be at least " + min + ".");
                } else {
                    System.out.println("Must be between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Get input from the user using the console
     *
     * @param prompt Prompt for the user
     * @return The double entered by the user
     * @since 20211020
     * @author BJM
     */
    public static int getInputInt(String prompt) {

        String inputString = getInputString(prompt);
        int output = Integer.parseInt(inputString);
        return output;
    }

    /**
     * Get input boolean from the user using the console
     *
     * @param prompt Prompt for the user
     * @return boolean as specified by user input
     * @since 20211108
     * @author BJM
     */
    public static boolean getInputBoolean(String prompt) {

        String inputString = getInputString(prompt+" (y/n)");
        if(inputString.equalsIgnoreCase("y")){
            return true;
        }else{
            return false;
        }
        
    }

     /**
     * Get input boolean from the user using the console
     *
     * @param prompt Prompt for the user
     * @return boolean as specified by user input
     * @since 20211108
     * @author BJM
     */
    public static boolean getInputBoolean(String prompt, String affirmative, String negative) {

        String inputString = getInputString(prompt+" ("+affirmative+"/"+negative+")");
        if(inputString.equalsIgnoreCase(affirmative)){
            return true;
        }else{
            return false;
        }
        
    }


    
    /**
     * Provide today's date in the specified format
     *
     * @param format Date format desired
     * @return Today's date in specified format
     * @since 20211021
     * @author BJM
     */
    public static String getTodayString(String format) {
        //https://www.javatpoint.com/java-get-current-date

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);

    }

    /**
     * Get a date from the user in a yyyy-MM-dd format.
     * Asks until the date is a valid date, if left empty
     * it will default to today's date.
     *
     * @param prompt Prompt for the user
     * @return The date entered by the user (yyyy-MM-dd)
     * @since 20260925
     * @author Kody Verhulp
     */
    public static String getInputDate(String prompt) {
        while (true) {
            String inputString = getInputString(prompt).trim();
            if (inputString.isEmpty()) {
                return getTodayString("yyyy-MM-dd");
            }
            try {
                LocalDate.parse(inputString);
                return inputString;
            } catch (DateTimeException e) {
                System.out.println("Please use yyyy-MM-dd");
            }
        }
    }

        /**
     * Get a random number between min and max
     * @since 20211109
     * @author BJM
     */
    public static int getRandom(int min, int max){
        Random rand = new Random();
        int theRandomNumber = rand.nextInt((max - min) + 1) + min;
        return theRandomNumber;
    }
    
    
}
