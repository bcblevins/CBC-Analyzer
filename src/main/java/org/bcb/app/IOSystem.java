package org.bcb.app;

import org.bcb.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.bcb.app.Main.*;    //need to come up with alternative. Not good style?


public class IOSystem {
    private final String SEPARATOR = "---------------------------------------------------------";
    private final String ANSI_RED_CODE = "\u001B[31m";
    private final String ANSI_RESET_CODE = "\u001B[0m";
    private final Scanner input = new Scanner(System.in);

    //------------------
    // Class Methods
    //------------------

    public Map<String, Double> takeBloodValues() {
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

    public String createTable(LabTest labTest, Patient patient) {
        String tags;
        labTest.prependTags(patient.getAgeTagObject());
        if (labTest.getTagNames().isEmpty() && patient.getAgeTag() == null) {
            tags = "";
        } else {
            tags = labTest.getTagNames().toString();
        }
        LocalDate date = labTest.getTimestamp().toLocalDate();

        String timestampFormatted = date.toString() + " " + labTest.getTimestamp().getHour() + ":" + labTest.getTimestamp().getMinute() + ":" + labTest.getTimestamp().getSecond();

        StringBuilder outputTable =
                new StringBuilder(labTest.getType() + "\n" +
                        timestampFormatted + "\n" +
                        tags + "\n" +
                        "Parameter                 |Result    |Normal Range   | Unit     |\n" +
                        "--------------------------|----------|---------------|----------|\n");

        for (BloodParameter bloodParameter : labTest.getBloodParameterList()) {
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


    public void searchForTests(String filters, Patient patient) {
        List<String> typeFilters = new ArrayList<>();
        List<LocalDate> dateFilters = new ArrayList<>();
        List<String> tagFilters = new ArrayList<>();
        String[] filtersArray = filters.split(",");
        List<LabTest> matchingTests = new ArrayList<>();

        //add filters to their respective lists, remove filter identifier (first character)
        for (String filter : filtersArray) {
            if (filter.charAt(0) == 't') {
                typeFilters.add(filter.substring(1));
            } else if (filter.charAt(0) == 'd') {
                try {
                    dateFilters.add(LocalDate.parse(filter.substring(1)));
                } catch (DateTimeParseException e) {
                    System.out.println("Failed to parse search date");
                }
            } else if (filter.charAt(0) == 'f') {
                tagFilters.add(filter.substring(1));
            }
        }

        if (!dateFilters.isEmpty()) {
            for (LocalDate dateFilter : dateFilters) {
                matchingTests.addAll(jdbcLabTestDao.getLabTestByDate(dateFilter, patient));
            }
        }
        // Only support CBC for now, add all tests for patient
        if (!typeFilters.isEmpty()) {
            matchingTests.addAll(jdbcLabTestDao.getLabTestsByPatient(patient));
        }
        if (!tagFilters.isEmpty()) {
            matchingTests.addAll(jdbcLabTestDao.getLabTestsByTags(tagFilters, true));
        }

        if (matchingTests.isEmpty()) {
            System.out.println("No log entries matching selected filters.");
        } else {
            displayTests(matchingTests, patient);
        }
    }

    public void displayTests(List<LabTest> tests, Patient patient) {

        for (LabTest test : tests) {
            StringBuilder tags = new StringBuilder();
            for (Tag tag : jdbcTagDao.getTagsForTest(test)) {
                tags.append(tag.getName()).append(",");
            }
            System.out.println(createTable(test, patient));
        }
    }

    public Patient selectPatientRecord(String chartId) {
        Patient patient = jdbcPatientDao.getPatientByChartNumber(chartId);

        if (patient != null) {
            patient.setTags(jdbcTagDao.getTagsForPatient(patient));
            System.out.println("This patient has a record on file.");
            waitForUser();
            return patient;
        } else {
            System.out.println("No patient with that chart number was found.");
            String choice = displayMenu("Would you like to:", "Set up a new patient record", "Choose another patient record", "Quit");
            if (choice.equals("1")) {
                return createPatientRecord(chartId);
            } else if (choice.equals("2")) {
                return patient;
            } else {
                patient = new Patient(true);
                return patient;
            }
        }
    }

    private Patient createPatientRecord(String chartId) {
        String name = promptForInput("Please enter the patient's name (First Last)");
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
        if (choice.equals("1")) {
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

        Patient patient = new Patient(chartId, name, sex, species, dateOfBirth);
        patient = jdbcPatientDao.createPatient(patient);


        String addTags = displayMenu("Would you like to add tags for this patient?", "Yes", "No");
        if (addTags.equals("1")) {
            patient = tagSystem.addTags(patient);
        }
        return patient;

    }
    public User createUser(String username) {
        String userInfo = "";
        String yOrN = promptForInput("Would you like to keep the username (" + username + ")? (y/n)").toLowerCase();
        if (yOrN.equals("n")) {
            username = promptForInput("Please enter a username:");
        }
        userInfo += username;
        System.out.println(userInfo);
        String firstName = promptForInput("Please enter your first name:");

        userInfo += " : " + firstName;
        System.out.println(userInfo);
        String lastName = promptForInput("Please enter your last name:");

        userInfo += " : " + lastName;
        System.out.println(userInfo);
        String password;
        while (true) {
            password = promptForInput("Please enter your password:");
            String confirmPassword = promptForInput("Please re-enter your password to confirm:");
            if (password.equals(confirmPassword)) {
                break;
            } else {
                System.out.println("Password did not match.");
                waitForUser();
            }
        }
        return jdbcUserDao.createUser(new User(firstName, lastName, false, username, password));
    }

    //----------------------
    //  Helper/Small Methods
    //----------------------
    public String displayMenu(String title, String... args) {
        //Display options
        System.out.println(SEPARATOR);
        System.out.println(title);
        Set<String> options = new HashSet<>();
        int choiceNumber = 0;
        for (int i = 0; i < args.length; i++) {
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


    public void printPatientInfo(Patient patient) {
        System.out.println(patient.isActive() ? "[ ACTIVE ]" : "[ INACTIVE ]");
        System.out.println("Name:    |" + patient.getName());
        System.out.println("Species: |" + patient.getSpecies());
        System.out.println("Sex:     |" + patient.getSex());
        System.out.println("DOB:     |" + patient.getDateOfBirth().toString());
        System.out.println("tags:   |" + patient.getTagNames());
    }

    private String createCell(String value, int cellSize) {
        return String.format("%" + (-cellSize) + "s", value) + "|";
    }

    public String outlineInRed(String message) {
        return ANSI_RED_CODE + message + ANSI_RESET_CODE;
    }

    public String promptForInput(String prompt) {
        System.out.println(prompt);
        return input.nextLine();
    }

    public void waitForUser() {
        promptForInput("Press enter when ready");
        System.out.println(SEPARATOR);
    }

    public static void printSeparator() {
        System.out.println("---------------------------------------------------------");
    }

// Deprecated
//public String createTable(List<BloodParameter> bloodParameterList, String name, String tags, LocalDateTime timestamp) {
//    if (tags.isEmpty() && patient.getAgeTag() == null) {
//        tags = "";
//    } else {
//        tags = patient.getAgeTag() + "," + tags;
//    }
//    LocalDate date = timestamp.toLocalDate();
//
//    String timestampFormatted = date.toString() + " " + timestamp.getHour() + ":" + timestamp.getMinute() + ":" + timestamp.getSecond();
//
//    StringBuilder outputTable =
//            new StringBuilder(name + "\n" +
//                    timestampFormatted + "\n" +
//                    tags + "\n" +
//                    "Parameter                 |Result    |Normal Range   | Unit     |\n" +
//                    "--------------------------|----------|---------------|----------|\n");
//
//    for (BloodParameter bloodParameter : bloodParameterList) {
//        //create name cells for each bloodParameter
//        String nameCell = createCell(bloodParameter.getName(), 26);
//
//        //create result cells
//        String resultCell = createCell(bloodParameter.getAnalyzedBloodValue(), 10);
//
//        //create normal range cells
//        String normalRangeCell = createCell(bloodParameter.getNormalRangeForOutput(), 15);
//
//        //create unit cells
//        String unitCell = createCell(bloodParameter.getUnit(), 10);
//
//        //create row
//        String row = nameCell + resultCell + normalRangeCell + unitCell;
//
//        //turn row red and print if outside analyzed parameter outside normal range, otherwise just print row
//        if (bloodParameter.isOutsideNormalRange()) {
//            outputTable.append(outlineInRed(row));
//            outputTable.append("\n");
//        } else {
//            outputTable.append(row);
//            outputTable.append("\n");
//        }
//    }
//    return outputTable.toString();
//}
//

    //    public void writeTestToRecord(String table) {
//        File logFile = new File(patient.getRecordFilePath());
//        try (PrintWriter dataOutput = new PrintWriter(
//                new FileOutputStream(logFile, true)
//        )) {
//            dataOutput.println(getDate());
//            dataOutput.println(removeColor(table)); //remove color so that log file is more readable.
//            dataOutput.println(TEST_SEPARATOR);
//        } catch (Exception e) {
//            System.out.println("Could not write to patient record.");
//        }
//    }


// ----------
//    private Patient getPatientRecord(File patientFile) {
//        List<String> patientInfo = new ArrayList<>();
//        try (Scanner dataInput = new Scanner(patientFile)) {
//            while (dataInput.hasNextLine()) {
//                String currentLine = dataInput.nextLine();
//                if (currentLine.equals("-")) {
//                    continue;
//                } else if (currentLine.equals(PATIENT_INFO_SEPARATOR)) {
//                    break;
//                }
//                patientInfo.add(currentLine);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("There was an issue reading the patient file.");
//        }
//
//        String id = patientInfo.get(0);
//        String name = patientInfo.get(1);
//        String sex = patientInfo.get(2);
//        String species = patientInfo.get(3);
//        String dob = patientInfo.get(4);
//        String tagsRaw = patientInfo.get(5);
//
//        LocalDate dateOfBirth = LocalDate.parse(dob);
//        List<String> tags = List.of(tagsRaw.split(","));
//
//        return new Patient(id, name, sex, species, dateOfBirth, tags, patientFile.getPath());
//    }
//
//    private String removeColor(String table) {
//        String colorlessTable;
//        colorlessTable = table.replace(ANSI_RED_CODE, "");
//        colorlessTable = colorlessTable.replace(ANSI_RESET_CODE, "");
//        return colorlessTable;
//    }
//
//    private String getDate() {
//        return LocalDate.now().toString();
//    }
//
//    public void printInRed(String message) {
//        System.out.println(ANSI_RED_CODE + message + ANSI_RESET_CODE);
//    }


}
