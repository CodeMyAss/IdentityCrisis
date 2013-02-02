package ch.jamiete.identitycrisis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Utility class to check for plugin updates.
 */
public class Updater implements Listener, Runnable {
    private final JavaPlugin plugin;
    private boolean updated = false;
    private String updVer;
    private final String dboSlug, pluginName, tag, curVer;

    public Updater(final JavaPlugin plugin, final String dboSlug) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.curVer = plugin.getDescription().getVersion();
        this.pluginName = plugin.getDescription().getName();
        this.tag = ChatColor.DARK_RED + "[" + ChatColor.RED + this.pluginName + ChatColor.DARK_RED + "] " + ChatColor.RED;
        this.dboSlug = dboSlug;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (!this.updated || !event.getPlayer().isOp()) {
            return;
        }
        final Player player = event.getPlayer();
        player.sendMessage(this.tag + "There is an updated version available: " + this.updVer);
        player.sendMessage(this.tag + "You can download it from http://dev.bukkit.org/server-mods/" + this.dboSlug + "/");
    }

    @Override
    public void run() {
        try {
            final URLConnection connection = new URL("http://d.jamiete.ch/" + this.pluginName + "/version.html").openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("User-agent", this.pluginName + " update checker. v" + this.plugin.getDescription().getVersion());
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String version;
            if ((version = bufferedReader.readLine()) != null) {
                this.updVer = version;
                if (!this.curVer.equals(version)) {
                    this.plugin.getLogger().info("An updated version was found: " + this.updVer);
                    this.plugin.getLogger().info("Get it from http://dev.bukkit.org/server-mods/" + this.dboSlug + "/");
                    this.updated = true;
                }
                return;
            }
            bufferedReader.close();
            connection.getInputStream().close();
        } catch (final Exception e) {
        }
        this.plugin.getLogger().info("Failed to check plugin version. Will check later.");
    }
}
