package com.jacky.compatible.launcher.features.eliminateprocess;

import java.text.DecimalFormat;

public final class TextFormater {

    private TextFormater() throws InstantiationException {
        throw new InstantiationException("This class is not created for instantiation");
    }

    public static String longtoString(long size) {
        DecimalFormat format = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "byte";
        } else if (size < (1 << 20))        // Shift 20 bits to the left (* 1000000)
        {
            float kSize = size >> 10;       // Shift right by 10 (/ 1000)
            return format.format(kSize) + "KB";
        } else if (size < (1 << 30))        // Shift 30 bits to left (* 1000000000)
        {
            float mSize = size >> 20;       // Shift right by 20 (/ 1000000)
            return format.format(mSize) + "MB";
        } else if (size < (1 << 40)) {      // Shift 40 bits to left (* 1000000000000)
            float gSize = size >> 30;       // Shift right by 30 (/ 1000000000)
            return format.format(gSize) + "GB";
        } else {
            return "size error";
        }
    }

    public static String floattoString(float size) {
        if (size < 0) {
            return String.valueOf(0);
        } else {
            return size + "MB";
        }
    }
}
