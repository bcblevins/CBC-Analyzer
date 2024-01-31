package org.example;


import javax.swing.text.html.HTMLDocument;
import java.util.*;

import static java.util.Map.entry;

public class Main {
    public static void main(String[] args) {
        //BloodParameter is a class I made that holds 4 values: Name, Low end of reference range, high end of reference range, and unit.
        BloodParameter wbc = new BloodParameter("White Blood Cells", 5, 14.1, "x10^3 / mcL");
        BloodParameter rbc = new BloodParameter("Red Blood Cells", 4.95, 7.87, "x10^12/L");
        BloodParameter hemaglobin = new BloodParameter("Hemaglobin", 11.0, 18.9, "g/dl");
        BloodParameter hematacrit = new BloodParameter("Hematacrit", 35, 57, "%" );
        BloodParameter mcv = new BloodParameter( "Mean Corpuscular Volume", 66, 77, "fL");
        BloodParameter platelets = new BloodParameter( "Platelets", 211, 621, "x10^3/mcL");
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
        String hematacritOutput;
        String mcvOutput;
        String plateletsOutput;


        //Create random generator for blood map. (Don't do here, do outside main method.
        randomBloodParameters();

        //Loop through Map and apply conditional logic to fill output variables.

        //output
    }

    public static Map<String, Double> randomBloodParameters() {
        Random rand = new Random();

        //create blood map with parameters for the keys and random double for the values.
        Map<String, Double> bloodMap = new HashMap<>() {{
            put("wbc", 0.0);
            put("Hemoglobin", 0.0);
        }};

        
        return bloodMap;
    }
}