package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

import static org.example.Main.*;    //need to come up with alternative. Not good style?


public class IOSystem {
    static final String ANSI_RED_CODE = "\u001B[31m";
    static final String ANSI_RESET_CODE = "\u001B[0m";
    static final String LOG_SEPARATOR = "::";
    public final int SEARCH_BY_NAME = 1;
    public final int SEARCH_BY_DATE = 2;
    private Scanner input = new Scanner(System.in);

    //------------------
    // Class Methods
    //------------------
    public String takePatientName() {
        System.out.println("Please enter your name: (last, first)");

        return input.nextLine();
    }
    public String displayMenu(String... args) {
        System.out.println("Would you like to:");
        int menuNumber = 1;

        for (String arg : args) {
            System.out.println(menuNumber + ": " + arg);
            menuNumber++;
        }

        return input.nextLine();
    }

    public Map<String, Double> takeBloodValues(){
        System.out.println("Please enter the following values one at a time. Do not include spaces or units.");
        Map<String, Double> bloodMap = new HashMap<>();

        try {
            System.out.println("White blood cell count:");
            bloodMap.put(wbc.getName(), Double.parseDouble(input.nextLine()));

            System.out.println("Red blood cell count:");
            bloodMap.put(rbc.getName(), Double.parseDouble(input.nextLine()));

            System.out.println("Hemoglobin:");
            bloodMap.put(hemoglobin.getName(), Double.parseDouble(input.nextLine()));

            System.out.println("Hematocrit percent:");
            bloodMap.put(hematocrit.getName(), Double.parseDouble(input.nextLine()));

            System.out.println("Mean crepuscular volume:");
            bloodMap.put(mcv.getName(), Double.parseDouble(input.nextLine()));

            System.out.println("Platelet count:");
            bloodMap.put(platelets.getName(), Double.parseDouble(input.nextLine()));

        } catch (Exception e) {
            System.out.println("Looks like you entered an invalid input. Let's try again:");
            takeBloodValues();
        }

        return bloodMap;
    }
    public String outputTable(List<BloodParameter> bloodParameterList, String name) {
        StringBuilder outputTable =
                new StringBuilder(name + "\n" +
                        "\n" +
                        "Parameter                 |Result    |Normal Range   | Unit     |\n" +
                        "--------------------------|----------|---------------|----------|\n");

        for (BloodParameter bloodParameter : bloodParameterList) {
            //create name cells for each bloodParameter
            String nameCell = createCell(bloodParameter.getName(), 26);

            //create result cells
            String resultCell = createCell(bloodParameter.getAnalyzedBloodValue(), 10);

            //create normal range cells
            String normalRangeCell = createCell(bloodParameter.getNormalRangeForOutput(), 15);

            //create unit cells
            String unitCell = createCell(bloodParameter.getUnit(), 10);

            //create row
            String row = nameCell + resultCell + normalRangeCell + unitCell;

            //turn row red and print if outside analyzed parameter outside normal range, otherwise just print row
            if (bloodParameter.isOutsideNormalRange()) {
                outputTable.append(ANSI_RED_CODE).append(row).append(ANSI_RESET_CODE).append("\n");
            } else {
                outputTable.append(row).append("\n");
            }
        }
        return outputTable.toString();
    }
    public void writeToLog(String table){
        File logFile = new File("log.txt");
        try (PrintWriter dataOutput = new PrintWriter(
                new FileOutputStream(logFile, true)
        )) {
            dataOutput.println(getDate());
            dataOutput.println(removeColor(table));
            dataOutput.println(LOG_SEPARATOR);
        } catch (Exception e) {
            System.out.println("file not found");
        }
    }
    public String searchLog(int nameOrDate){
        if (nameOrDate == SEARCH_BY_NAME) {
            System.out.println("Please enter the name you would like to search: (last, first)");
        } else if (nameOrDate == SEARCH_BY_DATE) {
            System.out.println("Please enter the date you would like to search: (do not include leading zeroes (01/02/2022))");
        }

        String searchTerm = input.nextLine();
        File logFile = new File("log.txt");

        StringBuilder matchingLogEntries = new StringBuilder();

        try (Scanner dataInput = new Scanner(logFile)){
            boolean isDesiredLogEntry = false; //This is used to start and stop adding lines to the output string.
            String previousLine = "";

            while (dataInput.hasNextLine()){
                String currentLine = dataInput.nextLine() + "\n";
                if (currentLine.contains(searchTerm)){
                    isDesiredLogEntry = true;
                    if (nameOrDate == SEARCH_BY_NAME) {
                        matchingLogEntries.append(previousLine); //This adds the previous line (which includes the entry's date) to the output string.
                    }
                } else if (currentLine.contains(LOG_SEPARATOR)) {
                    isDesiredLogEntry = false;
                }
                if (isDesiredLogEntry) {
                    matchingLogEntries.append(currentLine);
                }
                previousLine = currentLine;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Log file not found.");
        }

        if (matchingLogEntries.isEmpty()) {
            return "No log entries matching that name.";
        } else {
            return matchingLogEntries.toString();
        }
    }

    //------------------
    //  Helper Methods
    //------------------
    private String createCell(String value, int cellSize) {
        return String.format("%" + (-cellSize) + "s", value) + "|";
    }

    private String removeColor(String table) {
        String colorlessTable;
        colorlessTable = table.replace(ANSI_RED_CODE, "");
        colorlessTable = colorlessTable.replace(ANSI_RESET_CODE, "");
        return colorlessTable;
    }
    private String getDate() {
        Calendar currentDate = new GregorianCalendar();
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        int month = 1 + currentDate.get(Calendar.MONTH); //1 is added because months are zero based
        int year = currentDate.get(Calendar.YEAR);

        return month + "/" + day + "/" + year;
    }

}
