package org.bcb.app;

import org.apache.commons.dbcp2.BasicDataSource;
import org.bcb.dao.JdbcBloodParameterDao;
import org.bcb.dao.JdbcPatientDao;
import org.bcb.dao.JdbcLabTestDao;
import org.bcb.dao.JdbcTagDao;
import org.bcb.model.Patient;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/*
    TODO:
     Patient: update

 */
public class Main {
    public static IOSystem iOSys = new IOSystem();
    public static Analyzer analyzer = new Analyzer();
    public static Patient patient;
    public static BasicDataSource dataSource;
    public static JdbcPatientDao jdbcPatientDao;
    public static JdbcLabTestDao jdbcLabTestDao;
    public static JdbcBloodParameterDao jdbcBloodParameterDao;
    public static JdbcTagDao jdbcTagDao;

    public static void main(String[] args) {
        dataSource = new BasicDataSource();
        dataSource.setUrl(System.getenv("URL"));
        dataSource.setUsername(System.getenv("USERNAME"));
        dataSource.setPassword(System.getenv("PASSWORD"));

        jdbcPatientDao = new JdbcPatientDao(dataSource);
        jdbcBloodParameterDao = new JdbcBloodParameterDao(dataSource);
        jdbcLabTestDao = new JdbcLabTestDao(dataSource);
        jdbcTagDao = new JdbcTagDao(dataSource);

        while (true) {
            String chartNumber = iOSys.promptForInput("Please enter a patient chart number, or hit enter to search by name:");

            if (chartNumber.isEmpty()) {
                patient = searchPatientMain();
                if (patient == null) {
                    continue;
                }
            } else {
                try {
                    Integer.parseInt(chartNumber);
                } catch (NumberFormatException e) {
                    System.out.println("That is not a valid option. Please try again.");
                    iOSys.waitForUser();
                    continue;
                }
                patient = iOSys.selectPatientRecord(chartNumber);
                if (patient == null) {
                    continue;
                } else if (patient.isQuitPatient()) {
                    return;
                }
            }
            //setup patient


            while (true) {
                System.out.println("Name:    |" + patient.getName());
                System.out.println("Species: |" + patient.getSpecies());
                System.out.println("Sex:     |" + patient.getSex());
                System.out.println("DOB:     |" + patient.getDateOfBirth().toString());
                System.out.println("Flags:   |" + patient.getFlags());
                String choice = iOSys.displayMenu("Would you like to ",
                        "Analyze blood values",
                        "Search old tests",
                        "Work with a different patient",
                        "Quit");
                if (choice.equals("1")) {
                    analyzer.analyzeNewValues(patient);
                } else if (choice.equals("2")) {
                    searchTestMain();
                } else if (choice.equals("3")) {
                    break;
                } else if (choice.equals("4")) {
                    return;
                }
            }
        }
    }


    public static void searchTestMain(){
        String filters = "";
        String filtersForDisplay = "";
        while (true) {
            String searchMethod = iOSys.displayMenu("::Selected filters:: " + "\n"
                    + filtersForDisplay + "\n"
                    + "\n"
                    + "Please add at least one filter:", "LabTest type", "Date", "Flags", "Search using selected filters", "Go back to patient menu");
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
                    iOSys.searchForTests(filters, patient);
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

    public static Patient searchPatientMain() {
        Patient match = null;
        String name = iOSys.promptForInput("Please enter all or part of the name you would like to search:");
        List<Patient> matches = jdbcPatientDao.getPatientsByName(name, true);
        int option = 0;
        if (matches.isEmpty()) {
            System.out.print("No matches.");
            iOSys.waitForUser();
            return match;
        }
        System.out.println("Matches:");
        for (Patient patient : matches) {
            System.out.println(option + ") " + patient.toString());
            option++;
        }
        while (true) {
            String choice = iOSys.promptForInput("Please select a patient above by number");
            try {
                match = matches.get(Integer.parseInt(choice));
                return match;
            } catch (NumberFormatException e) {
                System.out.println("Please select a number option.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("That is not a valid option.");
            }
        }
    }

}