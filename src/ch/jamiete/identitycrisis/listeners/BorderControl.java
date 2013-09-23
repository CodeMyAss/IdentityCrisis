package ch.jamiete.identitycrisis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ch.jamiete.identitycrisis.IdentityCrisis;

// Changes the player's name in flow messages
public class BorderControl implements Listener {
    private final IdentityCrisis plugin;

    public BorderControl(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final String name = event.getPlayer().getName();
        final String newName = this.plugin.getNameManager().getChanged(name);

        if (newName != null) {
            event.setJoinMessage(event.getJoinMessage().replace(name, newName));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final String name = event.getPlayer().getName();
        final String newName = this.plugin.getNameManager().getChanged(name);

        if (newName != null) {
            event.setQuitMessage(event.getQuitMessage().replace(name, newName));
        }
    }

}
