package tk.nekotech.identitycrisis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import tk.nekotech.identitycrisis.IdentityCrisis;
import tk.nekotech.identitycrisis.exceptions.TooBigException;

public class PlayerLogin implements Listener {
    private final IdentityCrisis plugin;

    public PlayerLogin(final IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
        final String name = event.getPlayer().getName();
        final String newName = this.plugin.getDefinedName(name);
        if (newName != null) {
            try {
                this.plugin.addNameChange(name, newName);
            } catch (final TooBigException e) {
                this.plugin.getLogger().severe("The new name for " + name + " is too long! It can be maximum 16 characters. (" + newName + ")");
            }
        }
    }
}
