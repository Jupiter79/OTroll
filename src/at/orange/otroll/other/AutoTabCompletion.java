package at.orange.otroll.other;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AutoTabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = Arrays.asList("random");

        if (commandSender instanceof Player && commandSender.hasPermission("otroll.troll")) {
            List<String> canBeTrolled = Bukkit.getOnlinePlayers().stream().filter(x -> x.getUniqueId() != ((Player) commandSender).getUniqueId() && !x.hasPermission("otroll.bypass")).map(x -> x.getName()).collect(Collectors.toList());
            canBeTrolled.addAll(Arrays.asList("#random", "#vanish"));

            if (strings.length == 1) {
                return canBeTrolled;
            } else if (strings.length == 2) return completions;
        }
        return new ArrayList<>();
    }
}
