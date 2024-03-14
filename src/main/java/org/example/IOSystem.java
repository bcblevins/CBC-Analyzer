package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.example.Main.*;    //need to come up with alternative. Not good style?


public class IOSystem {
    private final String SEPARATOR = "---------------------------------------------------------";
    private final String ANSI_RED_CODE = "\u001B[31m";
    private final String ANSI_RESET_CODE = "\u001B[0m";
    static final String FILE_SEPARATOR = "::";
    public final int SEARCH_BY_NAME = 1;
    public final int SEARCH_BY_DATE = 2;
    private Scanner input = new Scanner(System.in);

    //------------------
    // Class Methods
    //------------------
    public String takePatientName() {
        System.out.println("Please enter patient name: (last, first)");

        return input.nextLine();
    }
    public String displayMenu(String title, String... args) {
        //Display options
        System.out.println(SEPARATOR);
        System.out.println(title);
        Set<String> options = new HashSet<>();
        int choiceNumber = 0;
        for (int i = 0 ; i < args.length ; i++) {
            choiceNumber = i + 1;
            options.add(String.valueOf(choiceNumber));
            System.out.println("  " + choiceNumber + ". " + args[i]);
        }

        //Validate choice and re-prompt if necessary
        String choice = input.nextLine();
        while (true) {
            if (options.contains(choice)) {
                break;
            } else {
                System.out.println("That is not a valid option. Please select an option between 1 and " + choiceNumber);
                choice = input.nextLine();
            }
        }
        return choice;
    }

