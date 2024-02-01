package org.example;
import java.util.*;

public class BloodParameter {
    String Name;
    double RangeLow;
    double RangeHigh;
    String Unit;

    public BloodParameter(String name, double rangeLow, double rangeHigh, String unit) {
        this.Name = name;
        this.RangeLow = rangeLow;
        this.RangeHigh = rangeHigh;
        this.Unit = unit;
    }

    public String analyzeParamter(Map.Entry<String, Double> bloodValue) {
        if (bloodValue.getValue() > 0){
        }
        return "";
    }

}
