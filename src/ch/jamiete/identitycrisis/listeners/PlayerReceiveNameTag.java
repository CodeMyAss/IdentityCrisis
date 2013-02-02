package ch.jamiete.identitycrisis.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import ch.jamiete.identitycrisis.IdentityCrisis;

public class PlayerReceiveNameTag implements Listener {
    private final IdentityCrisis plugin;

    public PlayerReceiveNameTag(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerReceiveNametag(final PlayerReceiveNameTagEvent event) {
        final String name = event.getNamedPlayer().getName();
        if (this.plugin.hasChanged(name)) {
            event.setTag(ChatColor.translateAlternateColorCodes('&', this.plugin.getName(name)));
        }
    }
}
