package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/*
    TODO:
     Analyze:
        - Add units to cbc prompts
     Main:
     IOSystem:
        - Make static
     Search:
        - Change to a search class
        - Explain that search is (greedy? nonspecific)
     Other:
        - Create more data for testing (weight loss tag)

 */
public class Main {
    public static IOSystem iOSys = new IOSystem();
    public static Analyzer analyzer = new Analyzer();
    public static Patient patient;

    public static void main(String[] args) {
        while (true) {
            String patientID = iOSys.promptForInput("Please enter a patient chart number:");

            //setup patient
            patient = iOSys.selectPatientRecord(patientID);
            System.out.println("This patient has a record on file.");
            if (patient.isNullPatient()) {
                return;
            }


            while (true) {
                System.out.println("Name:    |" + patient.getName());
                System.out.println("Species: |" + patient.getSpecies());
                System.out.println("Sex:     |" + patient.getSex());
                System.out.println("DOB:     |" + patient.getDateOfBirth().toString());
                System.out.println("Flags:   |" + patient.getFlags());
                String choice = iOSys.displayMenu("Would you like to ", "Analyze blood values", "Search old tests", "Work with a different patient", "Quit");
                if (choice.equals("1")) {
                    analyzer.analyzeNewValues();
                } else if (choice.equals("2")) {
                    searchMain();
                } else if (choice.equals("3")) {
                    break;
                } else if (choice.equals("4")) {
                    return;
                }
            }
        }
    }


    public static void searchMain(){
        String filters = "";
        String filtersForDisplay = "";
        while (true) {
            String searchMethod = iOSys.displayMenu("::Selected filters:: " + "\n"
                    + filtersForDisplay + "\n"
                    + "\n"
                    + "Please add at least one filter:", "Test type", "Date", "Flags", "Search using selected filters", "Go back to patient menu");
            System.out.println();

            //tests
            if (searchMethod.equals("1")) {
                String choice = iOSys.displayMenu("Select a test type to filter:", "Chemistry", "CBC");
                if (choice.equals("1")) {
                    System.out.println("Not yet implemented. Filter not added.");
                    iOSys.waitForUser();
                } else if (choice.equals("2")) {
                    filters += "tCBC,";
                    filtersForDisplay += "CBC, ";
                }

            //dates
            } else if (searchMethod.equals("2")) {
                String date = iOSys.promptForInput("Please enter the date you would like to filter for (YYYY-MM-DD):");
                while (true) {
                    try {
                        LocalDate.parse(date);
                        break;
                    } catch (DateTimeParseException e) {
                        date = iOSys.promptForInput("That is not a valid date. Please try again.");
                    }
                }
                filters += "d" + date + ",";
                filtersForDisplay += date + ", ";

            //flags
            } else if (searchMethod.equals("3")) {
                String filterToAdd =  iOSys.promptForInput("Please enter a flag to filter for:");
                filters += "f" + filterToAdd + ",";
                filtersForDisplay += filterToAdd + ", ";

            //do the search!
            } else if (searchMethod.equals("4")){
                if (!filters.isEmpty()) {
                    System.out.println(iOSys.searchLog(filters));
                    iOSys.waitForUser();
                    return;
                } else {
                    System.out.println("Please enter at least one filter.");
                    iOSys.waitForUser();
                }

            //return to main menu
            } else if (searchMethod.equals("5")) {
                return;
            }
        }

    }

}