package org.bcb.app;

import org.bcb.model.BloodParameter;
import org.bcb.model.LabTest;
import org.bcb.model.Patient;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

import static org.bcb.app.Main.iOSys;
import static org.bcb.app.Main.tagSystem;

//TODO: Get blood parameters from DB so that they have IDs
public class Analyzer {
    private final IOSystem IO_SYSTEM = new IOSystem();
    //TODO: Make these not hard-coded
    //BloodParameters are used to evaluate blood values based on reference ranges, as well as store analyzed values
    private final BloodParameter WBC = new BloodParameter(1, "White Blood Cells", 4, 15.5, " 10^3/mcL");
    private final BloodParameter RBC = new BloodParameter(2, "Red Blood Cells", 4.8, 9.3, " 10^6/mcL");
    private final BloodParameter HEMOGLOBIN = new BloodParameter(3, "Hemoglobin", 12.1, 20.3, " g/dl");
    private final BloodParameter HEMATOCRIT = new BloodParameter(4, "Hematocrit", 36, 60, " %");
    private final BloodParameter MCV = new BloodParameter(5, "Mean Crepuscular Volume", 58, 79, " fL");
    private final BloodParameter PLATELETS = new BloodParameter(6, "Platelets", 170, 400, " 10^3/mcL");

    //BloodParameter objects are put into Map so that they can be matched up to the Map of blood values to be analyzed
    private final Map<String, BloodParameter> BLOOD_PARAMETER_MAP = new HashMap<>() {{
        put(WBC.getName(), WBC);
        put(RBC.getName(), RBC);
        put(HEMOGLOBIN.getName(), HEMOGLOBIN);
        put(HEMATOCRIT.getName(), HEMATOCRIT);
        put(MCV.getName(), MCV);
        put(PLATELETS.getName(), PLATELETS);
    }};

    private Map<String, Double> randomBloodValueGenerator() {
        Random rand = new Random();

        /*
        Create blood map with BloodParameter names for the keys and random double for the values. The range that the
        random number will generate inside is just outside the normal range for each parameter, so we can demonstrate
        what the output will look like for values outside the normal range.

        Generating a random integer and dividing by 10.0/100.0 allows us to easily control the number of decimal places in line.
        This methodology comes with problems like:
          - The range numbers would be much more clear if we could use variables instead to demonstrate why we are using those numbers.
          - There is probably an explicit way to limit decimal length while keeping the types as doubles
         */
        Map<String, Double> bloodMap = new HashMap<>() {{
            put(WBC.getName(), rand.nextInt(40, 150) / 10.0);
            put(RBC.getName(), rand.nextInt(450, 825) / 100.0);
            put(HEMOGLOBIN.getName(), rand.nextInt(119, 189) / 10.0);
            put(HEMATOCRIT.getName(), (double) rand.nextInt(20, 75));
            put(MCV.getName(), (double) rand.nextInt(60, 85));
            put(PLATELETS.getName(), (double) rand.nextInt(190, 750));
        }};


        return bloodMap;
    }

    public void analyzeNewValues(Patient patient) {
        String choice = IO_SYSTEM.displayMenu("Would you like to:", "Input your own blood values", "Generate random blood values (demo mode - will not save to patient record)", "Go back to patient menu");
        Map<String, Double> bloodInputMap = new HashMap<>();
        String tags = "";

        //TODO: MAKE THIS FALSE AFTER TESTING
        boolean isWrittenToDb = true;

        if (choice.equals("1")) {
            bloodInputMap = IO_SYSTEM.takeBloodValues();
            isWrittenToDb = true;
        } else if (choice.equals("2")) {
            bloodInputMap = randomBloodValueGenerator();
        } else if (choice.equals("3")) {
            return;
        }
        LabTest labTest = new LabTest();

        LocalDateTime timeStamp = LocalDateTime.now();

        //Loop through Map and call BloodParameter method analyzeParameter.
        for (Map.Entry<String, Double> bloodValue : bloodInputMap.entrySet()) {
            double currentValue = bloodValue.getValue();
            String currentKey = bloodValue.getKey();

            BloodParameter currentParameter = BLOOD_PARAMETER_MAP.get(currentKey);

            currentParameter.analyzeParameter(currentValue);
        }

        //Create list of bloodParameters, so we can control the order for output.
        List<BloodParameter> bloodParameterList = new ArrayList<>() {{
            add(WBC);
            add(RBC);
            add(HEMOGLOBIN);
            add(HEMATOCRIT);
            add(MCV);
            add(PLATELETS);
        }};


        labTest = Main.jdbcLabTestDao.createTest(bloodParameterList, timeStamp, patient);
        labTest.setType("CBC");

        String addTags = iOSys.displayMenu("Would you like to add tags for this test?", "Yes", "No");
        if (addTags.equals("1")) {
            labTest = tagSystem.addTags(labTest);
        }

        String outputTable = IO_SYSTEM.createTable(labTest, patient);
        IOSystem.printSeparator();
        System.out.println(outputTable);
        IOSystem.printSeparator();
    }


    //---------------------------
    //BloodParameter name Getters
    //---------------------------

    public String getWbcName() {
        return WBC.getName();
    }

    public String getRbcName() {
        return RBC.getName();
    }

    public String getHemoglobinName() {
        return HEMOGLOBIN.getName();
    }

    public String getHematocritName() {
        return HEMATOCRIT.getName();
    }

    public String getMcvName() {
        return MCV.getName();
    }

    public String getPlateletsName() {
        return PLATELETS.getName();
    }
}
