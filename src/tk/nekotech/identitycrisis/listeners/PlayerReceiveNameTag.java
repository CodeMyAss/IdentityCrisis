package tk.nekotech.identitycrisis.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import tk.nekotech.identitycrisis.IdentityCrisis;

public class PlayerReceiveNameTag implements Listener {
	private IdentityCrisis plugin;
	
	public PlayerReceiveNameTag(IdentityCrisis plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerReceiveNametag(PlayerReceiveNameTagEvent event) {
		if (event.getPlayer().getName().equals(event.getNamedPlayer().getName())) {
			return;
		}
		String name = event.getNamedPlayer().getName();
		if (plugin.contains(name)) {
			event.setTag(ChatColor.translateAlternateColorCodes('&', plugin.getName(name)));
		}
	}

}
