package tk.nekotech.identitycrisis.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import tk.nekotech.identitycrisis.IdentityCrisis;

public class PlayerReceiveNameTag implements Listener {
    private final IdentityCrisis plugin;

    public PlayerReceiveNameTag(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerReceiveNametag(final PlayerReceiveNameTagEvent event) {
        if (event.getPlayer().getName().equals(event.getNamedPlayer().getName())) {
            return;
        }
        final String name = event.getNamedPlayer().getName();
        if (this.plugin.contains(name)) {
            event.setTag(ChatColor.translateAlternateColorCodes('&', this.plugin.getName(name)));
        }
    }
}
