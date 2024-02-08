package org.example;

import java.util.*;

import static org.example.Main.*;


public class IOSystem {
    static final String ANSI_RED_CODE = "\u001B[31m";
    static final String ANSI_RESET_CODE = "\u001B[0m";
    private Scanner input = new Scanner(System.in);

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
    public void outputTable(List<BloodParameter> bloodParameterList) {
        System.out.println("Parameter                 |Result    |Normal Range   | Unit     |");
        System.out.println("--------------------------|----------|---------------|----------|");

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
                System.out.println(ANSI_RED_CODE + row + ANSI_RESET_CODE);
            } else {
                System.out.println(row);
            }
        }
    }

    public String createCell(String value, int cellSize) {
        String cell = String.format("%" + (-cellSize) + "s", value) + "|";
        return cell;
    }
}
