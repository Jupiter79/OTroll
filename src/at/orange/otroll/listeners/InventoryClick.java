package at.orange.otroll.listeners;

import at.orange.otroll.other.TrollInventory;
import at.orange.otroll.other.TrollInventoryItem;
import at.orange.otroll.other.TrollInventoryUser;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.stream.Collectors;

public class InventoryClick implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (TrollInventory.inventories.stream().map(TrollInventory::getInventory).collect(Collectors.toList()).contains(e.getClickedInventory())) {
            e.setCancelled(true);

            Player clicker = (Player) e.getWhoClicked();

            if (e.getClick() == ClickType.LEFT && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && !e.getCurrentItem().getItemMeta().getDisplayName().equals("")) {
                TrollInventoryItem clickedItem = TrollInventoryItem.items.stream().filter(x -> x.item.getType() == e.getCurrentItem().getType()).collect(Collectors.toList()).get(0);

                Player target = TrollInventory.inventories.stream().filter(x -> x.getInventory() == e.getClickedInventory()).collect(Collectors.toList()).get(0).victim;

                if (target.isOnline()) {
                    if (clickedItem.targetOnly) {
                        clickedItem.onClick(new TrollInventoryUser(target));
                    } else clickedItem.onClick(new TrollInventoryUser(target, clicker));


                    clicker.playSound(clicker.getLocation(), Sound.ENTITY_VILLAGER_YES, SoundCategory.MASTER, 1, 1);
                } else {
                    clicker.closeInventory();
                    clicker.sendMessage("§cThe player §6" + target.getName() + " §cwent offline and cannot be trolled anymore!\n§7The inventory has been closed");
                }
            }
        }
    }
}
