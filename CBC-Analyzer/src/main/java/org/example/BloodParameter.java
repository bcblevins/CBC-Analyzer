package org.example;
import java.util.*;



public class BloodParameter {
    private String Name;
    private double RangeLow;
    private double RangeHigh;
    private String Unit;
    private String analyzedBloodValue;
    private String NormalRangeForOutput;
    private boolean isOutsideNormalRange;


    public BloodParameter(String name, double rangeLow, double rangeHigh, String unit) {
        this.Name = name;
        this.RangeLow = rangeLow;
        this.RangeHigh = rangeHigh;
        this.Unit = unit;
        this.NormalRangeForOutput = rangeLow + " - " + rangeHigh;
    }

    //This method fills the analyzedBloodValue with a string depending on how it compares to the normal range
    public void analyzeParameter(double bloodValue) {
        String bloodValueFormattedForOutput;

        //Create a result based on how bloodValue compares to normal range, then create an output string for return.
        if (bloodValue > this.RangeHigh){
            this.analyzedBloodValue =  bloodValue + "(+)";
            isOutsideNormalRange = true;

        } else if (bloodValue < this.RangeLow) {
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
        return Name;
    }

    public String getNormalRangeForOutput() {
        return NormalRangeForOutput;
    }

    public boolean isOutsideNormalRange() {
        return isOutsideNormalRange;
    }

    public String getUnit() {
        return Unit;
    }
}
