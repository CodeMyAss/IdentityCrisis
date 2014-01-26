package ch.jamiete.identitycrisis;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;
import ch.jamiete.identitycrisis.commands.ChangeNameCommand;
import ch.jamiete.identitycrisis.commands.ResetNameCommand;
import ch.jamiete.identitycrisis.exceptions.TooBigException;
import ch.jamiete.identitycrisis.listeners.AsyncPlayerReceiveNameTag;
import ch.jamiete.identitycrisis.listeners.BorderControl;
import ch.jamiete.identitycrisis.listeners.PlayerLogin;
import ch.jamiete.identitycrisis.listeners.PlayerReceiveNameTag;

public class IdentityCrisis extends JavaPlugin {
    private ConcurrentHashMap<String, String> nameChanges;
    private boolean changeTab = true, changeChat = false;

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

            if (this.changeTab) {
                player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', newName));
            }

            if (this.changeChat) {
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName) + ChatColor.WHITE);
            }
        }
    }

    /**
     * Returns a username that is 16 characters or less.
     * @param name
     * @return
     */
    public String chopString(final String name) {
        if (name.length() <= 16) {
            return name;
        }
        return name.substring(0, 16);
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

    /**
     * Returns whether or not there is an active name change for specified user.
     * @param name
     * @return
     */
    public boolean hasChanged(final String name) {
        return this.nameChanges.containsKey(name);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Changed " + this.nameChanges.size() + " players names this session!");
    }

    @Override
    public void onEnable() {
        final PluginManager pm = this.getServer().getPluginManager();

        if (pm.getPlugin("TagAPI") == null) {
            this.getLogger().severe("Oops. I couldn't manage to find TagAPI.");
            this.getLogger().severe("It is required to use this plugin.");
            this.getLogger().severe("Download it at http://dev.bukkit.org/server-mods/tag");
            pm.disablePlugin(this);
            return;
        }

        boolean async = true;

        try {
            Class.forName("org.kitteh.tag.AsyncPlayerReceiveNameTagEvent");
        } catch (final ClassNotFoundException e) {
            async = false;
            this.getLogger().severe("Oops. Your TagAPI is outdated!");
            this.getLogger().severe("Please update it for a faster version of this plugin.");
            this.getLogger().severe("Download it at http://dev.bukkit.org/server-mods/tag");
        }

        pm.registerEvents(new PlayerLogin(this), this);

        if (async) {
            pm.registerEvents(new AsyncPlayerReceiveNameTag(this), this);
        } else {
            pm.registerEvents(new PlayerReceiveNameTag(this), this);
        }

        if (this.getConfig().getBoolean("bordercontrol", false)) {
            pm.registerEvents(new BorderControl(this), this);
        }

        this.changeChat = this.getConfig().getBoolean("changechat", false);
        this.changeTab = this.getConfig().getBoolean("changeplayerlist", true);

        this.getCommand("changename").setExecutor(new ChangeNameCommand(this));
        this.getCommand("resetname").setExecutor(new ResetNameCommand(this));

        for (final Player player : this.getServer().getOnlinePlayers()) {
            final String oldName = player.getName();
            final String newName = this.getDefinedName(oldName);
            if (!newName.equals(oldName)) {
                try {
                    this.addNameChange(oldName, newName);
                } catch (final TooBigException e) {
                    this.getLogger().severe("Error while changing name from memory:");
                    this.getLogger().severe(e.getMessage());
                }
                TagAPI.refreshPlayer(player);
            }
        }
    }

    @Override
    public void onLoad() {
        final String version = this.getConfig().getString("version");

        if (version == null || version != null && !version.equals("1.1")) {
            this.getConfig().options().copyDefaults(true);
        }

        this.nameChanges = new ConcurrentHashMap<String, String>();
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

            if (this.changeTab) {
                player.setPlayerListName(oldName);
            }

            if (this.changeChat) {
                player.setDisplayName(oldName);
            }
        }
    }
}
