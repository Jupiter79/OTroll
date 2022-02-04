package at.orange.otroll.other;

import org.bukkit.entity.Player;

public class TrollInventoryUser {
    public Player target;
    public Player player;

    public TrollInventoryUser(Player target) {
        this.target = target;
    }

    public TrollInventoryUser(Player target, Player player) {
        this.target = target;
        this.player = player;
    }
}
