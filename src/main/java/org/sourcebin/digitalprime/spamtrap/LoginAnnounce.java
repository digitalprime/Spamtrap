package org.sourcebin.digitalprime.spamtrap;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class LoginAnnounce implements Runnable {

    final static Logger console = Logger.getLogger("Minecraft");
    static private final ArrayList<String> login = new ArrayList<>();
    static private final ArrayList<String> logoff = new ArrayList<>();
    private static Server theServer;

    static public void playerLogin(String user) {
        if (!login.contains(user)) {
            login.add(user);
        }

        logoff.remove(user);
    }

    static public void playerLogoff(String user) {
        if (!logoff.contains(user)) {
            logoff.add(user);
        }

        login.remove(user);
    }

    static public ArrayList<String> getLoginList(String user) {
        return login;
    }

    static public ArrayList<String> getLogoffList(String user) {
        return logoff;
    }

    public static void setServer(Server theServer) {
        LoginAnnounce.theServer = theServer;
    }

    public static Server getServer() {
        return theServer;
    }

    @Override
    public void run() {
        String loginPlayers = "";
        String logoffPlayers = "";

        if (!login.isEmpty()) {
            for (String player : login) {
                loginPlayers = loginPlayers + " ";
                loginPlayers = loginPlayers + player;
            }
            login.clear();
            getServer().broadcastMessage(MessageFormat.format("{0}{1}{2}{3}", ChatColor.GREEN,
                    "Joined:", ChatColor.DARK_AQUA, loginPlayers));
        }

        if (!logoff.isEmpty()) {
            for (String player : logoff) {
                logoffPlayers = logoffPlayers + " ";
                logoffPlayers = logoffPlayers + player;
            }
            logoff.clear();
            getServer().broadcastMessage(MessageFormat.format("{0}{1}{2}{3}", ChatColor.GREEN,
                    "Quit:", ChatColor.DARK_AQUA, logoffPlayers));
        }
    }
}
