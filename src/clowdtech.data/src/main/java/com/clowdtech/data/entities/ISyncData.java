package com.clowdtech.data.entities;

import org.joda.time.DateTime;

public interface ISyncData {
    DateTime getLastSync();

    void setLastSync(DateTime currentDateStamp);
}
