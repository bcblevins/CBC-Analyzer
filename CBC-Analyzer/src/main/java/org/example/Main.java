package org.example;


import java.util.*;

public class Main {
    public static void main(String[] args) {
        //BloodParameter is a class I made that holds 4 values: Name, Low end of reference range, high end of reference range, and unit.
        BloodParameter wbc = new BloodParameter("White Blood Cells", 5, 14.1, "x10^3 / mcL");
        BloodParameter rbc = new BloodParameter("Red Blood Cells", 4.95, 7.87, "x10^12/L");
        BloodParameter hemaglobin = new BloodParameter("Hemaglobin", 11.0, 18.9, "g/dl");
        BloodParameter hematocrit = new BloodParameter("Hematocrit", 35, 57, "%" );
        BloodParameter mcv = new BloodParameter( "Mean Corpuscular Volume", 66, 77, "fL");
        BloodParameter platelets = new BloodParameter( "Platelets", 211, 621, "x10^3/mcL");

        //Create a Map of the BloodParameter objects so that we can get the right
         /*
         Fill in the rest of the parameters into BloodParameter instances like above. They are:
         - Red blood cells (rbc):              4.95 - 7.87        x10^12 / L
         - Hemoglobin:                         11.9 - 18.9        g/dL
         - Hematocrit:                         35 - 57            %
         - Mean corpuscular volume (mcv):      66 - 77            fL
         - Platelets:                          211 - 621          x10^3 / mcL
          */
        //Create output variables for each parameter. These don't need to be assigned, just declared.
        String wbcOutput;
        String rbcOutput;
        String hemaglobinOutput;
        String hematocritOutput;
        String mcvOutput;
        String plateletsOutput;

        //Create random generator for blood map. (Don't do here, do outside main method.
        Map<String, Double> randomBloodMap = randomBloodParameters();

        /*
        Loop through Map and apply conditional logic to fill output variables.

        Problems to solve:
         - get correct BloodParameter object based on bloodValue pulled from random Map
         - use analyzeParameter() method in the associated BloodParameter object to get an output String
         - apply output string to correct variable
         */

        for (Map.Entry<String, Double> bloodValue : randomBloodMap.entrySet()) {

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