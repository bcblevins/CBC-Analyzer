package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

import static org.example.Main.*;    //need to come up with alternative. Not good style?


public class IOSystem {
    static final String ANSI_RED_CODE = "\u001B[31m";
    static final String ANSI_RESET_CODE = "\u001B[0m";
    static final String LOG_SEPARATOR = "::";
    private Scanner input = new Scanner(System.in);

    //------------------
    // Class Methods
    //------------------
    public String takePatientName() {
        System.out.println("Please enter your name: (last, first)");

        String name = input.nextLine();
        return name;
    }
    public String displayMenu(String... args) {
        System.out.println("Would you like to:");
        int menuNumber = 1;

        for (String arg : args) {
            System.out.println(menuNumber + ": " + arg);
            menuNumber++;
        }

        String choice = input.nextLine();
        return choice;
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
        String outputTable =
                name + "\n" +
                        "\n" +
                        "Parameter                 |Result    |Normal Range   | Unit     |\n" +
                        "--------------------------|----------|---------------|----------|\n";

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
                outputTable += ANSI_RED_CODE + row + ANSI_RESET_CODE + "\n";
            } else {
                outputTable += row + "\n";
            }
        }
        return outputTable;
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

    //------------------
    //  Helper Methods
    //------------------
    private String createCell(String value, int cellSize) {
        String cell = String.format("%" + (-cellSize) + "s", value) + "|";
        return cell;
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
