package org.example;


public class BloodParameter {
    private String name;
    private double rangeLow;
    private double rangeHigh;
    private String unit;
    private String analyzedBloodValue;
    private String normalRangeForOutput;
    private boolean isOutsideNormalRange;


    public BloodParameter(String name, double rangeLow, double rangeHigh, String unit) {
        this.name = name;
        this.rangeLow = rangeLow;
        this.rangeHigh = rangeHigh;
        this.unit = unit;
        this.normalRangeForOutput = rangeLow + " - " + rangeHigh;
    }

    //This method fills the analyzedBloodValue with a string depending on how it compares to the normal range
    public void analyzeParameter(double bloodValue) {

        //Create a result based on how bloodValue compares to normal range, then create an output string for return.
        if (bloodValue > this.rangeHigh){
            this.analyzedBloodValue =  bloodValue + "(+)";
            isOutsideNormalRange = true;

        } else if (bloodValue < this.rangeLow) {
            this.analyzedBloodValue = bloodValue + "(-)";
            isOutsideNormalRange = true;

        } else {
            this.analyzedBloodValue = String.valueOf(bloodValue);
        }
    }

    public String getAnalyzedBloodValue() {
        return analyzedBloodValue;
    }

    public String getName() {
        return name;
    }

    public String getNormalRangeForOutput() {
        return normalRangeForOutput;
    }

    public boolean isOutsideNormalRange() {
        return isOutsideNormalRange;
    }

    public String getUnit() {
        return unit;
    }
}