    public Map<String, Double> takeBloodValues(){
        System.out.println("Please enter the following values one at a time. Do not include spaces or units.");
        Map<String, Double> bloodMap = new HashMap<>();

        try {
            System.out.println("White blood cell count:");
            bloodMap.put(analyzer.getWbcName(), Double.parseDouble(input.nextLine()));

            System.out.println("Red blood cell count:");
            bloodMap.put(analyzer.getRbcName(), Double.parseDouble(input.nextLine()));

            System.out.println("Hemoglobin:");
            bloodMap.put(analyzer.getHemoglobinName(), Double.parseDouble(input.nextLine()));

            System.out.println("Hematocrit percent:");
            bloodMap.put(analyzer.getHematocritName(), Double.parseDouble(input.nextLine()));

            System.out.println("Mean crepuscular volume:");
            bloodMap.put(analyzer.getMcvName(), Double.parseDouble(input.nextLine()));

            System.out.println("Platelet count:");
            bloodMap.put(analyzer.getPlateletsName(), Double.parseDouble(input.nextLine()));

        } catch (Exception e) {
            System.out.println("Invalid input. Try again:");
            takeBloodValues();
        }

        return bloodMap;
    }
    public String createTable(List<BloodParameter> bloodParameterList, String name) {
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
                outputTable.append(outlineInRed(row));
                outputTable.append("\n");
            } else {
                outputTable.append(row);
                outputTable.append("\n");
            }
        }
        return outputTable.toString();
    }
    public void writeToRecord(String table){
        File logFile = new File(patient.getRecordFilePath());
        try (PrintWriter dataOutput = new PrintWriter(
                new FileOutputStream(logFile, true)
        )) {
            dataOutput.println(getDate());
            dataOutput.println(removeColor(table)); //remove color so that log file is more readable.
            dataOutput.println(FILE_SEPARATOR);
        } catch (Exception e) {
            System.out.println("Could not write to patient record.");
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

                if (currentLine.toUpperCase().contains(searchTerm.toUpperCase())){
                    isDesiredLogEntry = true;
                    if (nameOrDate == SEARCH_BY_NAME) {
                        matchingLogEntries.append(previousLine); //This adds the previous line (which includes the entry's date) to the output string.
                    }
                } else if (currentLine.contains(FILE_SEPARATOR)) {
                    isDesiredLogEntry = false;
                }
                if (isDesiredLogEntry) {
                    if (currentLine.contains("(+)") || currentLine.contains("(-)")) {
                        matchingLogEntries.append(outlineInRed(currentLine));
                    } else {
                        matchingLogEntries.append(currentLine);

                    }
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
    public Patient selectPatientRecord(String patientID){
        File patientFile = new File ("src/main/resources/patient-records/" + patientID + ".dat");
        if (patientFile.exists()) {
            System.out.println("This patient has a record on file.");
            return getPatientRecord(patientFile);
        } else {
            System.out.println("No patient with that ID was found.");
            String choice = displayMenu("Would you like to:", "Set up a new patient record", "Quit");
            if (choice.equals("1")) {
                return createPatientRecord(patientID, patientFile);
            } else {
                return new Patient(true);
            }
        }
    }
    private Patient createPatientRecord(String id, File patientFile) {
        String name = promptForInput("Please enter the patient's name (Last, First)");
        String choice = displayMenu("Please enter the patient's sex:", "F", "M", "SF", "CM");

        String sex = switch (choice) {
            case "1" -> "F";
            case "2" -> "M";
            case "3" -> "SF";
            case "4" -> "CM";
            default -> "";
        };
        choice = displayMenu("Please enter the patient's species", "Canine");
        String species = "";
        switch (choice) {
            case "1":
                species = "Canine";
        }

        String dob = promptForInput("Please enter the patient's date of birth (YYYY-MM-DD):");
        LocalDate dateOfBirth;
        while (true) {
            try {
                dateOfBirth = LocalDate.parse(dob);
                break;
            } catch (DateTimeParseException e) {
                System.out.println(e.getMessage());
                dob = promptForInput("Invalid date. Please try again:");
            }
        }

        String flagsRaw = promptForInput("Please enter any patient flags you would like to add to this patients record, separated by commas (flag1,flag2,flag3):");
        List<String> flags = Arrays.asList(flagsRaw.split(","));

        //create file
        try (PrintWriter dataOutput = new PrintWriter(patientFile)){
            dataOutput.println(id + "\n"
                    + "-" + "\n"
                    + name + "\n"
                    + sex + "\n"
                    + species + "\n"
                    + dateOfBirth + "\n"
                    + flagsRaw + "\n"
                    + FILE_SEPARATOR);
        } catch (FileNotFoundException e) {
            System.out.println("There was an error writing to patient file.");
        }

        return new Patient(id, name, sex, species, dateOfBirth, flags, patientFile.getPath());

    }
    private Patient getPatientRecord(File patientFile) {
        List<String> patientInfo = new ArrayList<>();
        try (Scanner dataInput = new Scanner(patientFile)) {
            while (dataInput.hasNextLine()) {
                String currentLine = dataInput.nextLine();
                if (currentLine.equals("-")) {
                    continue;
                } else if (currentLine.equals(FILE_SEPARATOR)) {
                    break;
                }
                patientInfo.add(currentLine);
            }
        } catch (FileNotFoundException e) {
            System.out.println("There was an issue reading the patient file.");
        }

        String id = patientInfo.get(0);
        String name = patientInfo.get(1);
        String sex = patientInfo.get(2);
        String species = patientInfo.get(3);
        String dob = patientInfo.get(4);
        String flagsRaw = patientInfo.get(5);

        LocalDate dateOfBirth = LocalDate.parse(dob);
        List<String> flags = List.of(flagsRaw.split(","));

        return new Patient(id, name, sex, species, dateOfBirth, flags, patientFile.getPath());
    }

    //----------------------
    //  Helper/Small Methods
    //----------------------
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
    public void printInRed(String message) {
        System.out.println(ANSI_RED_CODE + message + ANSI_RESET_CODE);
    }
    public String outlineInRed(String message) {
        return ANSI_RED_CODE + message + ANSI_RESET_CODE;
    }
    public String promptForInput(String prompt){
        System.out.println(prompt);
        return input.nextLine();
    }

}
