package tk.nekotech.identitycrisis;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;
import tk.nekotech.identitycrisis.commands.ChangeNameCommand;
import tk.nekotech.identitycrisis.commands.ResetNameCommand;
import tk.nekotech.identitycrisis.exceptions.TooBigException;
import tk.nekotech.identitycrisis.listeners.PlayerLogin;
import tk.nekotech.identitycrisis.listeners.PlayerReceiveNameTag;

public class IdentityCrisis extends JavaPlugin {
    private HashMap<String, String> nameChanges;

    public void onLoad() {
        String version = this.getConfig().getString("version");
        if (version == null || version != null && !version.equals("0.1")) {
            this.getConfig().options().copyDefaults(true);
        }
        nameChanges = new HashMap<String, String>();
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        if (pm.getPlugin("TagAPI") == null) {
            getLogger().severe("FAILED TO FIND TAGAPI. SHUTTING DOWN!");
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new PlayerLogin(this), this);
        pm.registerEvents(new PlayerReceiveNameTag(this), this);
        this.getCommand("changename").setExecutor(new ChangeNameCommand(this));
        this.getCommand("resetname").setExecutor(new ResetNameCommand(this));
        for (Player player : this.getServer().getOnlinePlayers()) {
            String oldName = player.getName();
            String newName = getDefinedName(oldName);
            if (!newName.equals(oldName)) {
                try {
                    addNameChange(oldName, newName);
                } catch (TooBigException e) {
                    this.getLogger().severe("The new name for " + oldName + " is too long! It can be maximum 16 characters. (" + newName + ")");
                }
                TagAPI.refreshPlayer(player);
            }
        }
    }

    public void onDisable() {
        this.getLogger().info("Changed " + nameChanges.size() + " players names this session!");
    }

    /**
     * Returns the defined name of the user.
     * If not set, returns normal name.
     * @param oldName
     * @return
     */
    public String getDefinedName(String oldName) {
        String newName = this.getConfig().getString("names." + oldName);
        return (newName == null ? oldName : newName);
    }

    /**
     * Adds a name change for defined user.
     * <b>Saved to configuration immediately.</b>
     * @param oldName
     * @param newName
     * @throws TooBigException
     */
    public void addNameChange(String oldName, String newName) throws TooBigException {
        if (newName.length() > 16) {
            throw new TooBigException("Couldn't change " + oldName + " to " + newName + " as the new name is too long!");
        }
        nameChanges.put(oldName, newName);
        this.getConfig().set("names." + oldName, newName);
        this.saveConfig();
        Player player = this.getServer().getPlayerExact(oldName);
        if (player != null) {
            TagAPI.refreshPlayer(player);
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', newName));
        }
    }

    /**
     * Removes a name change for defined user.
     * <b>Saved to configuration immediately.</b>
     * @param oldName
     */
    public void removeNameChange(String oldName) {
        nameChanges.remove(oldName);
        this.getConfig().set("names." + oldName, null);
        this.saveConfig();
        Player player = this.getServer().getPlayerExact(oldName);
        if (player != null) {
            TagAPI.refreshPlayer(player);
            player.setPlayerListName(oldName);
        }
    }

    /**
     * Returns whether or not there is an active name change for specified user.
     * @param name
     * @return
     */
    public boolean contains(String name) {
        return nameChanges.containsKey(name);
    }

    /**
     * Returns the changed name for a user, null if not changed.
     * @param name
     * @return
     */
    public String getName(String name) {
        return nameChanges.get(name);
    }
}
