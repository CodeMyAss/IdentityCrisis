package ch.jamiete.identitycrisis.listeners;

import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ch.jamiete.identitycrisis.IdentityCrisis;

public class BorderControl implements Listener {
    private final IdentityCrisis plugin;

    public BorderControl(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final String newName = this.plugin.getManager().getDefinedName(uuid);

        if (newName != null) {
            event.setJoinMessage(event.getJoinMessage().replace(event.getPlayer().getName(), newName));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final String newName = this.plugin.getManager().getDefinedName(uuid);

        if (newName != null) {
            event.setQuitMessage(event.getQuitMessage().replace(event.getPlayer().getName(), newName));
        }
    }

}
