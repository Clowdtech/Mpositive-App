package clowdtech.mpositive.areas.till.contracts;

import com.clowdtech.data.entities.IOrderLineManual;

import clowdtech.mpositive.areas.till.views.ManualLineEditableView;

public interface IManualLineSelection<TV, TVM> {
    void removeLine(ManualLineEditableView view, IOrderLineManual model);
}
