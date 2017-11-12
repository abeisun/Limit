package com.limitwhack.limit;

public class Calculations {
    public static final double CONVERT_KILOGRAMS = 0.45359237;

    //Convert weight in pounds to weight in kilograms
    public double convertWeight(double weight) {
        double realWeight = weight * CONVERT_KILOGRAMS;
        return realWeight;
    }

    //Calculate BAC level
    public double calculateBAC(double weight,String gender, double standard_drinks) {

        double BACLevel;
        double realWeight = convertWeight(weight);

        if (gender.equals("Male")) {
            BACLevel = (((((0.9672 * standard_drinks)/(0.58 * realWeight)))-0.045) * 10);

        }
        else if (gender.equals("Female")){
            BACLevel = (((((0.9672 * standard_drinks)/(0.49 * realWeight)))-0.051) * 10);

        }
        else {
            BACLevel = (((((0.9672 * standard_drinks)/(0.49 * realWeight)))-0.048) * 10);
        }

        return BACLevel;
    }


}