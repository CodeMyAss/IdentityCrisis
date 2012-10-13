package tk.nekotech.identitycrisis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import tk.nekotech.identitycrisis.IdentityCrisis;
import tk.nekotech.identitycrisis.exceptions.TooBigException;

public class PlayerLogin implements Listener {
    private IdentityCrisis plugin;

    public PlayerLogin(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        String newName = plugin.getDefinedName(name);
        if (newName != null) {
            try {
                plugin.addNameChange(name, newName);
            } catch (TooBigException e) {
                plugin.getLogger().severe("The new name for " + name + " is too long! It can be maximum 16 characters. (" + newName + ")");
            }
        }
    }
}
