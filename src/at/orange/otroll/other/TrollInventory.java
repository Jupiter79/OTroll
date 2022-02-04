package at.orange.otroll.other;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrollInventory {
    public static List<TrollInventory> inventories = new ArrayList<>();

    Inventory inventory;
    public Player troller;
    public Player victim;

    private final int inventorySize = 9 * 6;

    public TrollInventory(Player troller, Player victim) {
        this.troller = troller;
        this.victim = victim;
        this.inventory = Bukkit.createInventory(null, inventorySize, "§4§lTroll §2§l" + victim.getName());

        ItemStack nothing = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemStack nothingRed = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);

        ItemMeta meta = nothing.getItemMeta();
        meta.setDisplayName("§r");
        nothing.setItemMeta(meta);

        ItemMeta metaRed = nothingRed.getItemMeta();
        metaRed.setDisplayName("§r");
        nothingRed.setItemMeta(metaRed);

        TrollInventoryItem.items.forEach(x -> {
            if (x.targetOnly) {
                this.inventory.addItem(x.item);
                if (this.inventory.firstEmpty() <= inventorySize - 10)
                    this.inventory.setItem(this.inventory.firstEmpty(), nothing);
            } else {
                this.inventory.setItem(this.inventory.firstEmpty(), nothingRed);
                this.inventory.addItem(x.item);
            }
        });

        this.inventory.setItem(this.inventory.firstEmpty(), nothingRed);

        inventories.add(this);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
