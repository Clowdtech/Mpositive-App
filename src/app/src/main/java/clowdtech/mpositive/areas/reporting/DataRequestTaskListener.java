package clowdtech.mpositive.areas.reporting;

public interface DataRequestTaskListener<TDL> {
    void onFinished(TDL data);
}
