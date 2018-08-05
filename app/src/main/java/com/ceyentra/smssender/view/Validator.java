package com.ceyentra.smssender.view;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class Validator {
    public static boolean isValidNumber(String text) {
        if (text != null && !text.isEmpty() && isNumeric(text)){
            return true;
        }
        return false;
    }

    public static boolean isValidString(String text) {
        if (text != null && !text.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
