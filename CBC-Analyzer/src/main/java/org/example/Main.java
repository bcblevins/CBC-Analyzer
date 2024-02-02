package org.example;


import java.util.*;

public class Main {
    public static void main(String[] args) {
        //BloodParameter is a class that holds 4 values: Name, Low end of reference range, high end of reference range, and unit.
        //These will be used to determine if each blood parameter is in or out of range.
        BloodParameter wbc = new BloodParameter("White Blood Cells", 5, 14.1, " thousand/mcL");
        BloodParameter rbc = new BloodParameter("Red Blood Cells", 4.95, 7.87, " million/L");
        BloodParameter hemoglobin = new BloodParameter("Hemoglobin", 11.0, 18.9, " g/dl");
        BloodParameter hematocrit = new BloodParameter("Hematocrit", 35, 57, " %" );
        BloodParameter mcv = new BloodParameter( "Mean Crepuscular Volume", 66, 77, " fL");
        BloodParameter platelets = new BloodParameter( "Platelets", 211, 621, " thousand/mcL");

        //Create BloodParameter object map.
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

        /*
        Loop through Map and apply conditional logic to fill output variables.

        Problems to solve:
         - get correct BloodParameter object based on bloodValue pulled from random Map
         - use analyzeParameter() method in the associated BloodParameter object to get an output String
              this works by passing the value of the blood parameter and returning a string for output.
         - apply output string to correct variable
         */
        for (Map.Entry<String, Double> bloodValue : randomBloodMap.entrySet()) {
            System.out.println(bloodParameterMap.get(bloodValue.getKey()).analyzeParameter(bloodValue.getValue()));
        }


        //output


    }

    public static Map<String, Double> randomBloodParameters() {
        Random rand = new Random();

        //create blood map with parameters for the keys and random double for the values.
        //Generating a random integer and dividing by 10.0/100.0/1.0 allows us to easily control the number of decimal places in line.
        Map<String, Double> bloodMap = new HashMap<>() {{
            put("White Blood Cells", rand.nextInt(40, 150)/10.0);
            put("Red Blood Cells", rand.nextInt(450, 825)/100.0);
            put("Hemoglobin", rand.nextInt(119,189)/10.0);
            put("Hematocrit", rand.nextInt(20, 75)/1.0);
            put("Mean Crepuscular Volume", rand.nextInt(60, 85)/1.0);
            put("Platelets", rand.nextInt(190, 750)/1.0);
        }};

        
        return bloodMap;
    }
}