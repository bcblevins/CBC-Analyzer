package org.bcb.app;

import org.bcb.model.LabTest;
import org.bcb.model.Patient;
import org.bcb.model.Tag;

import java.util.HashMap;
import java.util.Map;

import static org.bcb.app.Main.*;

public class TagSystem {
    IOSystem iOSys = new IOSystem();

    public Patient addTags(Patient patient) {
        while (true) {
            String tagToAdd = iOSys.promptForInput("Please enter a tag to add (letters and spaces only) or hit enter to go back:");
            if (tagToAdd.isEmpty()) {   //string.matches(regex) returns a boolean for whether the string matches the regex
                return patient;
            }
            if (!tagToAdd.matches("\\D+")) {
                System.out.println("Not a valid tag.");
                continue;
            }
            if (patient.getTagNames().contains(tagToAdd)) {
                System.out.println("Tag already assigned to patient.");
            } else {
                Tag match = jdbcTagDao.searchForSingleTagByName(tagToAdd);
                if (match == null) {
                    String yOrN = iOSys.promptForInput("Is this a diagnosis? (y/n)");
                    match = new Tag(tagToAdd, yOrN.equals("y"));
                    match = jdbcTagDao.createTag(match);
                }
                jdbcPatientDao.linkTagToPatient(patient, match);
                patient.appendTags(match);
            }
            String keepGoing = iOSys.promptForInput("Would you like to add more tags? (y/n)");
            if (keepGoing.equals("n")) {
                break;
            }
        }
        return patient;
    }
    public LabTest addTags(LabTest labTest) {
        while (true) {
            String tagToAdd = iOSys.promptForInput("Please enter a tag to add (letters and spaces only):").toLowerCase();

            if (tagToAdd.isEmpty()) {   //string.matches(regex) returns a boolean for whether the string matches the regex
                return labTest;
            }
            if (!tagToAdd.matches("\\D+")) {
                System.out.println("Not a valid tag.");
                continue;
            }

            if (labTest.getTagNames().contains(tagToAdd)) {
                System.out.println("Tag already assigned to test.");
            } else {
                Tag match = jdbcTagDao.searchForSingleTagByName(tagToAdd);
                if (match == null) {
                    String yOrN = iOSys.promptForInput("Is this a diagnosis? (y/n)");
                    match = new Tag(tagToAdd, yOrN.equals("y"));
                    match = jdbcTagDao.createTag(match);
                }
                jdbcLabTestDao.linkTagToLabTest(labTest, match);
                labTest.appendTags(match);
            }

            String keepGoing = iOSys.promptForInput("Would you like to add more tags? (y/n)");
            if (keepGoing.equals("n")) {
                break;
            }

        }
        return labTest;
    }
    public Patient removeTagsFromPatient(Patient patient) {
        while (true) {
            Tag tagToDelete = null;
            int option = 0;
            for (Tag tag : patient.getTags()) {
                System.out.println(option + ") " + tag.toString());
                option++;
            }
            System.out.println(option + ") back");
            String tagChoice = iOSys.promptForInput("Please select a tag above by number to remove");
            if (tagChoice.equals("0")) {
                System.out.println("This is an age tag and cannot be removed.");
                iOSys.waitForUser();
                continue;
            } else if (tagChoice.equals(String.valueOf(option))) {
                return patient;
            }
            while (true) {

                try {
                    tagToDelete = patient.getTags().get(Integer.parseInt(tagChoice));
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please select a number option.");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("That is not a valid option.");
                }
                tagChoice = iOSys.promptForInput("");
            }

            jdbcPatientDao.unlinkTagFromPatient(patient, tagToDelete);
            String choice = iOSys.promptForInput("Would you like to delete another tag? (y/n)").toLowerCase();
            if (choice.equals("n")) {
                break;
            }
        }
        patient.setTags(jdbcTagDao.getTagsForPatient(patient));
        return patient;
    }
}
