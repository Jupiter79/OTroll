package at.orange.otroll.listeners;

import at.orange.otroll.OTroll;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.Collectors;

public class Vanish implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (OTroll.vanishedPlayers.contains(e.getPlayer().getUniqueId())) OTroll.joinVanish(e.getPlayer());

        OTroll.vanishedPlayers.stream().filter(x -> Bukkit.getPlayer(x) != null).map(Bukkit::getPlayer).collect(Collectors.toList()).forEach(p -> {
            e.getPlayer().hidePlayer(OTroll.plugin, p);
        });
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (OTroll.vanishedPlayers.contains(p.getUniqueId())) e.setCancelled(true);
        }
    }

    public static boolean isVanished(Player p) {
        return OTroll.vanishedPlayers.contains(p.getUniqueId());
    }
}
