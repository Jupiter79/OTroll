package at.orange.otroll.commands;

import at.orange.otroll.OTroll;
import at.orange.otroll.other.TrollInventory;
import at.orange.otroll.other.TrollInventoryItem;
import at.orange.otroll.other.TrollInventoryUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CmdTroll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (p.hasPermission("otroll.troll")) {
                if (strings.length > 0) {
                    if (strings[0].equalsIgnoreCase("#random")) {
                        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(x -> x.getUniqueId() != p.getUniqueId() && !x.hasPermission("otroll.bypass")).collect(Collectors.toList());
                            if (!players.isEmpty()) {
                                Player target = players.get(new Random().nextInt(players.size()));

                                TrollInventory trollInventory = new TrollInventory(p, target);
                                p.openInventory(trollInventory.getInventory());
                            } else p.sendMessage(OTroll.prefix + " §cNo player which can be trolled has been found!");
                            return false;
                    } else if (strings[0].equalsIgnoreCase("#vanish")) {
                        if (p.hasPermission("otroll.vanish")) {
                            OTroll.toggleVanish(p);
                        } else p.sendMessage(OTroll.prefix + " §4You need the permission §aotroll.vanish§4!");
                        return false;
                    }
                    Player target = Bukkit.getPlayer(strings[0]);

                    if (target != null) {
                        if (target.getUniqueId() != p.getUniqueId()) {
                            if (!target.hasPermission("otroll.bypass")) {
                                if (strings.length > 1 && strings[1].equalsIgnoreCase("random")) {
                                    List<TrollInventoryItem> filteredItems = TrollInventoryItem.items.stream().filter(x -> x.targetOnly).collect(Collectors.toList());
                                    TrollInventoryItem randomItem = filteredItems.get(new Random().nextInt(filteredItems.size()));

                                    randomItem.onClick(new TrollInventoryUser(target));

                                    p.sendMessage(OTroll.prefix + " | §4RANDOM > " + randomItem.description.split("\n§c")[0] + " was chosen!");
                                } else {
                                    TrollInventory trollInventory = new TrollInventory(p, target);
                                    p.openInventory(trollInventory.getInventory());
                                }
                            } else p.sendMessage(OTroll.prefix + " §cYou cannot troll this player!");
                        } else p.sendMessage(OTroll.prefix + " §cYou cannot troll yourself, why would you? xD");
                    } else p.sendMessage(OTroll.prefix + " §cThe player §6" + strings[0] + " §cis not online!");
                } else p.sendMessage(OTroll.prefix + " §cYou need to define a §6player§c, §6#vanish §cor §6#random§c!");
            } else p.sendMessage(OTroll.prefix + " §4You need the permission §aotroll.troll§4!");
        } else Bukkit.getConsoleSender().sendMessage(OTroll.prefix + " Ay bro you're not real go login with an account on the server than you will get a wonderful and pretty GUI");
        return false;
    }
}
