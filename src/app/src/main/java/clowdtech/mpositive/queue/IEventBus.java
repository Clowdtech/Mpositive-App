package clowdtech.mpositive.queue;

public interface IEventBus {
    void register(Object registration);

    void unregister(Object registration);

    void post(Object notification);
}
