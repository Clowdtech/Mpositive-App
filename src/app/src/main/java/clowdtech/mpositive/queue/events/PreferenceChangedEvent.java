package clowdtech.mpositive.queue.events;

import clowdtech.mpositive.areas.preferences.PreferenceEntry;

public class PreferenceChangedEvent<TVal> {
    private final PreferenceEntry key;
    private final TVal value;

    public PreferenceChangedEvent(PreferenceEntry key, TVal value) {
        this.key = key;
        this.value = value;
    }

    public PreferenceEntry getKey() {
        return key;
    }

    public TVal getValue() {
        return value;
    }
}
