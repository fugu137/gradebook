package net.mjduncan.gradebook.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberRounder {

    public static Double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int roundToInt(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        return bd.intValue();
    }
}

