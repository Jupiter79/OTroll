package at.orange.otroll.other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TrollInventoryItem {
    public static List<TrollInventoryItem> items = new ArrayList<>();

    public String name;
    public ItemStack item;
    public Consumer<TrollInventoryUser> action;
    public String description;
    public boolean targetOnly;

    public TrollInventoryItem(String name, Material item, Consumer<TrollInventoryUser> action, String description, boolean targetOnly) {
        this.name = name;
        this.action = action;
        this.description = description;
        this.targetOnly = targetOnly;

        this.item = new ItemStack(item, 1);

        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        meta.setLore(Arrays.asList(description.split("\n").clone()));

        this.item.setItemMeta(meta);

        items.add(this);
    }

    public void onClick(TrollInventoryUser user) {
        if (user.player != null) {
            action.accept(new TrollInventoryUser(user.target, user.player));
        } else action.accept(new TrollInventoryUser(user.target));
    }
}
