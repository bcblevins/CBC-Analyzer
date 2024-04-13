package org.bcb.model;


public class BloodParameter {

    private int id;
    private double result;
    private String name;
    private double rangeLow;
    private double rangeHigh;
    private String unit;
    private String analyzedBloodValue;
    private String normalRangeForOutput;
    private boolean isOutsideNormalRange;


    public BloodParameter(int id, double result, String name, double rangeLow, double rangeHigh, String unit) {
        this.id = id;
        this.result = result;
        this.name = name;
        this.rangeLow = rangeLow;
        this.rangeHigh = rangeHigh;
        this.unit = unit;
        analyzeParameter(this.result);
        this.normalRangeForOutput = rangeLow + " - " + rangeHigh;
    }

    public BloodParameter(int id, String name, double rangeLow, double rangeHigh, String unit) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public double getResult() {
        return result;
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
