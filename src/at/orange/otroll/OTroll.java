package at.orange.otroll;

import at.orange.otroll.listeners.*;
import at.orange.otroll.other.bstats.Metrics;
import at.orange.otroll.commands.CmdTroll;
import at.orange.otroll.other.AutoTabCompletion;
import at.orange.otroll.other.TrollInventoryItem;
import at.orange.otroll.other.updater.UpdateChecker;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange.a;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import org.bukkit.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class OTroll extends JavaPlugin {
    public static Plugin plugin;
    public static String prefix = "§d§lOTroll §8|§r";
    public static boolean updateAvailable = false;
    public static String updateVersion;
    public static List<UUID> vanishedPlayers = new ArrayList();

    public void onEnable() {
        plugin = this;

        getServer().getConsoleSender().sendMessage("§aOTroll v" + getDescription().getVersion() + " has been successfully enabled!");

        // Metrics
        new Metrics(this, 12453);

        // Update
        new UpdateChecker(this, 88813).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                updateAvailable = false;
                updateVersion = null;
            } else {
                getServer().getConsoleSender().sendMessage(OTroll.prefix + " §cThere is a new update available! §7(§c" + this.getDescription().getVersion() + " §7-> §a" + version + "§7)");
                updateAvailable = true;
                updateVersion = version;
            }
        });

        setTrollItems();

        Arrays.asList(new InventoryClick(), new InventoryClose(), new AutoCloseInventory(), new Vanish(), new DisableJLMessage()).forEach(x -> {
            getServer().getPluginManager().registerEvents(x, this);
        });

        PluginCommand troll = getCommand("troll");
        if (troll != null) {
            troll.setExecutor(new CmdTroll());
            troll.setTabCompleter(new AutoTabCompletion());
        }
    }

    @Override
    public void onDisable() {
        OTroll.vanishedPlayers.stream().filter(x -> Bukkit.getPlayer(x) != null).map(x -> Bukkit.getPlayer(x)).collect(Collectors.toList()).forEach(x -> {
            x.sendMessage(OTroll.prefix + " §cYou are now visible because of a reload!");
            x.playSound(x.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 0.5F);

            x.setCollidable(true);
            Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(plugin, x));
        });
    }

    void setTrollItems() {
        new TrollInventoryItem("§cBurn", Material.FLINT_AND_STEEL, (t) -> {
            Player target = t.target;

            target.setFireTicks(20 * 5);
        }, "§7Lights the player up for 5 seconds\n§cWARNING: Death may occur", true);
        new TrollInventoryItem("§dFake Timeout", Material.STRING, (t) -> {
            Player target = t.target;

            target.kickPlayer("java.net.ConnectException: Connection timed out: no further information:");
        }, "§7Sends the player a fake timeout message\n(java.net.ConnectException...)", true);
        new TrollInventoryItem("§aDrop", Material.HOPPER, (t) -> {
            Player target = t.target;

            target.dropItem(true);
        }, "§7Drops the current item in the player's hand", true);
        new TrollInventoryItem("§cFake OP", Material.COMMAND_BLOCK, (t) -> {
            Player target = t.target;

            target.sendMessage("§7§o[Server: Made " + target.getName() + " a server operator]");
        }, "§7Lets the player think, he would have got OP", true);
        new TrollInventoryItem("§eLightning Strike", Material.TRIDENT, (t) -> {
            Player target = t.target;

            target.getWorld().strikeLightning(target.getLocation());
        }, "§7Lightning Strikes the player\n§cWARNING: Block damage and death may occur!", true);
        new TrollInventoryItem("§cScare", Material.SKELETON_SKULL, (t) -> {
            Player target = t.target;

            target.spawnParticle(Particle.MOB_APPEARANCE, target.getLocation(), 1, 0, 0, 0, 0);

            target.playSound(target.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.MASTER, 1, 1);
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
            for (int i = 0; i < 25; i++) {
                target.playSound(target.getLocation(), Sound.ITEM_TOTEM_USE, SoundCategory.MASTER, 1, 1);
                target.playSound(target.getLocation(), Sound.ENTITY_GHAST_HURT, SoundCategory.MASTER, 1, 1);
            }
        }, "§7Elder-jumpscares the player", true);
        new TrollInventoryItem("§6Fake Demo", Material.GOLD_BLOCK, (t) -> {
            Player target = t.target;

            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(new a(5), 0);
            ((CraftPlayer) target).getHandle().b.a(packet);
        }, "§7Displays the minecraft demo screen", true);
        new TrollInventoryItem("§dLook Random", Material.MAGENTA_GLAZED_TERRACOTTA, (t) -> {
            Player target = t.target;

            final int[] count = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (count[0] < 10) {
                        Location loc = target.getLocation();
                        loc.setYaw((float) (Math.floor(Math.random() * 180) - 180));
                        loc.setPitch((float) (Math.floor(Math.random() * 90) - 90));

                        target.teleport(loc);

                        count[0]++;
                    } else this.cancel();
                }
            }.runTaskTimer(this, 0, 10);
        }, "§7Lets the player look in a random direction for 5 seconds", true);
        new TrollInventoryItem("§bFreeze", Material.BLUE_ICE, (t) -> {
            Player target = t.target;

            Location freezeLoc = target.getLocation();

            final int[] count = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (count[0] / 20 <= 10) {
                        target.teleport(freezeLoc);

                        count[0]++;
                    } else this.cancel();
                }
            }.runTaskTimer(this, 0, 1);
        }, "§7Freezes the player for 10 seconds", true);
        new TrollInventoryItem("§4LAUNCH", Material.FIREWORK_ROCKET, (t) -> {
            Player target = t.target;

            Location toSpawn = target.getLocation();
            toSpawn.add(new Vector(0, 3, 0));

            target.setVelocity(new Vector(0, 25, 0));
            Firework firework = (Firework) toSpawn.getWorld().spawnEntity(toSpawn, EntityType.FIREWORK);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).trail(true).withColor(Color.ORANGE).withColor(Color.LIME).withColor(Color.RED).build());

            firework.setFireworkMeta(fireworkMeta);

            firework.setVelocity(new Vector(0, 3.45, 0));
        }, "§7Launches the player up with a firework explosion at the top\n§cWARNING: Death may occur!", true);
        new TrollInventoryItem("§7Hunger", Material.ROTTEN_FLESH, (t) -> {
            Player target = t.target;

            target.setFoodLevel(1);
        }, "§7Sets the player's hunger scale to 1", true);
        new TrollInventoryItem("§5Wither Sounds", Material.WITHER_SKELETON_SKULL, (t) -> {
            Player target = t.target;

            for (int i = 0; i < 50; i++) {
                target.playSound(target.getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1, 1);
            }
        }, "§7Lets the player think, 50 withers would have been spawned", true);
        new TrollInventoryItem("§4ALL effects", Material.POTION, (t) -> {
            Player target = t.target;

            Arrays.stream(PotionEffectType.values()).collect(Collectors.toList()).forEach(x -> {
                target.addPotionEffect(new PotionEffect(x, 20 * 10, 0, false, false));
            });
        }, "§7Gives the player all effects for 10 seconds", true);
        new TrollInventoryItem("§4Fake BAN", Material.NETHERITE_SWORD, (t) -> {
            Player target = t.target;

            target.kickPlayer("You are banned from this server!\nReason: Banned by an operator.");
        }, "§7Lets the player think, he would have been banned", true);
        new TrollInventoryItem("§2AntiCheat fake", Material.REDSTONE_TORCH, (t) -> {
            Player target = t.target;

            Location freezeLoc = target.getLocation();


            int delay = 20;
            for (int i = 0; i < 25; i++) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        target.teleport(freezeLoc);
                    }
                }.runTaskLater(plugin, delay);

                delay += new Random().nextInt(50 - 10) + 10;
            }
        }, "§7Bugs the player back 25 times with a random delay", true);
        new TrollInventoryItem("§cRAM overload", Material.REPEATER, (t) -> {
            Player target = t.target;

            target.sendMessage("Warning: Critial RAM overload (>95%)!\nIMMEDIATELY shut down computer to prevent further damage!");
        }, "§7Lets the player think his RAM would overload", true);
        new TrollInventoryItem("§4Explode", Material.TNT, (t) -> {
            Player target = t.target;

            TNTPrimed tnt = (TNTPrimed) target.getWorld().spawnEntity(target.getLocation(), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(0);
        }, "§7Summons a primed TNT at the player\n§cWARNING: Block damage and death may occur!", true);
        new TrollInventoryItem("§cKill", Material.TOTEM_OF_UNDYING, (t) -> {
            Player target = t.target;

            target.setHealth(0);
        }, "§7Kills the player", true);
        new TrollInventoryItem("§6Shuffle inventory", Material.KELP, (t) -> {
            Player target = t.target;

            List<ItemStack> contents = Arrays.asList(target.getInventory().getContents());
            Collections.shuffle(contents);

            target.getInventory().setContents(contents.toArray(new ItemStack[0]));
        }, "§7Shuffles the player's inventory", true);
        new TrollInventoryItem("§cDamage", Material.WOODEN_SWORD, (t) -> {
            Player target = t.target;

            target.damage(2);
        }, "§7Removes the player 1 heart", true);
        new TrollInventoryItem("§2Drop all items", Material.DROPPER, (t) -> {
            Player target = t.target;

            List<ItemStack> contents = Arrays.asList(target.getInventory().getContents());

            target.getInventory().clear();

            contents.forEach(x -> {
                if (x != null) {
                    Item dropped = target.getWorld().dropItem(target.getLocation(), x);
                    dropped.setPickupDelay(20 * 10);
                }
            });
        }, "§7Drops all items from the player's inventory\nand prevents the player pick up the items for 10 seconds", true);
        new TrollInventoryItem("§8Teleport below bedrock", Material.BEDROCK, (t) -> {
            Player target = t.target;

            Location loc = target.getLocation();
            loc.setY(-2);

            target.teleport(loc);
        }, "§7Teleports the player below bedrock\n§cWARNING: Death WILL occur", true);
        new TrollInventoryItem("§bDrown", Material.WATER_BUCKET, (t) -> {
            Player target = t.target;

            Location loc = target.getLocation();

            Location modifyX = target.getLocation();
            modifyX.add(1, 0, 0).getBlock().setType(Material.OBSIDIAN);
            modifyX.add(-2, 0, 0).getBlock().setType(Material.OBSIDIAN);
            modifyX.add(0, 1, 0).getBlock().setType(Material.OBSIDIAN);
            modifyX.add(2, 0, 0).getBlock().setType(Material.OBSIDIAN);

            Location modifyZ = target.getLocation();
            modifyZ.add(0, 0, 1).getBlock().setType(Material.OBSIDIAN);
            modifyZ.add(0, 0, -2).getBlock().setType(Material.OBSIDIAN);
            modifyZ.add(0, 1, 0).getBlock().setType(Material.OBSIDIAN);
            modifyZ.add(0, 0, 2).getBlock().setType(Material.OBSIDIAN);

            Location top = target.getLocation();
            top.add(0, 2, 0).getBlock().setType(Material.OBSIDIAN);

            Location bottom = target.getLocation();
            bottom.add(0, -1, 0).getBlock().setType(Material.OBSIDIAN);

            Location water = target.getLocation();
            water.getBlock().setType(Material.WATER);
            water.add(0, 1, 0).getBlock().setType(Material.WATER);

            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 60, 4));
            target.teleport(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5));
        }, "§7Builds an obsidian cage around the player with water inside\nand gives the player mining fatigue\n§cWARNING: Death and block damage may occur!", true);

        new TrollInventoryItem("§5§lTeleport to player", Material.ENDER_PEARL, (t) -> {
            t.player.teleport(t.target.getLocation());
        }, "§cWARNING: Make sure you are invisible!\n§bUse /troll #vanish to become invisible", false);
        new TrollInventoryItem("§6§lGive player gamemode creative", Material.DIAMOND_SWORD, (t) -> {
            t.target.setGameMode(GameMode.CREATIVE);
        }, "", false);
        new TrollInventoryItem("§a§lGive player gamemode survival", Material.GOLDEN_PICKAXE, (t) -> {
            t.target.setGameMode(GameMode.SURVIVAL);
        }, "", false);
        new TrollInventoryItem("§b§lGive player gamemode adventure", Material.WOODEN_HOE, (t) -> {
            t.target.setGameMode(GameMode.ADVENTURE);
        }, "§7Very funny because the player will wonder\n§7why he can't destroy any blocks", false);
    }

    public static void toggleVanish(Player p) {
        if (!vanishedPlayers.contains(p.getUniqueId())) {
            vanishedPlayers.add(p.getUniqueId());

            Bukkit.getOnlinePlayers().forEach(x -> x.hidePlayer(plugin, p));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

            p.sendMessage(OTroll.prefix + " §aYou are now invisible!\nYou won't pickup any items!");
        } else {
            vanishedPlayers.remove(p.getUniqueId());

            Bukkit.getOnlinePlayers().forEach(x -> x.showPlayer(plugin, p));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 0.5F);

            p.sendMessage(OTroll.prefix + " §cYou are now visible!");
        }
    }

    public static void joinVanish(Player p) {
        Bukkit.getOnlinePlayers().forEach(x -> x.hidePlayer(plugin, p));

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

        p.sendMessage(OTroll.prefix + " §aYou are still invisible!");
    }
}
