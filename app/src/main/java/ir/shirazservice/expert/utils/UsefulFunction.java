package ir.shirazservice.expert.utils;

import android.os.SystemClock;

public class UsefulFunction {


    public String[] preventDoubleCLick(long lastClickTime) {
        String[] retval = {"-1", ""};
        if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
            return retval;
        }

        lastClickTime = SystemClock.elapsedRealtime();
        retval[0] = "1";
        retval[1] = String.valueOf(lastClickTime);
        return retval;

    }

    public String attachCamma(String value) {
        String s;
        try {
            // The comma in the format specifier does the trick
            s = String.format("%,d", Integer.parseInt(value));
        } catch (NumberFormatException e) {
            s = "";
        }
        return s;
    }


    public String deAttachCamma(String value) {
        String retval = value.replace(",", "").replace("٬", "");
        return retval;
    }


}
