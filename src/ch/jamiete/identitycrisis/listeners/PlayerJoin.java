package ch.jamiete.identitycrisis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ch.jamiete.identitycrisis.IdentityCrisis;

public class PlayerJoin implements Listener {
    private final IdentityCrisis plugin;

    public PlayerJoin(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(final PlayerJoinEvent event) {
        this.plugin.getNameManager().process(event.getPlayer());
    }
}
