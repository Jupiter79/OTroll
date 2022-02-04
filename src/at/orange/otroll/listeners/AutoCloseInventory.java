package at.orange.otroll.listeners;

import at.orange.otroll.other.TrollInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.stream.Collectors;

public class AutoCloseInventory implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        List<TrollInventory> inventoriesWithOfflinePlayers = TrollInventory.inventories.stream().filter(x -> x.victim.getUniqueId() == e.getPlayer().getUniqueId()).collect(Collectors.toList());

        if (inventoriesWithOfflinePlayers.size() > 0) {
            inventoriesWithOfflinePlayers.forEach(x -> {
                x.troller.closeInventory();

                x.troller.sendMessage("§cThe player §6" + x.troller.getName() + " §cwent offline and cannot be trolled anymore!\n§7The inventory has been closed");
            });
        }
    }
}
