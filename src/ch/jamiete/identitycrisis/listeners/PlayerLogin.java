package ch.jamiete.identitycrisis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import ch.jamiete.identitycrisis.IdentityCrisis;
import ch.jamiete.identitycrisis.exceptions.TooBigException;

public class PlayerLogin implements Listener {
    private final IdentityCrisis plugin;

    public PlayerLogin(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
        final String name = event.getPlayer().getName();
        final String newName = this.plugin.getManager().getDefinedName(name);
        if (newName != null) {
            try {
                this.plugin.getManager().addNameChange(name, newName);
            } catch (final TooBigException e) {
                this.plugin.getLogger().severe("Error while changing name from memory:");
                this.plugin.getLogger().severe(e.getMessage());
            }
        }
    }
}
