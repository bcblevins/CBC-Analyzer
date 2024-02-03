package org.example;


import java.util.*;




public class Main {
    public static BloodParameter wbc;
    public static BloodParameter rbc;
    public static BloodParameter hemoglobin;
    public static BloodParameter hematocrit;
    public static BloodParameter mcv;
    public static BloodParameter platelets;

    public static void main(String[] args) {
        //BloodParameter is a class that holds 4 values: Name, Low end of reference range, high end of reference range, and unit.
        //These will be used to determine if each blood value is in or out of range.
        wbc = new BloodParameter("White Blood Cells", 5, 14.1, " thousand/mcL");
        rbc = new BloodParameter("Red Blood Cells", 4.95, 7.87, " million/L");
        hemoglobin = new BloodParameter("Hemoglobin", 11.0, 18.9, " g/dl");
        hematocrit = new BloodParameter("Hematocrit", 35, 57, " %" );
        mcv = new BloodParameter( "Mean Crepuscular Volume", 66, 77, " fL");
        platelets = new BloodParameter( "Platelets", 211, 621, " thousand/mcL");

        //BloodParameter objects are put into Map so that they can be matched up to the Map of blood values to be analyzed
        //Each blood value
        Map<String, BloodParameter> bloodParameterMap = new HashMap<>() {{
            put(wbc.Name, wbc);
            put(rbc.Name, rbc);
            put(hemoglobin.Name, hemoglobin);
            put(hematocrit.Name, hematocrit);
            put(mcv.Name, mcv);
            put(platelets.Name, platelets);
        }};

        //Get randomBloodMap
        Map<String, Double> randomBloodMap = randomBloodParameters();

        //Loop through Map and apply conditional logic and output.
        for (Map.Entry<String, Double> bloodValue : randomBloodMap.entrySet()) {
            System.out.println(bloodParameterMap.get(bloodValue.getKey()).analyzeParameter(bloodValue.getValue()));
        }

    }

    public static Map<String, Double> randomBloodParameters() {
        Random rand = new Random();

        /*
        Create blood map with BloodParameter names for the keys and random double for the values. The range that the
        random number will generate inside is just outside the normal range for each parameter so we can demonstrate
        what the output will look like for values outside the normal range.

        Generating a random integer and dividing by 10.0/100.0/1.0 allows us to easily control the number of decimal places in line.
        This methodology comes with problems like:
          - The range numbers would be much more clear if we could use variables instead to demonstrate why we are using those numbers.
          - There is probably an explicit way to limit decimal length while keeping the types as doubles

         */
        Map<String, Double> bloodMap = new HashMap<>() {{
            put(wbc.Name, rand.nextInt(40, 150)/10.0);
            put(rbc.Name, rand.nextInt(450, 825)/100.0);
            put(hemoglobin.Name, rand.nextInt(119,189)/10.0);
            put(hematocrit.Name, rand.nextInt(20, 75)/1.0);
            put(mcv.Name, rand.nextInt(60, 85)/1.0);
            put(platelets.Name, rand.nextInt(190, 750)/1.0);
        }};

        
        return bloodMap;
    }
}