package ch.jamiete.identitycrisis;

import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NameManager {
    private final IdentityCrisis plugin;
    private final HashMap<String, String> changes = new HashMap<String, String>();

    public NameManager(IdentityCrisis plugin) {
        this.plugin = plugin;
    }

    public void process(Player player) {
        if (this.isChanged(player.getName())) {
            String compiled = ChatColor.translateAlternateColorCodes('&', this.getChanged(player.getName()));

            if (this.plugin.changeChat) {
                player.setDisplayName(compiled);
            }

            if (this.plugin.changeTab) {
                player.setPlayerListName(compiled.substring(0, 16));
            }

            plugin.namer.rename(player, compiled);
        }
    }

    public void onEnable() {
        for (Entry<String, Object> change : this.plugin.getConfig().getConfigurationSection("names").getValues(true).entrySet()) {
            final String original = change.getKey();

            if (change.getValue() instanceof String) {
                this.changes.put(original, (String) change.getValue());
            } else {
                plugin.getConfig().set("names." + original, null);
                plugin.getLogger().info("Removed name change for " + original + " as the value was not a String.");
            }
        }

        this.plugin.saveConfig();
    }

    public void onDisable() {
        for (Entry<String, String> change : changes.entrySet()) {
            plugin.getConfig().set("names." + change.getKey(), change.getValue());
        }

        this.plugin.saveConfig();
    }

    public void removeRename(String name) {
        changes.remove(name);

        this.plugin.getConfig().set("names." + name, null);
        this.plugin.saveConfig();
    }

    public void addRename(String name, String newname) {
        changes.put(name, newname);

        this.plugin.getConfig().set("names." + name, newname);
        this.plugin.saveConfig();
    }

    public boolean isChanged(String name) {
        return changes.containsKey(name);
    }

    public String getChanged(String name) {
        return changes.get(name);
    }

}
