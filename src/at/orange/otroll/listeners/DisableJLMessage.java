package at.orange.otroll.listeners;

import at.orange.otroll.OTroll;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisableJLMessage implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Vanish.isVanished(e.getPlayer())) {
            e.setJoinMessage("");

            e.getPlayer().sendMessage(OTroll.prefix + " §bYour join message has been §fhidden §b:^)");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (Vanish.isVanished(e.getPlayer())) {
            e.setQuitMessage("");
        }
    }
}
