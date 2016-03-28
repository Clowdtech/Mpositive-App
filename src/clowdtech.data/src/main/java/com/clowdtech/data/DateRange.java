package com.clowdtech.data;

public class DateRange {
    private final long lowerStamp;
    private final long upperStamp;

    public DateRange(long lowerStamp, long upperStamp) {
        this.lowerStamp = lowerStamp;
        this.upperStamp = upperStamp;
    }

    public long getLowerStamp() {
        return lowerStamp;
    }

    public long getUpperStamp() {
        return upperStamp;
    }
}
