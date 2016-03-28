package clowdtech.mpositive;

public interface TaskListenerAsync<TDR> {
    void onSuccess(TDR data);
    void onFailure();
}
