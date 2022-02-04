package at.orange.otroll.listeners;

import at.orange.otroll.other.TrollInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.stream.Collectors;

public class InventoryClose implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (TrollInventory.inventories.stream().map(TrollInventory::getInventory).collect(Collectors.toList()).contains(e.getInventory())) {
            TrollInventory toRemove = TrollInventory.inventories.stream().filter(x -> x.getInventory() == e.getInventory()).collect(Collectors.toList()).stream().findFirst().get();

            TrollInventory.inventories.remove(toRemove);
        }
    }
}
