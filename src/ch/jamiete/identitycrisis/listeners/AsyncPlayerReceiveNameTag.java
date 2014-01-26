package ch.jamiete.identitycrisis.listeners;

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
        final String name = event.getNamedPlayer().getName();

        if (this.plugin.hasChanged(name)) {
            event.setTag(ChatColor.translateAlternateColorCodes('&', this.plugin.getName(name)));
        }
    }
}
