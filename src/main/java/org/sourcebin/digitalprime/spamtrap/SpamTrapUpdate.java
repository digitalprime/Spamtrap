package org.sourcebin.digitalprime.spamtrap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

/**
 *
 * @author digitalprime <digitalprime@sourcebin.org>
 */
public class SpamTrapUpdate implements Runnable {

    private final SpamTrapPlugin plugin;
    final static Logger console = Logger.getLogger("Minecraft");

    public SpamTrapUpdate(SpamTrapPlugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        DocumentBuilder dbf;
        try {
            dbf = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dbf.parse("http://dev.bukkit.org/server-mods/SpamTrap/files.rss");
            XPath xpath = XPathFactory.newInstance().newXPath();
            String latest = xpath.evaluate("//item[1]/title", doc);

            String current = plugin.getDescription().getVersion();
            current.substring(1); //drop the 'v' at the start

            latest = latest.substring(latest.indexOf("-") + 1);

            if (!current.equalsIgnoreCase(latest)) {
                console.log(Level.INFO, "[SpamTrap] The latest version of SpamTrap is " + latest);
                console.log(Level.INFO, "[SpamTrap] It can be downloaded from http://dev.bukkit.org/server-mods/spamtrap/files/");
            }

        } catch (Exception e) {
            //We will ignore the error otherwise it will just end up in the logs and people think is a problem
            //when its not.
            //console.log(Level.INFO, MessageFormat.format("[SpamTrap] Update Error ''{0}''", e.getMessage()));
        }
    }

}
