package org.example;
import java.util.*;

public class BloodParameter {
    String Name;
    double RangeLow;
    double RangeHigh;
    String Unit;
    String NormalRangeForOutput;
    static final String RED_ANSI_CODE = "\u001B[31m";
    static final String ANSI_RESET_CODE = "\u001B[0m";

    public BloodParameter(String name, double rangeLow, double rangeHigh, String unit) {
        this.Name = name;
        this.RangeLow = rangeLow;
        this.RangeHigh = rangeHigh;
        this.Unit = unit;
        this.NormalRangeForOutput = "  |  Normal Range: " + rangeLow + " - " + rangeHigh + unit;
    }

    public String analyzeParameter(double bloodValue) {
        String bloodValueFormattedForOutput;

        //Create a result based on how bloodValue compares to normal range, then create an output string for return.
        if (bloodValue > this.RangeHigh){
            bloodValueFormattedForOutput = RED_ANSI_CODE + this.Name + "(+) = " + bloodValue + ANSI_RESET_CODE;
            return bloodValueFormattedForOutput + this.NormalRangeForOutput;

        } else if (bloodValue < this.RangeLow) {
            bloodValueFormattedForOutput = RED_ANSI_CODE + this.Name + "(-) = " + bloodValue + ANSI_RESET_CODE;
            return bloodValueFormattedForOutput + this.NormalRangeForOutput;

        } else {
            bloodValueFormattedForOutput = this.Name + " = " + bloodValue;
            return bloodValueFormattedForOutput + this.NormalRangeForOutput;

        }
    }

}
