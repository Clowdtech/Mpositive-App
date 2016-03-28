package com.clowdtech.data.dbflow.entities;

import com.clowdtech.data.dbflow.ApplicationDatabase;
import com.clowdtech.data.dbflow.TypeConverter.DateTimeConverter;
import com.clowdtech.data.entities.ISyncData;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.joda.time.DateTime;

@Table(database = ApplicationDatabase.class, name = "SyncData")
public class SyncData extends BaseModel implements ISyncData {

    @PrimaryKey(autoincrement = true)
    long Id;

    @Column(name = "LastSync", typeConverter = DateTimeConverter.class)
    public DateTime lastSync;

    @Override
    public DateTime getLastSync() {
        return lastSync;
    }

    @Override
    public void setLastSync(DateTime currentDateStamp) {
        lastSync = currentDateStamp;
    }
}

