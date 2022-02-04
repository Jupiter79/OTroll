package at.orange.otroll.other.updater;

import at.orange.otroll.OTroll;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MessageOps implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().isOp() && OTroll.updateAvailable) {
            e.getPlayer().sendMessage(OTroll.prefix + " §aThere's a new update available! §7(§c" + OTroll.plugin.getDescription().getVersion() + " §7-> §a" + OTroll.updateVersion + "§7)\n§bhttps://www.spigotmc.org/resources/otroll.88813/");
        }
    }
}
