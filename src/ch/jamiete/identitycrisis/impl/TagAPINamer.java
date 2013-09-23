package ch.jamiete.identitycrisis.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;
import ch.jamiete.identitycrisis.IdentityCrisis;
import ch.jamiete.identitycrisis.Namer;

public class TagAPINamer implements Namer, Listener {
    private final IdentityCrisis plugin;

    public TagAPINamer(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEnable() {
        return this.plugin.getServer().getPluginManager().getPlugin("TagAPI") != null;
    }

    @Override
    public void enable() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void rename(Player whom, String to) {
        TagAPI.refreshPlayer(whom);
    }

    @EventHandler
    public void onPlayerReceiveNametag(final PlayerReceiveNameTagEvent event) {
        if (plugin.getNameManager().isChanged(event.getNamedPlayer().getName())) {
            String compiled = ChatColor.translateAlternateColorCodes('&', this.plugin.getNameManager().getChanged(event.getNamedPlayer().getName()));
            event.setTag(compiled.substring(0, 16));
        }
    }

}
