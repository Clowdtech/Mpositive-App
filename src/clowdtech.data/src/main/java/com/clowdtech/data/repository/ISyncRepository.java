package com.clowdtech.data.repository;

import com.clowdtech.data.entities.ISyncData;

import org.joda.time.DateTime;

public interface ISyncRepository {

    ISyncData getLatestSync();

    void setLastSync(DateTime currentDateStamp);
}
