package clowdtech.mpositive.queue;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventBus implements IEventBus {
    OttoMainThreadBus bus;

    @Inject
    public EventBus() {
        bus = new OttoMainThreadBus();
    }

    @Override
    public void register(Object registration) {
        this.bus.register(registration);
    }

    @Override
    public void unregister(Object registration) {
        this.bus.unregister(registration);
    }

    @Override
    public void post(Object notification) {
        this.bus.post(notification);
    }
}
