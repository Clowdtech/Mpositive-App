package clowdtech.mpositive.areas.till.views;

import android.view.LayoutInflater;
import android.view.View;

import org.joda.time.DateTime;

public interface IReceiptLineView {
    View getView(LayoutInflater inflater);

    DateTime getSort();

    Long getIdentifier();
}
