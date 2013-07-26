package org.sourcebin.digitalprime.spamtrap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamTrapListener implements Listener {

    final SpamTrapPlugin plugin;
    final static Logger console = Logger.getLogger("Minecraft");

    public SpamTrapListener(SpamTrapPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().hasPermission("spamtrap.exempt") && plugin.getConfig().getBoolean("commands.ignore-duplicate")) {
            SpamTrapPlayer P = plugin.findPlayer(event.getPlayer());
            if (P.commandSpam.isSpam(event.getMessage()).isSpam() == true) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLogin(final PlayerLoginEvent event) {

        SpamTrapPlayer P = plugin.findPlayer(event.getPlayer());

        if (P.banishment.isBanned()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, P.banishment.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final SpamTrapPlayer p = plugin.findPlayer(event.getPlayer());

        p.chatSpam.setRemoveRepeating(plugin.getConfig().getBoolean("chat.remove-repeating"));
        p.chatSpam.setRemoveWhitespace(plugin.getConfig().getBoolean("chat.remove-whitespace"));
        p.chatSpam.setRemoveDuplicate(plugin.getConfig().getBoolean("chat.remove-duplicate"));

        p.commandSpam.setRemoveRepeating(false);
        p.commandSpam.setRemoveWhitespace(false);
        p.commandSpam.setRemoveDuplicate(plugin.getConfig().getBoolean("commands.ignore-duplicate"));

        //Try to detect vanish no packet.
        if (event.getJoinMessage().isEmpty()) {
            return;
        }

        if (plugin.getConfig().getBoolean("reduce-join-quit-messaging")) {
            //Add player name to the announce list
            LoginAnnounce.playerLogin(event.getPlayer().getName());
            event.setJoinMessage("");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (plugin.getConfig().getBoolean("reduce-join-quit-messaging")) {
            LoginAnnounce.playerLogoff(event.getPlayer().getName());
            event.setQuitMessage("");
        }
    }

    //DO NOT trust defaults in bukkit, there often wrongly set!
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        //Factions modifies the message and cancels the event!!
        //This is for faction chat i would assume. We still need to see the
        //event so we ignore the fact its been cancelled.

        Player p = event.getPlayer();
        //Do we need a log file?
        if (plugin.getConfig().getBoolean("chat.record")) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String file = formatter.format(new Date());

                SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm:ss");

                BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(new File(plugin.getDataFolder() + "/" + file + ".log"), true));
                writer.write(MessageFormat.format("{0} {1}: {2}\n", timeFmt.format(new Date()), p.getName(), event.getMessage()).getBytes());
                writer.flush();
            } catch (IOException a) {
                console.log(Level.WARNING, "SpamTrap: " + a.getMessage());
            }
        }

        if (p.hasPermission("spamtrap.exempt")) {
            return;
        }

        SpamTrapPlayer spam = plugin.findPlayer(p);

        if (spam.chatSpam.isMuted()) {
            event.setCancelled(true);
            p.sendMessage("You are muted!");
            return;
        }

        SpamResult result = spam.chatSpam.isSpam(event.getMessage());

        if (result.isSpam()) {
            event.setCancelled(true);
            spam.chatSpam.reset();

            String punishment = plugin.getConfig().getString("punishment");

            if (punishment.equalsIgnoreCase("kick")) {
                p.kickPlayer(plugin.getConfig().getString("messages.kick"));
                console.log(Level.INFO, MessageFormat.format("SpamTrap kicking {0} for spamming ({1}).", p.getName(), result.getMessage()));
            } else if (punishment.equalsIgnoreCase("mute")) {
                p.sendMessage(plugin.getConfig().getString("messages.mute"));
                spam.chatSpam.setMuted(true);
                return;
            } else if (punishment.equalsIgnoreCase("ban")) {
                p.kickPlayer(plugin.getConfig().getString("messages.ban"));
                console.log(Level.INFO, MessageFormat.format("SpamTrap banning {0} for spamming ({1}).", p.getName(), result.getMessage()));
                spam.banishment.setBanned("", plugin.getConfig().getInt("cooldown"));
            }
        }

        if ((result.getType() == SpamResult.resultEnum.LAST_DUPLICATE)
                && plugin.getConfig().getBoolean("chat.remove-duplicate")) {
            event.setCancelled(true);
            return;
        }

        event.setMessage(result.getMessage());
    }
}
