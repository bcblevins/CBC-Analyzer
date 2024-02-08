package org.example;


import java.util.*;

/*
    Master problem list:
     + Random Blood Map:
       - Generating a random integer and dividing by 10.0/100.0/1.0 allows us to easily control the number of decimal places in line.
        This methodology comes with problems like:
          - The range numbers would be much more clear if we could use variables instead to demonstrate why we are using those numbers.
          - There is probably an explicit way to limit decimal length while keeping the types as doubles.
     + IOSystem:
       - Used import to access Main variables (import static org.example.Main.*;), bad style?

       TODO: See README
 */


public class Main {
    public static IOSystem iOSys = new IOSystem();
    //BloodParameter is a class that is constructed with 4 values: Name, Low end of reference range, high end of reference range, and unit.
    //These will be used to determine if each blood value is in or out of range, and will store the result of the analysis.
    public static BloodParameter wbc = new BloodParameter("White Blood Cells", 4, 15.5, " 10^3/mcL");
    public static BloodParameter rbc = new BloodParameter("Red Blood Cells", 4.8, 9.3, " 10^6/mcL");
    public static BloodParameter hemoglobin = new BloodParameter("Hemoglobin", 12.1, 20.3, " g/dl");
    public static BloodParameter hematocrit = new BloodParameter("Hematocrit", 36, 60, " %" );
    public static BloodParameter mcv = new BloodParameter( "Mean Crepuscular Volume", 58, 79, " fL");
    public static BloodParameter platelets = new BloodParameter( "Platelets", 170, 400, " 10^3/mcL");

    //BloodParameter objects are put into Map so that they can be matched up to the Map of blood values to be analyzed
    public static Map<String, BloodParameter> bloodParameterMap = new HashMap<>() {{
        put(wbc.getName(), wbc);
        put(rbc.getName(), rbc);
        put(hemoglobin.getName(), hemoglobin);
        put(hematocrit.getName(), hematocrit);
        put(mcv.getName(), mcv);
        put(platelets.getName(), platelets);
    }};

    public static void main(String[] args) {

        String choice = iOSys.displayMenu("Input your own blood values", "Generate random blood values (demo mode)");
        Map<String, Double> bloodInputMap;

        while (true) {
            if (choice.equals("1")) {
                bloodInputMap = iOSys.takeBloodValues();
                break;
            } else if (choice.equals("2")) {
                bloodInputMap = randomBloodValueGenerator();
                break;
            } else {
                System.out.println("Please select a valid (number) option.");
            }
        }

        //Loop through Map and call BloodParameter method analyzeParameter.
        for (Map.Entry<String, Double> bloodValue : bloodInputMap.entrySet()) {
            double currentValue = bloodValue.getValue();
            String currentKey = bloodValue.getKey();

            BloodParameter currentParameter = bloodParameterMap.get(currentKey);

            currentParameter.analyzeParameter(currentValue);
        }

        //Create list of bloodParameters so we can control the order for output.
        List<BloodParameter> bloodParameterList = new ArrayList<>() {{
            add(wbc);
            add(rbc);
            add(hemoglobin);
            add(hematocrit);
            add(mcv);
            add(platelets);
        }};

        iOSys.outputTable(bloodParameterList);

    }

    public static Map<String, Double> randomBloodValueGenerator() {
        Random rand = new Random();

        /*
        Create blood map with BloodParameter names for the keys and random double for the values. The range that the
        random number will generate inside is just outside the normal range for each parameter so we can demonstrate
        what the output will look like for values outside the normal range.

        Generating a random integer and dividing by 10.0/100.0 allows us to easily control the number of decimal places in line.
        This methodology comes with problems like:
          - The range numbers would be much more clear if we could use variables instead to demonstrate why we are using those numbers.
          - There is probably an explicit way to limit decimal length while keeping the types as doubles
         */
        Map<String, Double> bloodMap = new HashMap<>() {{
            put(wbc.getName(), rand.nextInt(40, 150)/10.0);
            put(rbc.getName(), rand.nextInt(450, 825)/100.0);
            put(hemoglobin.getName(), rand.nextInt(119,189)/10.0);
            put(hematocrit.getName(), (double)rand.nextInt(20, 75));
            put(mcv.getName(), (double)rand.nextInt(60, 85));
            put(platelets.getName(), (double)rand.nextInt(190, 750));
        }};

        
        return bloodMap;
    }


}