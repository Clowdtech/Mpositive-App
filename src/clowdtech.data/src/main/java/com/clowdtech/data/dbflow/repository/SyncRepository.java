package com.clowdtech.data.dbflow.repository;

import com.clowdtech.data.dbflow.entities.SyncData;
import com.clowdtech.data.entities.ISyncData;
import com.clowdtech.data.repository.ISyncRepository;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.joda.time.DateTime;

public class SyncRepository implements ISyncRepository {
    @Override
    public ISyncData getLatestSync() {
        SyncData sync = SQLite.select()
                .from(SyncData.class)
                .limit(1)
                .querySingle();

        // first time sync
        if (sync == null) {
            sync = new SyncData();
            sync.lastSync = new DateTime(1); // hard coded should surely be the down of time?
        }

        return sync;
    }

    @Override
    public void setLastSync(DateTime currentDateStamp) {
        SyncData syncData = new SyncData();
        syncData.lastSync = currentDateStamp;
        syncData.save();
    }
}
