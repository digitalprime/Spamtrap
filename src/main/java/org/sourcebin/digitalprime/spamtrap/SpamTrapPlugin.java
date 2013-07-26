package org.sourcebin.digitalprime.spamtrap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;
import org.sourcebin.digitalprime.utils.FileUtils;

/**
 * SpamTrap Plugin
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamTrapPlugin extends JavaPlugin {

    public final static Logger console = Logger.getLogger("Minecraft");
    public final Map<String, SpamTrapPlayer> mapPlayer = new HashMap<>();
    final long HOURINTICKS = 60 * 60 * 20;
    final long MININTICKS = 60 * 20;
    final long SECINTICKS = 20;

    public SpamTrapPlugin() {
    }

    SpamTrapPlayer findPlayer(Player p) {
        String playerName = p.getPlayer().getName();
        SpamTrapPlayer player;

        if ((player = mapPlayer.get(playerName)) == null) {
            player = new SpamTrapPlayer();
            mapPlayer.put(playerName, player);
            return player;
        } else {
            return player;
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SpamTrapListener(this), this);

        getCommand("spamtrapreload").setExecutor(this);

        if (new File(getDataFolder(), "config.yml").exists()) {
            try {
                if (getConfig().getInt("version") != 2) {
                    console.log(Level.WARNING, "SpamTrap configuration file is out of date, resetting it.");
                    FileUtils.moveFile(getDataFolder() + "/config.yml", getDataFolder() + "/config.yml-old");
                    reloadConfig();
                    getConfig().options().copyDefaults(true);
                    getConfig().options().copyHeader(true);
                    saveConfig();
                }
            } catch (Exception e) {
                console.log(Level.SEVERE, e.getMessage());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } else {
            getConfig().options().copyDefaults(true);
            getConfig().options().copyHeader(true);
            saveConfig();
        }

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            //We actually dont care if the metrics has a problem
            //console.log(Level.INFO, MessageFormat.format("[Spamtrap] Metrics Error ''{0}''", e.getMessage()));
        }

        if (getConfig().getBoolean("reduce-join-quit-messaging")) {
            LoginAnnounce.setServer(getServer());
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new LoginAnnounce(), SECINTICKS * 5, SECINTICKS * 5);
        }

        if (getConfig().getBoolean("version-check")) {
            getServer().getScheduler().runTaskAsynchronously(this, new SpamTrapUpdate(this));
        }
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (sender.hasPermission("spamtrap.reload")) {
            reloadConfig();

            getServer().getScheduler().cancelTasks(this);
            if (getConfig().getBoolean("reduce-join-quit-messaging")) {
                LoginAnnounce.setServer(getServer());
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new LoginAnnounce(), SECINTICKS * 5, SECINTICKS * 5);
            }

            sender.sendMessage(ChatColor.GOLD + "SpamTrap configuration reloaded from disk.");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
            return false;
        }
    }
}
