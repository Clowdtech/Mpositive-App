package clowdtech.mpositive.queue.events;

import java.util.List;

import clowdtech.mpositive.areas.shared.InventoryItem;

public class InventoryLoadedEvent {
    public List<InventoryItem> getAllInventory() {
        return allInventory;
    }

    private List<InventoryItem> allInventory;

    public InventoryLoadedEvent(List<InventoryItem> allInventory) {
        this.allInventory = allInventory;
    }
}
