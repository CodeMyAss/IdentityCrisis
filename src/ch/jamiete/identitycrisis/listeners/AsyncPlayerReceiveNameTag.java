package ch.jamiete.identitycrisis.listeners;

import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
import ch.jamiete.identitycrisis.IdentityCrisis;

public class AsyncPlayerReceiveNameTag implements Listener {
    private final IdentityCrisis plugin;

    public AsyncPlayerReceiveNameTag(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerReceiveNametag(final AsyncPlayerReceiveNameTagEvent event) {
        final UUID uuid = event.getNamedPlayer().getUniqueId();

        if (this.plugin.getManager().hasChanged(uuid)) {
            event.setTag(ChatColor.translateAlternateColorCodes('&', this.plugin.getManager().getName(uuid)));
        }
    }
}
