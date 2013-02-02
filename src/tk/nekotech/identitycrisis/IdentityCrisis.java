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

    /**
     * Adds a name change for defined user.
     * <b>Saved to configuration immediately.</b>
     * @param oldName
     * @param newName
     * @throws TooBigException
     */
    public void addNameChange(final String oldName, final String newName) throws TooBigException {
        if (newName.length() > 16) {
            throw new TooBigException("Couldn't change " + oldName + " to " + newName + " as the new name is too long!");
        }
        this.nameChanges.put(oldName, newName);
        this.getConfig().set("names." + oldName, newName);
        this.saveConfig();
        final Player player = this.getServer().getPlayerExact(oldName);
        if (player != null) {
            TagAPI.refreshPlayer(player);
            player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', newName));
        }
    }

    /**
     * Returns a username that is 16 characters or less.
     * @param name
     * @return
     */
    public String chopString(final String name) {
        if (name.length() >= 16) {
            return name;
        }
        return name.substring(0, 16);
    }

    /**
     * Returns whether or not there is an active name change for specified user.
     * @param name
     * @return
     */
    public boolean contains(final String name) {
        return this.nameChanges.containsKey(name);
    }

    /**
     * Returns the defined name of the user.
     * If not set, returns normal name.
     * @param oldName
     * @return
     */
    public String getDefinedName(final String oldName) {
        final String newName = this.getConfig().getString("names." + oldName);
        return newName == null ? oldName : newName;
    }

    /**
     * Returns the changed name for a user, null if not changed.
     * @param name
     * @return
     */
    public String getName(final String name) {
        return this.nameChanges.get(name);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Changed " + this.nameChanges.size() + " players names this session!");
    }

    @Override
    public void onEnable() {
        final PluginManager pm = this.getServer().getPluginManager();
        if (pm.getPlugin("TagAPI") == null) {
            this.getLogger().severe("FAILED TO FIND TAGAPI. SHUTTING DOWN!");
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new PlayerLogin(this), this);
        pm.registerEvents(new PlayerReceiveNameTag(this), this);
        this.getCommand("changename").setExecutor(new ChangeNameCommand(this));
        this.getCommand("resetname").setExecutor(new ResetNameCommand(this));
        for (final Player player : this.getServer().getOnlinePlayers()) {
            final String oldName = player.getName();
            final String newName = this.getDefinedName(oldName);
            if (!newName.equals(oldName)) {
                try {
                    this.addNameChange(oldName, newName);
                } catch (final TooBigException e) {
                    this.getLogger().severe("The new name for " + oldName + " is too long! It can be maximum 16 characters. (" + newName + ")");
                }
                TagAPI.refreshPlayer(player);
            }
        }
    }

    @Override
    public void onLoad() {
        final String version = this.getConfig().getString("version");
        if (version == null || version != null && !version.equals("0.1")) {
            this.getConfig().options().copyDefaults(true);
        }
        this.nameChanges = new HashMap<String, String>();
    }

    /**
     * Removes a name change for defined user.
     * <b>Saved to configuration immediately.</b>
     * @param oldName
     */
    public void removeNameChange(final String oldName) {
        this.nameChanges.remove(oldName);
        this.getConfig().set("names." + oldName, null);
        this.saveConfig();
        final Player player = this.getServer().getPlayerExact(oldName);
        if (player != null) {
            TagAPI.refreshPlayer(player);
            player.setPlayerListName(oldName);
        }
    }
}
