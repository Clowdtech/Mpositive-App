package clowdtech.mpositive.data;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

public interface IReceiptLine {
    DateTime getDateCreated();
    String getDisplayTitle();
    String getDisplaySubTitle();
    BigDecimal getPrice();
    Object getItem();
}

