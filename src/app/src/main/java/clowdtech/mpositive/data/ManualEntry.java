package clowdtech.mpositive.data;

import java.math.BigDecimal;
import java.util.UUID;

public class ManualEntry {
    private final UUID _id;
    private String _note;
    private BigDecimal _price;

    public ManualEntry(String note, BigDecimal price) {
        _note = note;
        _price = price;
        _id = java.util.UUID.randomUUID();
    }

    public String getNote() {
        return _note;
    }

    public String getDisplayTitle() {
        return "Manual Entry";
    }

    public String getDisplaySubTitle() {
        return _note;
    }

    public BigDecimal getUnitPrice() {
        return _price;
    }

    @Override
    public boolean equals(Object o) {
        ManualEntry toCompare;

        if (o instanceof ManualEntry) {
            toCompare = (ManualEntry)o;
        } else {
            return false;
        }

//        return this._note.equals(toCompare._note) && this._price == toCompare._price;
        return this._id.equals(toCompare._id);
    }
}
